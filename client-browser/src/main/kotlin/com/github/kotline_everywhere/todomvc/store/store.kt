package com.github.kotline_everywhere.todomvc.store

import com.github.kotlin_everywhere.react.Store

data class State(val todos: Todos)

object store : Store<State>(State(Todos(listOf(), TodoListFilter.ALL))) {
    fun setTodos(todos: Todos) {
        state = state.copy(todos = todos)
    }
}

