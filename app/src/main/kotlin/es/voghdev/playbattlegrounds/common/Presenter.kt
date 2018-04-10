/*
 * Copyright (C) 2018 Olmo Gallegos Hernández.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.appandweb.weevento.ui.presenter

import com.appandweb.catchapp.domain.executor.AnkoExecutor
import com.appandweb.catchapp.domain.executor.Executor

abstract class Presenter<T1, T2>() {
    open suspend fun initialize() { /* Empty */ }

    open suspend fun resume() { /* Empty */ }

    open suspend fun pause() { /* Empty */ }

    open suspend fun destroy() {
        view = null
        navigator = null
    }

    suspend fun execute(function: () -> Unit) {
        executor?.execute(function)
    }

    var executor: Executor? = AnkoExecutor()

    var view: T1? = null
    var navigator: T2? = null
}