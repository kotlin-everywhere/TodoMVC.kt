package com.github.kotline_everywhere.todomvc.state

enum class TodoState {
    ACTIVE, COMPLETE
}

data class Todo(val pk: Int, val text: String, val state: TodoState, val editing: Boolean) {
    fun setEditing(editing: Boolean) {
        Manager.mapTodoList(this) { it.copy(editing = editing) }
    }

    fun setText(text: String) {
        Manager.mapTodoList(this) { it.copy(text = text) }
    }

    fun delete() {
        Manager.deleteTodo(this)
    }

    companion object {
        var sequence: Int = 0
            private set

        fun nextSequence() = ++sequence
    }

    fun setState(state: TodoState) {
        Manager.mapTodoList(this) { it.copy(state = state) }
    }
}

enum class TodoListFilter {
    ALL, ACTIVE, COMPLETE
}

data class State(internal val todoList: Array<Todo>, val todoListFilter: TodoListFilter) {
    fun filterTodoList(state: TodoState) = todoList.filter { it.state == state }.toTypedArray()

    val activeTodoList: Array<Todo>
        get() = filterTodoList(TodoState.ACTIVE)

    val completeTodoList: Array<Todo>
        get() = filterTodoList(TodoState.COMPLETE)

    val filteredTodoList: Array<Todo>
        get() = when (todoListFilter) {
            TodoListFilter.ALL -> todoList
            TodoListFilter.ACTIVE -> activeTodoList
            TodoListFilter.COMPLETE -> completeTodoList
        }

    fun setTotoListFilter(todoListFilter: TodoListFilter) {
        Manager.setTodoListFilter(todoListFilter)
    }

    fun addTodo(text: String) {
        if (text.isNotBlank()) {
            Manager.addTodo(Todo(Todo.nextSequence(), text, TodoState.ACTIVE, false))
        }
    }
}


object Manager {
    var state = State(arrayOf(), TodoListFilter.ALL)
        private set(value) {
            field = value
            receivers.forEach { it() }
        }

    private val receivers = arrayListOf<() -> Unit>()

    fun subscribe(receiver: () -> Unit) {
        receivers.add(receiver)
    }

    fun mapTodoList(todo: Todo, map: (Todo) -> Todo) {
        val todoList = state.todoList
                .map {
                    when (it) {
                        todo -> map(todo)
                        else -> it
                    }
                }
                .toTypedArray()
        state = state.copy(todoList = todoList)
    }

    fun addTodo(todo: Todo) {
        state = state.copy(todoList = state.todoList + todo)
    }

    fun deleteTodo(todo: Todo) {
        state = state.copy(todoList = state.todoList.filter { it !== todo }.toTypedArray())
    }

    fun setTodoListFilter(todoListFilter: TodoListFilter) {
        state = state.copy(todoListFilter = todoListFilter)
    }
}

