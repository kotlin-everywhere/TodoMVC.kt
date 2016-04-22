package com.github.kotlin_everywhere.todomvc.store

data class Todos(val list: List<Todo>, val listFilter: TodoListFilter) {
    val activeList by lazy { filterList(TodoState.ACTIVE) }
    val completeList by lazy { filterList(TodoState.COMPLETE) }
    val filteredList by lazy {
        when (listFilter) {
            TodoListFilter.ALL -> list
            TodoListFilter.ACTIVE -> activeList
            TodoListFilter.COMPLETE -> completeList
        }
    }

    fun addTodo(text: String) {
        if (text.isBlank()) {
            return
        }
        setList(list + Todo(Todo.nextSequence(), text, TodoState.ACTIVE, false))
    }

    fun completeAllTodos() {
        setList(list.map { it.copy(state = TodoState.COMPLETE) })
    }

    fun setTodoState(todo: Todo, state: TodoState) {
        setTodo(todo) { copy(state = state) }
    }

    fun setTodoEditing(todo: Todo, editing: Boolean) {
        setTodo(todo) { copy(editing = editing) }
    }

    fun deleteTodo(todo: Todo) {
        setList(list.filter { it !== todo })
    }

    fun setTodoText(todo: Todo, text: String) {
        setTodo(todo) { copy(text = text) }
    }

    private fun setTodo(todo: Todo, body: Todo.() -> Todo) {
        setList(list.map {
            when (it) {
                todo -> todo.body()
                else -> it
            }
        })
    }

    private fun setList(list: List<Todo>) {
        store.setTodos(copy(list = list))
    }

    private fun filterList(state: TodoState) = list.filter { it.state == state }

    fun setListFilter(listFilter: TodoListFilter) {
        store.setTodos(copy(listFilter = listFilter))
    }

    fun deleteAllCompleteTodos() {
        setList(list.filter { it.state != TodoState.COMPLETE })
    }
}

enum class TodoListFilter {
    ALL, ACTIVE, COMPLETE
}

data class Todo(val pk: Int, val text: String, val state: TodoState, val editing: Boolean) {
    companion object {
        var sequence = 0;
            private set

        fun nextSequence() = ++sequence;
    }
}

enum class TodoState {
    ACTIVE, COMPLETE
}

