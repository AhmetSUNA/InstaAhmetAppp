package ahmetsuna.com.instaahmetapp.Profile

import ahmetsuna.com.instaahmetapp.Login.LoginActivity
import ahmetsuna.com.instaahmetapp.R
import ahmetsuna.com.instaahmetapp.utils.BottomNavigationViewHelper
import ahmetsuna.com.instaahmetapp.utils.UniversalImageLoader
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_profile.*



class ProfileActivity : AppCompatActivity() {

    private val ACTIVITY_NO=4
    private val TAG="ProfileActivity"

    lateinit var myAuth: FirebaseAuth
    lateinit var myAuthListener: FirebaseAuth.AuthStateListener



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        setupAuthListener()

        myAuth = FirebaseAuth.getInstance()

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
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationView)
        BottomNavigationViewHelper.setupNavigation(this, bottomNavigationView)
        var menu = bottomNavigationView.menu
        var menuItem=menu.getItem(ACTIVITY_NO)
        menuItem.setChecked(true)
    }

    private fun setupAuthListener() {
        myAuthListener=object : FirebaseAuth.AuthStateListener{
            override fun onAuthStateChanged(p0: FirebaseAuth) {
                var user=FirebaseAuth.getInstance().currentUser

                //kullanıcı null ise
                if(user == null){
                                                            //çıkış yapıldığında loginActivity'e yolla
                    var intent=Intent(this@ProfileActivity, LoginActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    finish()
                }else {


                }
            }

        }
    }

    override fun onStart() {
        super.onStart()
        Log.e("HATA","ProfilActivitydesin")
        myAuth.addAuthStateListener(myAuthListener)
    }

    override fun onStop() {
        super.onStop()
        if(myAuthListener != null){
            myAuth.removeAuthStateListener(myAuthListener)
        }
    }

}

