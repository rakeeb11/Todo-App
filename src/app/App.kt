package app

import app.task.TaskItem
import app.task.events.OnTaskCreate
import app.task.events.OnTaskMigrate
import app.utils.*
import org.w3c.dom.Element
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLUListElement
import kotlin.browser.document


val taskInput by lazy { document.getTypedElementById<HTMLInputElement>(INPUT) }
val addTaskAction by lazy { document.getTypedElementById<HTMLButtonElement>(ADD_TASK) }
val pendingTasks by lazy { document.getTypedElementById<HTMLUListElement>(TODO) }
val completedTasks by lazy { document.getTypedElementById<HTMLUListElement>(DONE) }

val taskMigrationListener = object : OnTaskMigrate {

    override fun onTaskPending(item: TaskItem, task: Element) {
        item.type = TODO
        pendingTasks.appendChild(task)
    }

    override fun onTaskDone(item: TaskItem, task: Element) {
        item.type = DONE
        completedTasks.appendChild(task)
    }
}

val taskListener = object : OnTaskCreate {

    override fun onSuccess(task: String) {
        // initialize item and add to task
        pendingTasks.addTask(createTask(TaskItem(task, TODO), taskMigrationListener))
        // clear text
        taskInput.value = ""
    }

    override fun onError() {
        // TODO show error on UI
        console.log("onError")
    }
}


fun main(args: Array<String>) {
    addUserInteractions()
}

private fun addUserInteractions() {
    // initialize listeners
    val buttonListener = ButtonEventListener(taskInput, taskListener)
    val inputListener = InputEventListener(addTaskAction)
    // add interactions to UI
    addTaskAction.addEventListener(ACTION_CLICK, buttonListener)
    taskInput.addEventListener(ACTION_KEY, inputListener)
}


