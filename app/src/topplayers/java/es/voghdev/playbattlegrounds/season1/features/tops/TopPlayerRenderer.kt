package es.voghdev.playbattlegrounds.season1.features.tops

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.pedrogomez.renderers.Renderer
import com.squareup.picasso.Picasso
import es.voghdev.playbattlegrounds.R
import es.voghdev.playbattlegrounds.season1.features.tops.model.TopPlayer
import java.io.File

class TopPlayerRenderer(val listener: OnRowClicked?) : Renderer<TopPlayer>() {
    var tv_title: TextView? = null
    var tv_bio: TextView? = null
    var iv_image: ImageView? = null
    var iv_flag: ImageView? = null

    override fun hookListeners(rootView: View?) {
        rootView?.setOnClickListener {
            listener?.onTopPlayerClicked(content)
        }
    }

    override fun setUpView(rootView: View?) {
        tv_title = rootView?.findViewById(R.id.tv_title)
        tv_bio = rootView?.findViewById(R.id.tv_bio)
        iv_image = rootView?.findViewById(R.id.iv_image)
        iv_flag = rootView?.findViewById(R.id.iv_flag)
    }

    override fun inflate(inflater: LayoutInflater?, parent: ViewGroup?): View =
        inflater?.inflate(R.layout.row_top_player, parent, false) ?: View(context)

    override fun render() {
        tv_title?.text = content.player.name
        tv_bio?.text = content.bio

        renderPicture()
        renderFlag()
    }

    private fun renderFlag() {
        val f = File(context.getExternalFilesDir("img"), "${content.country}.png")
        if (f.exists()) {
            Picasso.with(context)
                .load(f)
                .into(iv_flag)
        } else {
            Picasso.with(context)
                .load(content.getFlagRemoteUrl())
                .into(iv_flag)
        }
    }

    private fun renderPicture() {
        val f = File(context.getExternalFilesDir("img"), content.picture)
        if (f.exists()) {
            Picasso.with(context)
                .load(f)
                .into(iv_image)
        } else {
            Picasso.with(context)
                .load(content.getRemoteUrl())
                .into(iv_image)
        }
    }

    interface OnRowClicked {
        fun onTopPlayerClicked(player: TopPlayer)
    }
}