package com.github.kotlin_everywhere.todomvc.remote

val database = mutableListOf<TodoImpl>()

val remote = TodoRemote().apply {
    listTodo {
        TodoListImpl(database.toTypedArray())
    }

    addTodo(EditTodoParamImpl::class.java) { param ->
        database.add(TodoImpl(param.pk, param.text, param.state))
        ResultImpl()
    }

    editTodo(EditTodoParamImpl::class.java) { param ->
        database.filter { it.pk == param.pk }.forEach { it.text = param.text; it.state = param.state }
        ResultImpl()
    }

    deleteTodo(DeleteTodoParamImpl::class.java) { param ->
        database.removeAll(database.filter { it.pk == param.pk })
        ResultImpl()
    }
}

class TodoListImpl(override val list: Array<Todo>) : TodoList
class TodoImpl(override val pk: String, override var text: String, override var state: String) : Todo
class EditTodoParamImpl(override var pk: String, override var text: String, override var state: String) : EditTodoParam
class ResultImpl(override val message: Array<String> = arrayOf()) : Result
class DeleteTodoParamImpl(override var pk: String) : DeleteTodoParam

fun main(args: Array<String>) {
    remote.runServer()
}

