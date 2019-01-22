package ahmetsuna.com.instaahmetapp.utils

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class HomePagerAdapter(fm:FragmentManager) : FragmentPagerAdapter(fm) {

    private var myFragmenList : ArrayList<Fragment> = ArrayList()


    //fragmen pager adapterin yazmayı zorunlu kıldığı fonksiyon
    override fun getItem(position: Int): Fragment {
        return myFragmenList.get(position)
    }

    //fragmen pager adapterin yazmayı zorunlu kıldığı fonksiyon
    override fun getCount(): Int {
        return myFragmenList.size
    }

    //kişisel fonksiyon
    fun addFragment(fragment: Fragment){
        myFragmenList.add(fragment)
    }
}