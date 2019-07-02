package org.hexworks.microcline.extensions

import org.hexworks.cobalt.events.api.CancelState
import org.hexworks.cobalt.events.api.Subscription
import org.hexworks.zircon.api.extensions.handleKeyboardEvents
import org.hexworks.zircon.api.extensions.handleMouseEvents
import org.hexworks.zircon.api.uievent.KeyboardEvent
import org.hexworks.zircon.api.uievent.KeyboardEventHandler
import org.hexworks.zircon.api.uievent.KeyboardEventType
import org.hexworks.zircon.api.uievent.MouseEvent
import org.hexworks.zircon.api.uievent.MouseEventHandler
import org.hexworks.zircon.api.uievent.MouseEventType
import org.hexworks.zircon.api.uievent.Pass
import org.hexworks.zircon.api.uievent.Processed
import org.hexworks.zircon.api.uievent.UIEventPhase
import org.hexworks.zircon.api.uievent.UIEventResponse
import org.hexworks.zircon.api.uievent.UIEventSource

// TODO: move these to Zircon

/**
 * Adds a handler for all types of [MouseEvent]s.
 */
fun UIEventSource.onAnyMouseEvent(handler: MouseEventHandler) {
    MouseEventType.values().forEach {
        handleMouseEvents(it, handler)
    }
}

/**
 * Adds a handler for all types of [KeyboardEvent]s.
 */
fun UIEventSource.onAnyKeyboardEvent(handler: KeyboardEventHandler) {
    KeyboardEventType.values().forEach {
        handleKeyboardEvents(it, handler)
    }
}

/**
 * Adds a handler for [KeyboardEvent]s of the given [eventType]
 * and [phase].
 */
inline fun UIEventSource.onKeyboardEvent(
        eventType: KeyboardEventType,
        phase: UIEventPhase,
        crossinline fn: (KeyboardEvent) -> UIEventResponse): Subscription {
    return handleKeyboardEvents(eventType) { event, currentPhase ->
        if (phase == currentPhase) {
            fn(event)
        } else Pass
    }
}

/**
 * Adds a handler for [MouseEvent]s of the given [eventType]
 * and [phase].
 */
inline fun UIEventSource.handleMouseEvents(
        eventType: MouseEventType,
        phase: UIEventPhase,
        crossinline fn: (MouseEvent) -> UIEventResponse): Subscription {
    return handleMouseEvents(eventType) { event, currentPhase ->
        if (phase == currentPhase) {
            fn(event)
        } else Pass
    }
}

/**
 * Adds a handler for [MouseEvent]s of the given [eventType]
 * and [phase].
 */
inline fun UIEventSource.processMouseEvents(
        eventType: MouseEventType,
        phase: UIEventPhase,
        crossinline fn: (MouseEvent) -> Unit): Subscription {
    return handleMouseEvents(eventType) { event, currentPhase ->
        if (phase == currentPhase) {
            fn(event)
            Processed
        } else Pass
    }
}

/**
 * Adds a handler for [KeyboardEvent]s of the given [eventTypes]
 * and [phases].
 */
inline fun UIEventSource.onKeyboardEvent(
        eventTypes: Iterable<KeyboardEventType> = KeyboardEventType.values().asIterable(),
        phases: Iterable<UIEventPhase> = UIEventPhase.values().asIterable(),
        crossinline fn: (KeyboardEvent, UIEventPhase) -> UIEventResponse): Subscription {
    return eventTypes.map {
        handleKeyboardEvents(it) { event, currentPhase ->
            if (phases.contains(currentPhase)) {
                fn(event, currentPhase)
            } else Pass
        }
    }.let {
        CompositeSubscription(it)
    }
}

/**
 * Adds a handler for [MouseEvent]s of the given [eventTypes]
 * and [phases].
 */
inline fun UIEventSource.handleMouseEvents(
        eventTypes: Iterable<MouseEventType> = MouseEventType.values().asIterable(),
        phases: Iterable<UIEventPhase> = UIEventPhase.values().asIterable(),
        crossinline fn: (MouseEvent, UIEventPhase) -> UIEventResponse): Subscription {
    return eventTypes.map {
        handleMouseEvents(it) { event, currentPhase ->
            if (phases.contains(currentPhase)) {
                fn(event, currentPhase)
            } else Pass
        }
    }.let {
        CompositeSubscription(it)
    }
}

class CompositeSubscription(val subscriptions: Iterable<Subscription>) : Subscription {

    override val cancelState: CancelState
        get() = subscriptions.first().cancelState

    override fun cancel(cancelState: CancelState) {
        subscriptions.forEach { it.cancel(cancelState) }
    }

}
