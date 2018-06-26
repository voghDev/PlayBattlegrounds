package es.voghdev.playbattlegrounds.common.reslocator

interface ResLocator {
    fun getString(resId: Int): String
    fun getString(resId: Int, vararg formatArgs: Any): String
}
