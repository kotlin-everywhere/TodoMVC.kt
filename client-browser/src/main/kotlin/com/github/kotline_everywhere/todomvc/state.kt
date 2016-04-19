package com.github.kotline_everywhere.todomvc

enum class TodoState {
    ACTIVE, COMPLETE
}

data class Todo(val text: String, val state: TodoState)

data class State(val todoList: Array<Todo>)

object Manager {
    var state = State(arrayOf())
        private set(value) {
            field = value
            receivers.forEach { it() }
        }

    private val receivers = arrayListOf<() -> Unit>()

    fun subscribe(receiver: () -> Unit) {
        receivers.add(receiver)
    }
}

