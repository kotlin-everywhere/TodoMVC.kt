package com.github.kotline_everywhere.todomvc

import com.github.kotlin_everywhere.react.Ul
import com.github.kotlin_everywhere.react.stateless
import com.github.kotline_everywhere.todomvc.state.Todo
import com.github.kotline_everywhere.todomvc.state.TodoState
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import org.w3c.dom.events.KeyboardEvent

class TodoListProps(val todoList: Array<Todo>)

val TodoList = stateless { state: TodoListProps ->
    Ul({ className = "todo-list" }) {
        +state.todoList.map { todo ->
            li({
                asDynamic()["key"] = todo.pk;
                className = if (todo.editing) "editing" else if (todo.state == TodoState.COMPLETE) "completed" else ""
            }) {
                div({ className = "view" }) {
                    input({
                        className = "toggle"; asDynamic()["type"] = "checkbox";
                        asDynamic()["onChange"] = { e: Event ->
                            val newState =
                                    if ((e.target as HTMLInputElement).checked) TodoState.COMPLETE
                                    else TodoState.ACTIVE
                            todo.setState(newState)
                        }
                        asDynamic()["checked"] = todo.state == TodoState.COMPLETE
                    })
                    label({ asDynamic()["onDoubleClick"] = { todo.setEditing(true) } }) { +todo.text }
                    button({ className = "destroy"; onClick = { todo.delete() } })
                }
                input({
                    className = "edit"; value = todo.text;
                    asDynamic()["onChange"] = { e: Event ->
                        todo.setText((e.target as HTMLInputElement).value)
                    }
                    asDynamic()["onKeyDown"] = { e: KeyboardEvent ->
                        if (e.keyCode == 13) {
                            todo.setEditing(false)
                        }
                    }
                })
            }
        }.toTypedArray()
    }
}