package org.hexworks.microcline.data.events

import org.hexworks.cobalt.events.api.Event


data class FileChanged(val file: String): Event
