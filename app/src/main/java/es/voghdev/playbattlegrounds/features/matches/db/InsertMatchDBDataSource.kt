package es.voghdev.playbattlegrounds.features.matches.db

import com.raizlabs.android.dbflow.config.FlowManager
import es.voghdev.playbattlegrounds.common.db.AppDatabase
import es.voghdev.playbattlegrounds.features.matches.Match
import es.voghdev.playbattlegrounds.features.matches.usecase.InsertMatch

class InsertMatchDBDataSource : InsertMatch {
    override fun insertMatch(match: Match) {
        insertParticipants(match)

        MatchDBEntry(match).save()
    }

    private fun insertParticipants(match: Match) {
        if (match.participants.isNotEmpty())
            FlowManager.getDatabase(AppDatabase.javaClass)
                    .beginTransactionAsync {
                        match.participants.forEach {
                            it.matchId = match.id
                            MatchParticipantDBEntry(it).save()
                        }
                    }
    }
}
