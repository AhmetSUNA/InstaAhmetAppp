package ahmetsuna.com.instaahmetapp.Share

import ahmetsuna.com.instaahmetapp.Login.LoginActivity
import ahmetsuna.com.instaahmetapp.R
import ahmetsuna.com.instaahmetapp.utils.SharePagerAdapter
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_share.*

class ShareActivity : AppCompatActivity() {

    private val ACTIVITY_NO=2
    private val TAG="ShareActivity"

    lateinit var myAuthListener: FirebaseAuth.AuthStateListener


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)

        setupAuthListener()

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

    private fun setupAuthListener() {

        //bu tetiklenmeden buna ulaşmaya çalışırsak nullPointException çıkar dikkat!!!
        myAuthListener = object : FirebaseAuth.AuthStateListener {
            override fun onAuthStateChanged(p0: FirebaseAuth) {
                var user = FirebaseAuth.getInstance().currentUser

                //kullanıcı null ise
                if (user == null) {
                    //çıkış yapıldığında loginActivity'e yolla
                    var intent = Intent(this@ShareActivity, LoginActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    finish()
                } else {


                }
            }

        }
    }

}
