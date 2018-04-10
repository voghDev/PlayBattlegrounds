package com.appandweb.catchapp.domain.executor

class DefaultExecutor : Executor {
    override suspend fun execute(function: () -> Unit) {
        function()
    }
}