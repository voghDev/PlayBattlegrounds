package es.voghdev.playbattlegrounds.features.matches.db

import arrow.core.Either
import com.raizlabs.android.dbflow.sql.language.SQLite
import es.voghdev.playbattlegrounds.common.AbsError
import es.voghdev.playbattlegrounds.features.matches.Match
import es.voghdev.playbattlegrounds.features.matches.usecase.GetMatchById

class GetMatchByIdDBDataSource : GetMatchById {
    override fun getMatchById(id: String): Either<AbsError, Match> {
        val participants = SQLite.select()
            .from(MatchParticipantDBEntry::class.java)
            .where(MatchParticipantDBEntry_Table.matchId.eq(id))
            .queryList()
            .map { it.toDomain() }

        val match = SQLite.select()
            .from(MatchDBEntry::class.java)
            .where(MatchDBEntry_Table.id.eq(id))
            .queryList()
            .firstOrNull()?.toDomain()

        match?.participants = participants

        return if (match is Match) Either.right(match)
        else Either.left(AbsError("Match $id not found"))
    }
}