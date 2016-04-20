package com.github.kotline_everywhere.todomvc

import com.github.kotlin_everywhere.react.*
import com.github.kotlin_everywhere.react.Component
import com.github.kotlin_everywhere.react.ReactElement
import com.github.kotlin_everywhere.react.SyntheticKeyboardEvent
import com.github.kotline_everywhere.todomvc.state.Manager
import com.github.kotline_everywhere.todomvc.state.State
import com.github.kotline_everywhere.todomvc.state.TodoListFilter
import org.w3c.dom.HTMLInputElement

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
        return Section({ className = "todoapp" }) {
            +TodoHeader(TodoHeaderProps(state = state.state))

            if (state.state.todoList.isNotEmpty()) {
                section({ className = "main" }) {
                    input({
                        className = "toggle-all"; type = "checkbox";
                        checked = state.state.todoList.isNotEmpty() && state.state.activeTodoList.isEmpty()
                        onChange = { state.state.completeAllTodo() }
                    })
                    +TodoList(TodoListProps(state.state.filteredTodoList))
                }
                footer({ className = "footer" }) {
                    span({ className = "todo-count" }) {
                        strong { +"${state.state.activeTodoList.size}" }
                        +" item left"
                    }

                    ul({ className = "filters" }) {
                        li {
                            val className = classNames(mapOf("selected" to (state.state.todoListFilter == TodoListFilter.ALL)))
                            a({ this.className = className; href = "#"; onClick = { state.state.setTotoListFilter(TodoListFilter.ALL) } }) { +"All" }
                        }
                        li {
                            val className = classNames(mapOf("selected" to (state.state.todoListFilter == TodoListFilter.ACTIVE)))
                            a({ this.className = className; asDynamic()["href"] = "#"; onClick = { state.state.setTotoListFilter(TodoListFilter.ACTIVE) } }) { +"Active" }
                        }
                        li {
                            val className = classNames(mapOf("selected" to (state.state.todoListFilter == TodoListFilter.COMPLETE)))
                            a({ this.className = className; asDynamic()["href"] = "#"; onClick = { state.state.setTotoListFilter(TodoListFilter.COMPLETE) } }) { +"Completed" }
                        }
                    }


                    if (state.state.completeTodoList.isNotEmpty()) {
                        button({ className = "clear-completed"; onClick = { state.state.removeCompleteTodo() } }) { +"Clear completed" }
                    }
                }
            }
        }
    }

    companion object {
        val factory = Factory(::App)
    }
}


data class TodoHeaderProps(val state: State)

val TodoHeader = stateless { props: TodoHeaderProps ->
    Header({ className = "header" }) {
        h1 { +"todos" }
        input({
            className = "new-todo"; asDynamic()["placeholder"] = "What needs to be done?"; autoFocus = true;
            onKeyDown = { e: SyntheticKeyboardEvent ->
                if (e.keyCode == 13) {
                    val element = e.target as HTMLInputElement
                    props.state.addTodo(element.value)
                    element.value = ""
                }
            }
        })
    }
}
