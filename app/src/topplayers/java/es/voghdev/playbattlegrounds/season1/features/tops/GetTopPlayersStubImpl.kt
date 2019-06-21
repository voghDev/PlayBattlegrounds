package es.voghdev.playbattlegrounds.season1.features.tops

import arrow.core.Either
import es.voghdev.playbattlegrounds.common.AbsError
import es.voghdev.playbattlegrounds.features.players.model.Player
import es.voghdev.playbattlegrounds.season1.features.tops.model.TopPlayer

class GetTopPlayersStubImpl : GetTopPlayers {
    override fun getTopPlayers(): Either<AbsError, List<TopPlayer>> = Either.right(europeanPlayers())

    fun europeanPlayers() = listOf(
        TopPlayer(Player(id = "account.5088aaf9ab774816bdd390ad91c88863", name = "Winghaven")),
        TopPlayer(Player(id = "account.4cc004a9ac15428a9147bd9e0ac4d32e", name = "Carranco")),
        TopPlayer(Player(id = "account.76c8309d8b5b4f89aa3e1a4f80be0ab6", name = "ByRubi9")),
        TopPlayer(Player(id = "account.be4f1065f22f4560ac669af137caecf1", name = "Galgo96ESP")),
        TopPlayer(Player(id = "account.59e4ce452ac94e27b02a37ac7a301135", name = "AndyPyro"))
    ).apply {
        forEach { topPlayer ->
            topPlayer.picture = "${topPlayer.player.id}.jpg"
        }
    }

    fun americanPlayers() = listOf(
        TopPlayer(Player(id = "account.d50fdc18fcad49c691d38466bed6f8fd", name = "shroud")),
        TopPlayer(Player(id = "account.c9c08c8befe54788ad28f86ad5f03374", name = "just9n")),
        TopPlayer(Player(id = "account.74027ebd535345b8be87ee2bcbf5c30b", name = "Chadd")),
        TopPlayer(Player(id = "account.15cbf322a9bc45e88b0cd9f12ef4188e", name = "chocoTaco"))
    )
}
