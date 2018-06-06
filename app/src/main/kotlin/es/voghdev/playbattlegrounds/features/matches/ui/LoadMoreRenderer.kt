package es.voghdev.playbattlegrounds.features.matches.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pedrogomez.renderers.Renderer
import es.voghdev.playbattlegrounds.R

class LoadMoreRenderer(val listener: OnRowClicked?) : Renderer<LoadMore>() {
    override fun hookListeners(rootView: View?) {
        rootView?.setOnClickListener {
            listener?.onLoadMoreClicked()
        }
    }

    override fun setUpView(rootView: View?) = Unit

    override fun inflate(inflater: LayoutInflater?, parent: ViewGroup?): View {
        return inflater?.inflate(R.layout.row_load_more, parent, false) ?: View(context)
    }

    override fun render() = Unit

    interface OnRowClicked {
        fun onLoadMoreClicked()
    }
}
