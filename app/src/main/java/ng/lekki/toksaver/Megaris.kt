package ng.lekki.toksaver

import android.Manifest
import android.app.DownloadManager
import android.app.DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaScannerConnection
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.Toast
import com.downloader.*
import com.esafirm.rxdownloader.RxDownloader
import com.facebook.ads.Ad
import com.facebook.ads.AdError
import com.facebook.ads.InterstitialAd
import com.facebook.ads.InterstitialAdListener
import com.pedro.library.AutoPermissions
import com.pedro.library.AutoPermissionsListener
import io.reactivex.Observer
import kotlinx.android.synthetic.main.jonah.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import org.json.JSONObject
import org.jsoup.Jsoup
import java.io.File
import java.util.regex.Pattern
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

class Megaris : AppCompatActivity(), AutoPermissionsListener {

    var nicky = ""
    var museLink = ""
    var trueLink:ArrayList<String>? = null
    var fbAds: InterstitialAd? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.megaris)

        val pr = PRDownloaderConfig.newBuilder()
                .setDatabaseEnabled(true)
                .build()
        PRDownloader.initialize(getApplicationContext(), pr)


        val intents = intent
        museLink = intents.getStringExtra(Intent.EXTRA_TEXT)
        trueLink = getURLS(museLink!!)

        if (checkPermission()) {

            saveVideo(trueLink!![0])

        }else{

            AutoPermissions.loadActivityPermissions(this@Megaris, 1)


        }


        close.setOnClickListener {

            finishAndRemoveTask()
        }



        fbAds = InterstitialAd(this@Megaris,getString(R.string.fb_ads_inter))



        fbAds!!.setAdListener(object : InterstitialAdListener {
            override fun onLoggingImpression(p0: Ad?) {


            }

            override fun onAdLoaded(p0: Ad?) {

                fbAds!!.show()

            }

            override fun onError(p0: Ad?, p1: AdError?) {


            }

            override fun onInterstitialDismissed(p0: Ad?) {


            }

            override fun onAdClicked(p0: Ad?) {


            }

            override fun onInterstitialDisplayed(p0: Ad?) {


            }


        })



        // Load ads into Interstitial Ads
        fbAds!!.loadAd()
    }



    private fun checkPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        return result == PackageManager.PERMISSION_GRANTED
    }




    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        AutoPermissions.parsePermissions(this@Megaris, requestCode, permissions, this)
    }



    override fun onDenied(requestCode: Int, permissions: Array<String>) {


    }

    override fun onGranted(requestCode: Int, permissions: Array<String>) {

        saveVideo(trueLink!![0])

    }



    //extract URLS

    fun getURLS(value: String): ArrayList<String> {
        val urls = ArrayList<String>()
        val feggy = "\\(?\\b(https?://|www[.]|ftp://)[-A-Za-z0-9+&@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&@#/%=~_()|]"

        val patty = Pattern.compile(feggy)
        val matty = patty.matcher(value)

        while (matty.find()) {
            var urlStrings = matty.group()

            if (urlStrings.startsWith("(") && urlStrings.endsWith(")")) {
                urlStrings = urlStrings.substring(1, urlStrings.length - 1)
            }

            urls.add(urlStrings)
        }

        return urls
    }



    //tik proccessor




    fun saveVideo(muse:String) {
        var playURL = ""
        var nameURL = ""
        var carry = ""
        launch(UI) {
            val result = async(CommonPool) {

                try {


                    val docs = Jsoup.connect(muse)
                            .userAgent("Mozilla/5.0 (Linux; U; Android 4.0.2; en-us; Galaxy Nexus Build/ICL53F) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30")
                            .get()

                    val elem = docs.select("script");

                    for(elements in elem){

                        val dope = elements.data()
                        if (dope.contains("var data = ")){

                            val initial = dope.substring(dope.lastIndexOf("var data =")).replace("var data =","")
                            val finals = initial.substring(0, initial.indexOf("};")) +"};"



                            val json = JSONObject(finals)
                            nicky = json.getJSONObject("author").getString("nickname")
                            val songName = json.getJSONObject("music").getString("title")
                            val looper  = json.getJSONObject("video").getJSONObject("play_addr").getJSONArray("url_list")
                            for (i in 0..looper.length() -1){

                                playURL = looper.getString(0)
                            }

                            nameURL = songName

                        }

                    }



                } catch (e: Exception) {

                    carry = e.message!!


                }



            }.await()

            downloader("https:$playURL",nameURL,nicky)

        }
    }







    fun keep(downloadURL:String, videoName:String,nickNames:String){
        val lastURL ="$downloadURL"
        label.text = nickNames
        titled.text = "♫ $videoName"
        val dirPath = createDirectoryAndSaveFile()
        val fileDesc = nickNames + "-" +videoName
        val end = ".mp4"
        val patornking = "$fileDesc" + end

        val filepath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath + "/toksave/$patornking"
        val look = arrayOf(filepath)


        val downloadId = PRDownloader.download(lastURL, dirPath, patornking)
                .build()
                .setOnProgressListener(object : OnProgressListener {
                    override fun onProgress(progress: Progress) {
                        val progressPercent = progress.currentBytes * 100 / progress.totalBytes
                        val total = progressPercent.toInt()
                        progress_status.text = "$total%"
                        progressBar.isIndeterminate = false
                        progressBar.progress = total

                    }
                })
                .setOnStartOrResumeListener(object: OnStartOrResumeListener {
                    override fun onStartOrResume() {

                        showControls()
                    }

                })
                .start(object : OnDownloadListener {
                    override fun onError(error: Error?) {


                    }


                    override fun onDownloadComplete() {

                        play_btn.visibility = View.VISIBLE
                        progress_status.text = getString(R.string.fini)
                        progressBar.visibility =View.GONE

                        MediaScannerConnection.scanFile(this@Megaris, look, null, object : MediaScannerConnection.OnScanCompletedListener {
                            override fun onScanCompleted(p0: String?, mediaURI: Uri?) {


                                runOnUiThread {


                                }




                                play_btn.setOnClickListener {

                                    val intent = Intent(Intent.ACTION_VIEW, mediaURI)
                                    intent.setDataAndType(mediaURI, "video/mp4")
                                    startActivity(intent)
                                }


                            }


                        })

                    }

                })

    }


    fun createDirectoryAndSaveFile():String {

        val direct = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath + "/toksave/")

        if (!direct.exists()) {
            val wallpaperDirectory = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath + "/toksave/")
            wallpaperDirectory.mkdirs()
        }
        return  direct.absolutePath
    }


    //show controls
    fun showControls(){
        covrIcon.visibility = View.VISIBLE
        progress_status.visibility  = View.VISIBLE
        titled.visibility = View.VISIBLE
        progression.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
        progress_status.visibility = View.VISIBLE
        close.visibility = View.VISIBLE

    }






    fun downloader(downloadURL:String, videoName:String,nickNames:String){

        Toast.makeText(applicationContext,""+getString(R.string.cave_label),Toast.LENGTH_LONG).show()
        val rxDownloader = RxDownloader(applicationContext)
        val desc = getString(R.string.saving)
        val timeStamp =  System.currentTimeMillis()
        val file = "tiktok_"+"_"+timeStamp
        var ext = "mp4"
        val name = file + "." + ext
        val dex = File(Environment.getExternalStorageDirectory().absolutePath, "toksave")
        if (!dex.exists())
            dex.mkdirs()

        val Download_Uri = Uri.parse(downloadURL)
        val downloadManager =  getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val request =  DownloadManager.Request(Download_Uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE);
        request.setAllowedOverRoaming(true)
        request.setTitle( "♫ $videoName")
        request.setVisibleInDownloadsUi(true)
        request.setDescription(desc)
        request.setVisibleInDownloadsUi(true)
        request.allowScanningByMediaScanner()
        request.setNotificationVisibility(VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir("/toksave",  name)

        rxDownloader.download(request).subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(object: Observer<String> {
                    override fun onComplete() {


                    }

                    override fun onError(e: Throwable) {


                    }

                    override fun onNext(t: String) {


                    }

                    override fun onSubscribe(d: Disposable) {

                       finishAndRemoveTask()

                    }


                })

    }


}
