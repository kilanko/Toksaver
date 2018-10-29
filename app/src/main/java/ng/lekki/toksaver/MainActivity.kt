package ng.lekki.toksaver

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.view.Menu
import android.view.MenuItem
import com.breuhteam.apprate.AppRate
import com.facebook.ads.Ad
import com.facebook.ads.AdError
import com.facebook.ads.InterstitialAd
import com.facebook.ads.InterstitialAdListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.frag_solid.view.*
import ng.lekki.toksaver.Boxes.Downloads
import ng.lekki.toksaver.Boxes.Home
import ng.lekki.toksaver.Toolbox.TokService

class MainActivity : AppCompatActivity() {

    val videoUrl = "VfNNh4uBFh8"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val startKit = Intent(this@MainActivity, TokService::class.java)
        ContextCompat.startForegroundService(this@MainActivity,startKit)


        AppRate.app_launched(this@MainActivity, packageName,0,4)

        setSupportActionBar(tolus)



        btn_open.setOnClickListener {

            phoneTweet("com.zhiliaoapp.musically")

        }


        btn_how.setOnClickListener {

            playVideo(videoUrl)

        }






    }




    private fun phoneTweet(packageN: String) {
        val apppackage = packageN
        try {
            val i = packageManager.getLaunchIntentForPackage(apppackage)
            startActivity(i)
        } catch (e: Exception) {
           startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageN)))
        }

    }




    fun playVideo(id: String) {
        val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id))
        val webIntent = Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id))
        try {
            startActivity(appIntent)
        } catch (ex: ActivityNotFoundException) {
            startActivity(webIntent)
        }

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolx,menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when(item!!.itemId){

            R.id.menu_saves ->{


                startActivity(Intent(this@MainActivity,Downloaded::class.java))
            }


            R.id.menu_rate ->{

                AppRate.app_launched(this, packageName)


            }

        }

        return super.onOptionsItemSelected(item)
    }







}
