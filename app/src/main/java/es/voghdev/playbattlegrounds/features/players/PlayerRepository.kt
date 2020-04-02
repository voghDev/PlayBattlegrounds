package es.voghdev.playbattlegrounds.features.players

import arrow.core.Either
import es.voghdev.playbattlegrounds.common.AbsError
import es.voghdev.playbattlegrounds.common.Success
import es.voghdev.playbattlegrounds.features.players.model.Content
import es.voghdev.playbattlegrounds.features.players.model.Player
import es.voghdev.playbattlegrounds.features.players.usecase.GetContentById
import es.voghdev.playbattlegrounds.features.players.usecase.GetPlayerById
import es.voghdev.playbattlegrounds.features.players.usecase.GetPlayerByName
import es.voghdev.playbattlegrounds.features.players.usecase.InsertContent
import es.voghdev.playbattlegrounds.features.season.Season
import es.voghdev.playbattlegrounds.features.season.model.PlayerSeasonGameModeStats
import es.voghdev.playbattlegrounds.features.season.model.PlayerSeasonInfo
import es.voghdev.playbattlegrounds.features.season.usecase.GetPlayerSeasonInfo

class PlayerRepository(
    val getPlayerByIdApiDataSource: GetPlayerById,
    val getPlayerByNameApiDataSource: GetPlayerByName,
    val tooManyRequestsError: String,
    val getPlayerSeasonInfo: GetPlayerSeasonInfo,
    val getContentByIdDataSource: GetContentById,
    val getContentByIdDBDataSource: GetContentById,
    val insertContentDBDataSource: InsertContent
) :
    GetPlayerByName,
    GetPlayerById by getPlayerByIdApiDataSource,
    GetContentById,
    InsertContent by insertContentDBDataSource {

    var start = 0L
    var seasonInfoClock = 0L
    var seasonInfoMap = mutableMapOf<String, PlayerSeasonInfo>()
    var contents = mutableMapOf<Long, Content>()

    override fun getPlayerByName(name: String, region: String): Either<AbsError, Player> {
        val result = getPlayerByNameApiDataSource.getPlayerByName(name, region)

        return if (userCanRequest(System.currentTimeMillis())) result
        else Either.left(AbsError(tooManyRequestsError))
    }

    fun userCanRequest(ms: Long): Boolean {
        /*
        * Bluehole has announced the traffic limit in the /matches request has been removed,
        * So no more throttling, Yeehaa!
        * */
        return true
    }

    fun getPlayerSeasonInfo(player: Player, season: Season, ms: Long): Either<AbsError, PlayerSeasonInfo> {
        if (ms > seasonInfoClock.plus(10000))
            seasonInfoMap.clear()

        return if (!seasonInfoMap.containsKey(player.name)) {
            val result = getPlayerSeasonInfo.getPlayerSeasonInfo(player, season)
            seasonInfoMap[player.name] = (result as? Success)?.b ?: emptyPlayerSeasonInfo()
            seasonInfoClock = ms
            result
        } else {
            Either.right(seasonInfoMap[player.name] ?: emptyPlayerSeasonInfo())
        }
    }

    override fun getContentById(id: Long): Either<AbsError, Content> {
        if (contents.containsKey(id))
            return Either.right(contents[id] ?: Content())

        val dbResult = getContentByIdDBDataSource.getContentById(id)

        if (dbResult is Success) {
            contents[dbResult.b.id] = dbResult.b

            return dbResult
        }

        val apiResult = getContentByIdDataSource.getContentById(id)

        if (apiResult is Success) {
            contents[apiResult.b.id] = apiResult.b
        }

        return apiResult
    }

    private fun emptyPlayerSeasonInfo() = PlayerSeasonInfo(
        PlayerSeasonGameModeStats(),
        PlayerSeasonGameModeStats(),
        PlayerSeasonGameModeStats(),
        PlayerSeasonGameModeStats(),
        PlayerSeasonGameModeStats(),
        PlayerSeasonGameModeStats()
    )
}
