package es.voghdev.playbattlegrounds.season1.features.tops.renderer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import com.pedrogomez.renderers.Renderer
import es.voghdev.playbattlegrounds.R
import es.voghdev.playbattlegrounds.season1.features.tops.model.Whitespace

class WhitespaceRenderer() : Renderer<Whitespace>() {
    var v_whitespace: View? = null

    override fun inflate(inflater: LayoutInflater?, parent: ViewGroup?): View =
        inflater?.inflate(R.layout.row_whitespace, parent, false) ?: View(context)

    override fun hookListeners(rootView: View?) = Unit

    override fun render() {
        val lp = FrameLayout.LayoutParams(WRAP_CONTENT, content.height)
        v_whitespace?.layoutParams = lp
    }

    override fun setUpView(rootView: View?) {
        v_whitespace = rootView
    }
}