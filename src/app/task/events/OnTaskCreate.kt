package app.task.events

interface OnTaskCreate {
    fun onSuccess(task: String)
    fun onError()
}