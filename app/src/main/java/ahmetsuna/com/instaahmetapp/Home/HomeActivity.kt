package ahmetsuna.com.instaahmetapp.Home

import ahmetsuna.com.instaahmetapp.R
import ahmetsuna.com.instaahmetapp.utils.HomePagerAdapter
import ahmetsuna.com.instaahmetapp.utils.UniversalImageLoader
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.nostra13.universalimageloader.core.ImageLoader
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    private val ACTIVITY_NO = 0
    private val TAG = "HomeActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        initImageLoader()
        //setupNavigationView()
        setupHomeViewPager()
    }

    /*fun setupNavigationView() {
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationSettings)
        BottomNavigationViewHelper.setupNavigation(this, bottomNavigationSettings)
        var menu = bottomNavigationSettings.menu
        var menuItem = menu.getItem(ACTIVITY_NO)
        menuItem.setChecked(true)
    }*/

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
}
