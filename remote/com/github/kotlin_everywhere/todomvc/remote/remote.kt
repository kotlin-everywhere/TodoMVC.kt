package com.github.kotlin_everywhere.todomvc.remote

import com.github.kotlin_everywhere.rpc.*

class TodoRemote : Remote() {
    val listTodo = get<TodoList>("/todo/")
    val editTodo = put<Result>("/todo").with<EditTodoParam>()
    val addTodo = post<Result>("/todo").with<EditTodoParam>()
    val deleteTodo = delete<Result>("/todo").with<DeleteTodoParam>()
}

interface Result {
    val message: Array<String>
}

interface Todo {
    val pk: String
    val text: String
    val state: String
}

interface EditTodoParam {
    var pk: String
    var text: String
    var state: String
}

interface TodoList {
    val list: Array<Todo>
}

interface DeleteTodoParam {
    var pk: String
}
