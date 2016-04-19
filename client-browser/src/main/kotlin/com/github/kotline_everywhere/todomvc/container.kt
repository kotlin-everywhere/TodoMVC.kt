package com.github.kotline_everywhere.todomvc

import com.github.kotlin_everywhere.react.Component
import com.github.kotlin_everywhere.react.Div
import com.github.kotlin_everywhere.react.ReactElement
import com.github.kotlin_everywhere.react.stateless

class App(props: Any?) : Component<Any?, State>(props) {
    init {
        Manager.subscribe {
            setState(Manager.state)
        }
        state = Manager.state
    }

    override fun render(): ReactElement? {
        return Div {
        }
    }
}

val TodoList = stateless { list: Array<Todo> ->
    Div {
    }
}
