package com.github.kotline_everywhere.todomvc

import com.github.kotlin_everywhere.react.ReactDOM
import kotlin.browser.document


fun main(args: Array<String>) {
    ReactDOM.render(TodosApp(), document.getElementById("app")!!)
}

