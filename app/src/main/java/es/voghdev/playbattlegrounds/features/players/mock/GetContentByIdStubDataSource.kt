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
package es.voghdev.playbattlegrounds.features.players.mock

import android.content.Context
import arrow.core.Either
import es.voghdev.playbattlegrounds.R
import es.voghdev.playbattlegrounds.common.AbsError
import es.voghdev.playbattlegrounds.features.players.model.Content
import es.voghdev.playbattlegrounds.features.players.usecase.GetContentById

class GetContentByIdStubDataSource(private val appContext: Context) : GetContentById {
    override fun getContentById(id: Long): Either<AbsError, Content> {
        val links = appContext
            .resources
            .getStringArray(R.array.sample_links)
            .map { it }

        val buttonTexts = appContext
            .resources
            .getStringArray(R.array.sample_button_texts)
            .map { it }

        val titles = appContext
            .resources
            .getStringArray(R.array.sample_titles)
            .map { it }

        val content = appContext
            .resources
            .getStringArray(R.array.sample_contents)
            .mapIndexed { i, it ->
                Content(
                    i.toLong(),
                    titles[i],
                    it,
                    buttonText = buttonTexts[i],
                    link = links[i])
            }.firstOrNull { it.id == id }

        return if (content != null) Either.right(content)
        else Either.left(AbsError("Content not found"))
    }
}
