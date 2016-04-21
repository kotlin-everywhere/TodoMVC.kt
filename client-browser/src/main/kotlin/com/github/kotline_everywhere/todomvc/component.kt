package com.github.kotline_everywhere.todomvc

import com.github.kotlin_everywhere.react.Ul
import com.github.kotlin_everywhere.react.stateless
import com.github.kotline_everywhere.todomvc.state.Todo
import com.github.kotline_everywhere.todomvc.state.TodoState
import org.w3c.dom.HTMLInputElement

class TodoListProps(val todoList: Array<Todo>)

val TodoList = stateless { state: TodoListProps ->
    Ul({ className = "todo-list" }) {
        +state.todoList.map { todo ->
            li({
                key = todo.pk;
                className = if (todo.editing) "editing" else if (todo.state == TodoState.COMPLETE) "completed" else ""
            }) {
                div({ className = "view" }) {
                    input({
                        className = "toggle"; type = "checkbox";
                        onChange = { e ->
                            todo.setState(
                                    if ((e.target as HTMLInputElement).checked) TodoState.COMPLETE
                                    else TodoState.ACTIVE
                            )
                        }
                        checked = todo.state == TodoState.COMPLETE
                    })
                    label({ onDoubleClick = { todo.setEditing(true) } }) { +todo.text }
                    button({ className = "destroy"; onClick = { todo.delete() } })
                }
                input({
                    className = "edit"; value = todo.text;
                    onChange = { todo.setText((it.target as HTMLInputElement).value) }
                    onKeyDown = {
                        if (it.keyCode == 13) {
                            todo.setEditing(false)
                        }
                    }
                })
            }
        }
    }
}