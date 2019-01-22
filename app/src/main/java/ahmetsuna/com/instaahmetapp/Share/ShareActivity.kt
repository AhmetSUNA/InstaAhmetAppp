package ahmetsuna.com.instaahmetapp.Share

import ahmetsuna.com.instaahmetapp.R
import ahmetsuna.com.instaahmetapp.utils.BottomNavigationViewHelper
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_home.*

class ShareActivity : AppCompatActivity() {

    private val ACTIVITY_NO=2
    private val TAG="ShareActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setupNavigationView()
    }

    fun setupNavigationView(){

        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationSettings)
        BottomNavigationViewHelper.setupNavigation(this, bottomNavigationSettings)
        var menu = bottomNavigationSettings.menu
        var menuItem=menu.getItem(ACTIVITY_NO)
        menuItem.setChecked(true)
    }
}
