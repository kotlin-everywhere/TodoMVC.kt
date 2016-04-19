package com.github.kotline_everywhere.todomvc

import com.github.kotlin_everywhere.react.*
import com.github.kotlin_everywhere.react.Component
import com.github.kotlin_everywhere.react.ReactElement
import com.github.kotline_everywhere.todomvc.state.Manager
import com.github.kotline_everywhere.todomvc.state.State
import com.github.kotline_everywhere.todomvc.state.TodoListFilter

interface AppState {
    val state: State


}

fun AppState(state: State): AppState {
    val obj = js("({})")
    obj["state"] = state
    return obj
}

fun classNames(map: Map<String, Boolean>, vararg names: String): String =
        (map.filter { it.value }.keys + names).joinToString(" ")

class App(props: Any?) : Component<Any?, AppState>(props) {
    init {
        Manager.subscribe {
            setState(AppState(Manager.state))
        }
        state = AppState(Manager.state)
    }

    override fun render(): ReactElement? {
        return "section"(attr { className = "todoapp" }) {
            +"header"(attr { className = "header" }) {
                +"h1" { +"todos" }
                +"input"(attr { className = "new-todo"; asDynamic()["placeholder"] = "What needs to be done?"; asDynamic()["autofocus"] = true })
            }

            +"section"(attr { className = "main" }) {
                +"input"(attr { className = "toggle-all"; asDynamic()["type"] = "checkbox" })
                +TodoList(TodoListProps(state.state.filteredTodoList))
            }
            +"footer"(attr { className = "footer" }) {
                +"span"(attr { className = "todo-count" }) {
                    +"strong"{ +"${state.state.activeTodoList.size}" }
                    +" item left"
                }

                +"ul"(attr { className = "filters" }) {
                    +"li" {
                        val className = classNames(mapOf("selected" to (state.state.todoListFilter == TodoListFilter.ALL)))
                        +"a"(attr { this.className = className; asDynamic()["href"] = "#"; onClick = { state.state.setTotoListFilter(TodoListFilter.ALL) } }) { +"All" }
                    }
                    +"li" {
                        val className = classNames(mapOf("selected" to (state.state.todoListFilter == TodoListFilter.ACTIVE)))
                        +"a"(attr { this.className = className; asDynamic()["href"] = "#"; onClick = { state.state.setTotoListFilter(TodoListFilter.ACTIVE) } }) { +"Active" }
                    }
                    +"li" {
                        val className = classNames(mapOf("selected" to (state.state.todoListFilter == TodoListFilter.COMPLETE)))
                        +"a"(attr { this.className = className; asDynamic()["href"] = "#"; onClick = { state.state.setTotoListFilter(TodoListFilter.COMPLETE) } }) { +"Completed" }
                    }
                }


                if (state.state.completeTodoList.isNotEmpty()) {
                    +"button"(attr { className = "clear-completed" }) { +"Clear completed" }
                }
            }
        }
    }

    companion object {
        val factory = Factory(::App)
    }
}


