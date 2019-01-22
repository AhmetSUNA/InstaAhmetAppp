package ahmetsuna.com.instaahmetapp.Profile

import ahmetsuna.com.instaahmetapp.R
import ahmetsuna.com.instaahmetapp.utils.BottomNavigationViewHelper
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_profile_settings.*


class ProfileSettingsActivity : AppCompatActivity() {

    private val ACTIVITY_NO=4
    private val TAG="ProfileActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_settings)

        setupNavigationView()
        setupToolbar()
        fragmenNavigations()
    }

    private fun fragmenNavigations() {

        tvProfilDüzenleHesapAyarlari.setOnClickListener {
            profileSettingsRoot.visibility = View.GONE
            var transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.profileSettingsContainer, ProfileEditFragment())
            transaction.addToBackStack("editProfileFragmentEklendi")
            transaction.commit()
        }

        tvCikisHesapAyarlari.setOnClickListener {
            profileSettingsRoot.visibility = View.GONE
            var transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.profileSettingsContainer, SignOutFragment())
            transaction.addToBackStack("signOutFragmentEklendi")
            transaction.commit()
        }
    }

    private fun setupToolbar() {
        imgBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        profileSettingsRoot.visibility = View.VISIBLE
        super.onBackPressed()
    }

    private fun setupNavigationView() {
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationSettings)
        BottomNavigationViewHelper.setupNavigation(this, bottomNavigationSettings)
        var menu = bottomNavigationSettings.menu
        var menuItem=menu.getItem(ACTIVITY_NO)
        menuItem.setChecked(true)
    }
}
