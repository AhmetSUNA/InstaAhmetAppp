package ahmetsuna.com.instaahmetapp.Home

import ahmetsuna.com.instaahmetapp.R
import ahmetsuna.com.instaahmetapp.utils.BottomNavigationViewHelper
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeFragment : Fragment(){

    lateinit var fragmentView:View
    private val ACTIVITY_NO = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        fragmentView= inflater.inflate(R.layout.fragment_home, container, false)

        return fragmentView
    }

    override fun onResume() {

        setupNavigationView()

        super.onResume()
    }

    fun setupNavigationView() {

        var fragmentBottomNavView = fragmentView.bottomNavigationView

        BottomNavigationViewHelper.setupBottomNavigationView(fragmentBottomNavView)
        BottomNavigationViewHelper.setupNavigation(activity!!, fragmentBottomNavView)
        var menu = fragmentBottomNavView.menu
        var menuItem = menu.getItem(ACTIVITY_NO)
        menuItem.setChecked(true)
    }
}