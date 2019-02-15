package ahmetsuna.com.instaahmetapp.Home

import ahmetsuna.com.instaahmetapp.Login.LoginActivity
import ahmetsuna.com.instaahmetapp.R
import ahmetsuna.com.instaahmetapp.utils.BottomNavigationViewHelper
import ahmetsuna.com.instaahmetapp.utils.HomePagerAdapter
import ahmetsuna.com.instaahmetapp.utils.UniversalImageLoader
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.nostra13.universalimageloader.core.ImageLoader
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    private val ACTIVITY_NO = 0
    private val TAG = "HomeActivity"

    lateinit var myAuth: FirebaseAuth
    lateinit var myAuthListener: FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        myAuth = FirebaseAuth.getInstance()
        //myAuth.signOut()

        setupAuthListener()

        initImageLoader()

        setupHomeViewPager()
    }

    override fun onResume() {
        setupNavigationView()
        super.onResume()
    }

    fun setupNavigationView() {
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationView)
        BottomNavigationViewHelper.setupNavigation(this, bottomNavigationView)
        var menu = bottomNavigationView.menu
        var menuItem = menu.getItem(ACTIVITY_NO)
        menuItem.setChecked(true)
    }

    private fun setupHomeViewPager() {

        var homePagerAdapter = HomePagerAdapter(supportFragmentManager)

        homePagerAdapter.addFragment(CameraFragment())  // id=0
        homePagerAdapter.addFragment(HomeFragment())    // id=1
        homePagerAdapter.addFragment(MessagesFragment())// id=2

        //activity main'de bulunan viewPager'a oluşturduğumuz adaptörü atadık
        homeViewPager.adapter = homePagerAdapter

        //viewPager'ın homefragmet ile başlamasını sağladık
        homeViewPager.setCurrentItem(1)
    }

    private fun initImageLoader(){

        var universalImageLoader = UniversalImageLoader(this)
        ImageLoader.getInstance().init(universalImageLoader.config)
    }

    private fun setupAuthListener() {
        myAuthListener = object : FirebaseAuth.AuthStateListener {
            override fun onAuthStateChanged(p0: FirebaseAuth) {
                var user = FirebaseAuth.getInstance().currentUser

                if (user == null) {
                    //Log.e("HATA", "Kullanıcı oturum açmamış, HomeActivitydesin")
                    var intent = Intent(this@HomeActivity, LoginActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    finish()
                } else {


                }
            }

        }
    }

    override fun onStart() {
        super.onStart()
        Log.e("HATA", "HomeActivitydesin")
        myAuth.addAuthStateListener(myAuthListener)
    }

    override fun onStop() {
        super.onStop()
        if (myAuthListener != null) {
            myAuth.removeAuthStateListener(myAuthListener)
        }
    }

}
