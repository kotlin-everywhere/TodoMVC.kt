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

    private fun filterList(state: TodoState) = list.filter { it.state == state }

    companion object {
        private val todos: Todos
            get() = store.state.todos

        fun addTodo(text: String) {
            if (text.isBlank()) {
                return
            }
            setList(todos.list + Todo(Todo.nextSequence(), text, TodoState.ACTIVE, false))
        }

        private fun setList(list: List<Todo>) {
            store.setTodos(todos.copy(list = list))
        }

        private fun setTodo(todo: Todo, body: Todo.() -> Todo) {
            setList(todos.list.map {
                when (it) {
                    todo -> todo.body()
                    else -> it
                }
            })
        }

        fun completeAllTodos() {
            setList(todos.list.map { it.copy(state = TodoState.COMPLETE) })
        }

        fun setTodoState(todo: Todo, state: TodoState) {
            setTodo(todo) { copy(state = state) }
        }

        fun setTodoEditing(todo: Todo, editing: Boolean) {
            setTodo(todo) { copy(editing = editing) }
        }

        fun deleteTodo(todo: Todo) {
            setList(todos.list.filter { it !== todo })
        }

        fun setTodoText(todo: Todo, text: String) {
            setTodo(todo) { copy(text = text) }
        }


        fun setListFilter(listFilter: TodoListFilter) {
            store.setTodos(todos.copy(listFilter = listFilter))
        }

        fun deleteAllCompleteTodos() {
            setList(todos.list.filter { it.state != TodoState.COMPLETE })
        }
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

