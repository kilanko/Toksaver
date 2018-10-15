package ng.lekki.toksaver

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.view.MenuItem
import com.facebook.ads.Ad
import com.facebook.ads.AdError
import com.facebook.ads.InterstitialAd
import com.facebook.ads.InterstitialAdListener
import kotlinx.android.synthetic.main.activity_main.*
import ng.lekki.toksaver.Boxes.Downloads
import ng.lekki.toksaver.Boxes.Home
import ng.lekki.toksaver.Toolbox.TokService

class MainActivity : AppCompatActivity() {
    var faceADS: InterstitialAd? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        prepareViewPager(viewpager)
        sects.setupWithViewPager(viewpager)









    }




     fun prepareViewPager(viewPager: ViewPager) {
         val how = getString(R.string.howtos)
         val saver = getString(R.string.savers)
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(Home(), how)
        adapter.addFragment(Downloads(), saver)
        viewPager.adapter = adapter
    }
}
