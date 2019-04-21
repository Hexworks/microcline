package org.hexworks.microcline.extensions

import org.hexworks.cobalt.events.api.CancelState
import org.hexworks.cobalt.events.api.Subscription
import org.hexworks.zircon.api.extensions.onKeyboardEvent
import org.hexworks.zircon.api.extensions.onMouseEvent
import org.hexworks.zircon.api.uievent.*

// TODO: move these to Zircon

/**
 * Adds a handler for all types of [MouseEvent]s.
 */
fun UIEventSource.onAnyMouseEvent(handler: MouseEventHandler) {
    MouseEventType.values().forEach {
        onMouseEvent(it, handler)
    }
}

/**
 * Adds a handler for all types of [KeyboardEvent]s.
 */
fun UIEventSource.onAnyKeyboardEvent(handler: KeyboardEventHandler) {
    KeyboardEventType.values().forEach {
        onKeyboardEvent(it, handler)
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
    return onKeyboardEvent(eventType) { event, currentPhase ->
        if (phase == currentPhase) {
            fn(event)
        } else Pass
    }
}

/**
 * Adds a handler for [MouseEvent]s of the given [eventType]
 * and [phase].
 */
inline fun UIEventSource.onMouseEvent(
        eventType: MouseEventType,
        phase: UIEventPhase,
        crossinline fn: (MouseEvent) -> UIEventResponse): Subscription {
    return onMouseEvent(eventType) { event, currentPhase ->
        if (phase == currentPhase) {
            fn(event)
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
        onKeyboardEvent(it) { event, currentPhase ->
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
inline fun UIEventSource.onMouseEvent(
        eventTypes: Iterable<MouseEventType> = MouseEventType.values().asIterable(),
        phases: Iterable<UIEventPhase> = UIEventPhase.values().asIterable(),
        crossinline fn: (MouseEvent, UIEventPhase) -> UIEventResponse): Subscription {
    return eventTypes.map {
        onMouseEvent(it) { event, currentPhase ->
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
