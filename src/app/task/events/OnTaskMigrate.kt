package app.task.events

import app.task.TaskItem
import org.w3c.dom.Element

interface OnTaskMigrate {
    fun onTaskPending(item: TaskItem, task: Element)
    fun onTaskDone(item: TaskItem, task: Element)
}