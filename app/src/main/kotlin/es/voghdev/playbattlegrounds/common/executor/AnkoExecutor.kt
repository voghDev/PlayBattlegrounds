package com.appandweb.catchapp.domain.executor

import org.jetbrains.anko.doAsync

class AnkoExecutor : Executor {
    override suspend fun execute(function: () -> Unit) {
        doAsync {
            function()
        }
    }
}
