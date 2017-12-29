@file:Suppress("UNCHECKED_CAST")

package app.utils

import org.w3c.dom.Document
import org.w3c.dom.Element

fun <T> Document.createTypedElement(element: String) = createElement(element) as T

fun <T> Document.getTypedElementById(id: String) = getElementById(id) as T

fun <T : Element> Document.createView(type: String, block: T.() -> Unit) = createTypedElement<T>(type).apply {
    this.block()
}