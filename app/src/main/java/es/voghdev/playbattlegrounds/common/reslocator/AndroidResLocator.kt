package es.voghdev.playbattlegrounds.common.reslocator

import android.content.Context

class AndroidResLocator(val context: Context?) : ResLocator {
    override fun getString(resId: Int): String =
            if (context != null) context.getString(resId) else ""

    override fun getString(resId: Int, vararg formatArgs: Any): String =
            if (context != null) context.getString(resId, *formatArgs) else ""
}
