package es.voghdev.playbattlegrounds.features.players.db

import arrow.core.Either
import com.raizlabs.android.dbflow.sql.language.SQLite
import es.voghdev.playbattlegrounds.common.AbsError
import es.voghdev.playbattlegrounds.features.players.model.Content
import es.voghdev.playbattlegrounds.features.players.usecase.GetContentById

class GetContentByIdDBDataSource : GetContentById {
    override fun getContentById(id: Long): Either<AbsError, Content> {
        val content: Content? = SQLite.select()
            .from(ContentDBEntry::class.java)
            .where(ContentDBEntry_Table.id.eq(id))
            .querySingle()?.toDomain()

        return if (content is Content) Either.right(content)
        else Either.left(AbsError("Content $id not found"))
    }
}
