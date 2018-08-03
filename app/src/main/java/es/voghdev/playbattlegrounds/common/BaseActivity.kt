/*
 * Copyright (C) 2017 Olmo Gallegos Hernández.
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
package es.voghdev.playbattlegrounds.common

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem

abstract class BaseActivity : AppCompatActivity() {
    val NONE = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(getLayoutId())
    }

    abstract fun getLayoutId(): Int

    open fun getToolbarTitle(): String {
        return ""
    }

    open fun getToolbarBackgroundColor(): Int {
        return NONE
    }

    open fun getToolbarTitleColor(): Int {
        return NONE
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            onToolbarButtonClicked()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    open fun onToolbarButtonClicked() {
        finish() // Default behaviour. Override it if you want
    }
}
