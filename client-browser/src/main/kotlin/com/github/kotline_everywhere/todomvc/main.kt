package com.github.kotline_everywhere.todomvc

import com.github.kotlin_everywhere.react.ReactDOM
import com.github.kotline_everywhere.todomvc.state.Manager
import com.github.kotline_everywhere.todomvc.state.Todo
import com.github.kotline_everywhere.todomvc.state.TodoState
import kotlin.browser.document


fun main(args: Array<String>) {
    val reactElement = App.factory(null)
    ReactDOM.render(reactElement, document.getElementById("app")!!)
    Manager.addTodo(Todo(Todo.nextSequence(), "Taste JavaScript", TodoState.COMPLETE, editing = false))
    Manager.addTodo(Todo(Todo.nextSequence(), "Buy a unicorn", TodoState.ACTIVE, editing = false))
}

