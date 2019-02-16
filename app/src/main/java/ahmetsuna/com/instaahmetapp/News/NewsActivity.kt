package ahmetsuna.com.instaahmetapp.News

import ahmetsuna.com.instaahmetapp.R
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class NewsActivity : AppCompatActivity() {


    private val ACTIVITY_NO=3
    private val TAG="NewsActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

       // setupNavigationView()
    }

    /*fun setupNavigationView(){

        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationView)
        BottomNavigationViewHelper.setupNavigation(this, bottomNavigationView)
        var menu = bottomNavigationView.menu
        var menuItem=menu.getItem(ACTIVITY_NO)
        menuItem.setChecked(true)
    }*/
}
