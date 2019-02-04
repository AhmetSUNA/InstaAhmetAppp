package ahmetsuna.com.instaahmetapp.utils

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class SharePagerAdapter(fm: FragmentManager, tabAdlari: ArrayList<String>) : FragmentPagerAdapter(fm) {

    private var myFragmentList : ArrayList<Fragment> = ArrayList()
    private var myTabAdlari : ArrayList<String> = tabAdlari

    override fun getItem(position: Int): Fragment {
        return myFragmentList.get(position)
    }

    override fun getCount(): Int {

        return myFragmentList.size
    }

    fun addFragment(fragment: Fragment){
        myFragmentList.add(fragment)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return myTabAdlari.get(position)
    }
}