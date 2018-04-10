package com.appandweb.catchapp.domain.executor

interface Executor {
    suspend fun execute(function: () -> Unit)
}
