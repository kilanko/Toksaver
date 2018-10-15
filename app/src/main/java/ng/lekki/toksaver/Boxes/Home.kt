package ng.lekki.toksaver.Boxes


import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.VideoView
import kotlinx.android.synthetic.main.frag_solid.view.*
import kotlinx.android.synthetic.main.jelly.view.*
import ng.lekki.toksaver.R


class Home : Fragment() {
    var videoview: VideoView? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val design = inflater.inflate(R.layout.frag_solid, container, false)

        videoview = design.videoView


        videoview!!.setOnPreparedListener(object : MediaPlayer.OnPreparedListener{
            override fun onPrepared(mp: MediaPlayer?) {

                mp!!.setVolume(0f,0f)
                mp.isLooping = true
                mp.start()


            }


        })

        val uri = Uri.parse("android.resource://${activity!!.packageName}/" + R.raw.maji)
        videoview!!.setVideoURI(uri)






        design.opens.setOnClickListener {

            phoneTweet(activity!!.applicationContext,"com.zhiliaoapp.musically")

        }






        return design
    }




    private fun phoneTweet(context: Context, packageN: String) {
        val apppackage = packageN
        try {
            val i = context.packageManager.getLaunchIntentForPackage(apppackage)
            context.startActivity(i)
            activity!!.finish()
        } catch (e: Exception) {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageN)))
        }

    }





}
