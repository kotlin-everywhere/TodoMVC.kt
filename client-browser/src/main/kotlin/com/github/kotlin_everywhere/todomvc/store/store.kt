package com.github.kotlin_everywhere.todomvc.store

import com.github.kotlin_everywhere.react.Store
import com.github.kotlin_everywhere.todomvc.remote.DeleteTodoParam
import com.github.kotlin_everywhere.todomvc.remote.EditTodoParam
import com.github.kotlin_everywhere.todomvc.remote.TodoRemote

data class State(val todos: Todos)

fun <T> jsObject(init: T.() -> Unit): T {
    val obj: T = js("({})")
    obj.init()
    return obj
}

object store : Store<State>(State(Todos(listOf(), TodoListFilter.ALL))) {
    val remote = TodoRemote().apply {
        this.baseUri = "http://localhost:8080"
    }

    init {
        remote.listTodo.fetch().then { response ->
            val list = response.list.map { Todo(it.pk, it.text, TodoState.valueOf(it.state), false) }
            state = state.copy(todos = state.todos.copy(list = list))
        }
    }


    fun setTodos(todos: Todos) {
        val previousList = state.todos.list
        state = state.copy(todos = todos)

        if (previousList != state.todos.list) {
            state.todos.list
                    .filter { todo ->
                        previousList.find { todo.pk == it.pk } == null
                    }
                    .forEach {
                        remote.addTodo.fetch(jsObject<EditTodoParam> {
                            pk = it.pk; text = it.text; state = it.state.name
                        })
                    }

            state.todos.list
                    .filter { todo ->
                        previousList.find { it.pk == todo.pk && it != todo } != null
                    }
                    .forEach {
                        remote.editTodo.fetch(jsObject<EditTodoParam> {
                            pk = it.pk; text = it.text; state = it.state.name
                        })
                    }

            previousList
                    .filter { todo ->
                        state.todos.list.find { it.pk == todo.pk } == null
                    }
                    .forEach {
                        remote.deleteTodo.fetch(jsObject<DeleteTodoParam> { pk = it.pk })
                    }
        }
    }
}

