package app.utils

import app.task.TaskItem
import app.task.events.OnTaskCreate
import app.task.events.OnTaskMigrate
import org.w3c.dom.*
import org.w3c.dom.events.Event
import org.w3c.dom.events.EventListener
import org.w3c.dom.events.KeyboardEvent
import kotlin.browser.document

fun createTask(item: TaskItem, taskMigrateListener: OnTaskMigrate) = document.createView<HTMLLIElement>(ELEMENT_LI) {
    appendChild(createCheckbox(item, TaskCheckboxListener(item, taskMigrateListener)))
    appendChild(createLabel(item.description))
}

fun createCheckbox(item: TaskItem, eventListener: EventListener) = document.createView<HTMLInputElement>(ELEMENT_INPUT) {
    type = ELEMENT_CHECKBOX
    checked = item.type != TODO
    addEventListener(ACTION_CLICK, eventListener)
}

fun createLabel(task: String) = document.createView<HTMLLabelElement>(ELEMENT_SPAN) {
    textContent = task
}

fun HTMLUListElement.addTask(task: Element) = appendChild(task)


class ButtonEventListener(private val task: HTMLInputElement,
                          private val listener: OnTaskCreate) : EventListener {

    override fun handleEvent(event: Event) {
        if (task.value.trim().isBlank()) {
            listener.onError()
            return
        }
        listener.onSuccess(task.value.trim())
    }
}

class InputEventListener(private val button: HTMLButtonElement) : EventListener {

    override fun handleEvent(event: Event) {
        val code = (event as KeyboardEvent).keyCode
        if (code != 13) return
        button.click()
    }
}

class TaskCheckboxListener(private var item: TaskItem,
                           private val taskMigrationListener: OnTaskMigrate) : EventListener {

    override fun handleEvent(event: Event) {
        val checkbox = event.target as HTMLInputElement
        checkbox.checked = item.type == TODO
        val task = checkbox.parentElement!!
        when (item.type) {
            TODO -> taskMigrationListener.onTaskDone(item, task)
            DONE -> taskMigrationListener.onTaskPending(item, task)
        }
    }
}
