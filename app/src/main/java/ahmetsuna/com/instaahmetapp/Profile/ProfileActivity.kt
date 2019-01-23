package ahmetsuna.com.instaahmetapp.Profile

import ahmetsuna.com.instaahmetapp.R
import ahmetsuna.com.instaahmetapp.utils.BottomNavigationViewHelper
import ahmetsuna.com.instaahmetapp.utils.UniversalImageLoader
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_profile.*



class ProfileActivity : AppCompatActivity() {

    private val ACTIVITY_NO=4
    private val TAG="ProfileActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        setupNavigationView()
        setupToolbar()
        setupProfilePhoto()
    }

    private fun setupProfilePhoto() {

        //https://upload.wikimedia.org/wikipedia/commons/thumb/d/d7/Android_robot.svg/872px-Android_robot.svg.png

        var imgURL ="upload.wikimedia.org/wikipedia/commons/thumb/d/d7/Android_robot.svg/872px-Android_robot.svg.png"

        UniversalImageLoader.setImage(imgURL, circleProfileImage, progressBar, "https://")
    }

    private fun setupToolbar() {
        imgProfileSettings.setOnClickListener {

            var intent = Intent(this,ProfileSettingsActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }

        tvProfilDuzenleButunu.setOnClickListener {

            profileRoot.visibility = View.GONE
            var transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.profileContainer, ProfileEditFragment())
            transaction.addToBackStack("editProfileFragmentEklendi")
            transaction.commit()

        }
    }

    override fun onBackPressed() {
        profileRoot.visibility = View.VISIBLE
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

