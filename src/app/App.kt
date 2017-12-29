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

lateinit var taskInput: HTMLInputElement
lateinit var addTaskAction: HTMLButtonElement
lateinit var pendingTasks: HTMLUListElement
lateinit var completedTasks : HTMLUListElement

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
        addTask(pendingTasks, createTask(TaskItem(task, TODO), taskMigrationListener))
        // clear text
        taskInput.value = ""
    }

    override fun onError() {
        // TODO show error on UI
        console.log("onError")
    }
}

fun main(args: Array<String>) {
    initViews()
    addUserInteractions()
}

private fun initViews() {
    taskInput = document.getElementById(INPUT) as HTMLInputElement
    addTaskAction = document.getElementById(ADD_TASK) as HTMLButtonElement
    pendingTasks = document.getElementById(TODO) as HTMLUListElement
    completedTasks = document.getElementById(DONE) as HTMLUListElement
}
private fun addUserInteractions() {
    // initialize listeners
    val buttonListener = ButtonEventListener(taskInput, taskListener)
    val inputListener = InputEventListener(addTaskAction)
    // add interactions to UI
    addTaskAction.addEventListener(ACTION_CLICK, buttonListener)
    taskInput.addEventListener(ACTION_KEY, inputListener)
}


