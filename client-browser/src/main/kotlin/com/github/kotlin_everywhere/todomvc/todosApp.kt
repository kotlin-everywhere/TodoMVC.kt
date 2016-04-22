package com.github.kotlin_everywhere.todomvc
import com.github.kotlin_everywhere.react.Section
import com.github.kotlin_everywhere.react.SyntheticKeyboardEvent
import com.github.kotlin_everywhere.react.classNames
import com.github.kotlin_everywhere.react.stateless
import com.github.kotlin_everywhere.todomvc.store.TodoListFilter
import com.github.kotlin_everywhere.todomvc.store.TodoState
import com.github.kotlin_everywhere.todomvc.store.store
import org.w3c.dom.HTMLInputElement

val TodosApp = stateless(store) { state ->
    val todos = state.todos;

    Section({ className = "todoapp" }) {
        header({ className = "header" }) {
            h1 { +"todos" }
            input({
                className = "new-todo"; placeholder = "What needs to be done?"; autoFocus = true;
                onKeyDown = { e: SyntheticKeyboardEvent ->
                    if (e.keyCode == 13) {
                        val element = e.target as HTMLInputElement
                        todos.addTodo(element.value)
                        element.value = ""
                    }
                }
            })
        }

        if (todos.list.isNotEmpty()) {
            section({ className = "main" }) {
                input({
                    className = "toggle-all"; type = "checkbox";
                    checked = todos.list.isNotEmpty() && todos.activeList.isEmpty()
                    onChange = { todos.completeAllTodos() }
                })
                ul({ className = "todo-list" }) {
                    +todos.filteredList.map { todo ->
                        li({
                            key = todo.pk;
                            className = if (todo.editing) "editing" else if (todo.state == TodoState.COMPLETE) "completed" else ""
                        }) {
                            div({ className = "view" }) {
                                input({
                                    className = "toggle"; type = "checkbox";
                                    checked = todo.state == TodoState.COMPLETE
                                    onChange = { e ->
                                        todos.setTodoState(
                                                todo,
                                                if ((e.target as HTMLInputElement).checked) TodoState.COMPLETE else TodoState.ACTIVE
                                        )
                                    }
                                })
                                label({ onDoubleClick = { todos.setTodoEditing(todo, true) } }) { +todo.text }
                                button({ className = "destroy"; onClick = { todos.deleteTodo(todo) } })
                            }
                            input({
                                className = "edit"; value = todo.text;
                                onChange = { todos.setTodoText(todo, (it.target as HTMLInputElement).value) }
                                onKeyDown = {
                                    if (it.keyCode == 13) {
                                        todos.setTodoEditing(todo, false)
                                    }
                                }
                            })
                        }
                    }
                }
            }
            footer({ className = "footer" }) {
                span({ className = "todo-count" }) {
                    strong { +"${todos.activeList.size}" }
                    +" item left"
                }

                ul({ className = "filters" }) {
                    li {
                        val className = classNames("selected" to (todos.listFilter == TodoListFilter.ALL))
                        a({ this.className = className; href = "#"; onClick = { todos.setListFilter(TodoListFilter.ALL) } }) { +"All" }
                    }
                    li {
                        val className = classNames("selected" to (todos.listFilter == TodoListFilter.ACTIVE))
                        a({ this.className = className; href = "#"; onClick = { todos.setListFilter(TodoListFilter.ACTIVE) } }) { +"Active" }
                    }
                    li {
                        val className = classNames("selected" to (todos.listFilter == TodoListFilter.COMPLETE))
                        a({ this.className = className; href = "#"; onClick = { todos.setListFilter(TodoListFilter.COMPLETE) } }) { +"Completed" }
                    }
                }


                if (todos.completeList.isNotEmpty()) {
                    button({ className = "clear-completed"; onClick = { todos.deleteAllCompleteTodos() } }) { +"Clear completed" }
                }
            }
        }
    }
}
