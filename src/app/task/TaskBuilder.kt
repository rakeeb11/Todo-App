package app.task

const val TASK_PENDING = 0
const val TASK_DONE = 1

typealias TypeStatus = Int

class Task {
    lateinit var task: String
    var status: TypeStatus = TASK_PENDING
    operator fun unaryPlus() = {
        this.task = task
    }
}

class TaskBuilder {

    val tasks = mutableListOf<Task>()

    fun addTask(block: Task.() -> Unit) {
        val task = Task()
        task.block()
        tasks.add(task)
    }

    operator fun invoke(block: TaskBuilder.() -> Unit) {
        block()
    }
}