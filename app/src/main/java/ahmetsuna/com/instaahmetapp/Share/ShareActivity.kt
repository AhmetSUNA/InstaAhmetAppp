package ahmetsuna.com.instaahmetapp.Share

import ahmetsuna.com.instaahmetapp.R
import ahmetsuna.com.instaahmetapp.utils.SharePagerAdapter
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_share.*

class ShareActivity : AppCompatActivity() {

    private val ACTIVITY_NO=2
    private val TAG="ShareActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)

        setupShareViewPager()


    }

    private fun setupShareViewPager() {

        var tabAdlari = ArrayList<String>()

        tabAdlari.add("GALERİ")
        tabAdlari.add("KAMERA")
        tabAdlari.add("VİDEO")

        var sharePagerAdapter = SharePagerAdapter(supportFragmentManager, tabAdlari)
        sharePagerAdapter.addFragment(ShareGalleryFragment())
        sharePagerAdapter.addFragment(ShareCameraFragment())
        sharePagerAdapter.addFragment(ShareVideoFragment())

        shareViewPager.adapter = sharePagerAdapter

        shareTabLayout.setupWithViewPager(shareViewPager)

    }

    override fun onBackPressed() {

        anaLayout.visibility = View.VISIBLE
        fragmenContainerLayout.visibility = View.GONE
        super.onBackPressed()
    }

}
