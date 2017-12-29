package app.utils

import app.task.TaskItem
import app.task.events.OnTaskCreate
import app.task.events.OnTaskMigrate
import org.w3c.dom.*
import org.w3c.dom.events.Event
import org.w3c.dom.events.EventListener
import org.w3c.dom.events.KeyboardEvent
import kotlin.browser.document

fun createTask(item: TaskItem, taskMigrateListener: OnTaskMigrate): HTMLLIElement {
    val element = document.createElement(ELEMENT_LI) as HTMLLIElement
    element.appendChild(createCheckbox(item, TaskCheckboxListener(item, taskMigrateListener)))
    element.appendChild(createLabel(item.description))
    return element
}

fun createCheckbox(item: TaskItem, eventListener: EventListener): HTMLInputElement {
    val checkbox = document.createElement(INPUT) as HTMLInputElement
    checkbox.type = ELEMENT_CHECKBOX
    checkbox.checked = item.type != TODO
    checkbox.addEventListener(ACTION_CLICK, eventListener)
    return checkbox
}

fun createLabel(task: String): HTMLSpanElement {
    val label = document.createElement(ELEMENT_SPAN) as HTMLSpanElement
    label.textContent = task
    return label
}


fun addTask(group: HTMLUListElement, task: Element) = group.appendChild(task)


class ButtonEventListener(private val task: HTMLInputElement,
                          private val listener: OnTaskCreate): EventListener {

    override fun handleEvent(event: Event) {
        if(task.value.trim().isBlank()) {
            listener.onError()
            return
        }
        listener.onSuccess(task.value.trim())
    }
}

class InputEventListener(private val button: HTMLButtonElement): EventListener {

    override fun handleEvent(event: Event) {
        val code = (event as KeyboardEvent).keyCode
        if(code != 13) return
        button.click()
    }
}

class TaskCheckboxListener(private var item: TaskItem,
                           private val taskMigrationListener: OnTaskMigrate): EventListener {

    override fun handleEvent(event: Event) {
        val checkbox = event.target as HTMLInputElement
        checkbox.checked = item.type == TODO
        val task = checkbox.parentElement!!
        when(item.type) {
            TODO -> taskMigrationListener.onTaskDone(item, task)
            DONE -> taskMigrationListener.onTaskPending(item, task)
        }
    }
}
