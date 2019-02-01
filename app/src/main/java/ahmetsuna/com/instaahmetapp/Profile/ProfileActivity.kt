package ahmetsuna.com.instaahmetapp.Profile

import ahmetsuna.com.instaahmetapp.Login.LoginActivity
import ahmetsuna.com.instaahmetapp.Models.Users
import ahmetsuna.com.instaahmetapp.R
import ahmetsuna.com.instaahmetapp.utils.BottomNavigationViewHelper
import ahmetsuna.com.instaahmetapp.utils.EventBusDataEvents
import ahmetsuna.com.instaahmetapp.utils.UniversalImageLoader
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_profile.*
import org.greenrobot.eventbus.EventBus


class ProfileActivity : AppCompatActivity() {

    private val ACTIVITY_NO = 4
    private val TAG = "ProfileActivity"

    lateinit var myAuth: FirebaseAuth
    lateinit var myAuthListener: FirebaseAuth.AuthStateListener
    lateinit var myUser: FirebaseUser
    lateinit var myRef: DatabaseReference

    var ilkAcilis = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        setupAuthListener()
        myAuth = FirebaseAuth.getInstance()
        myUser = myAuth.currentUser!!
        myRef = FirebaseDatabase.getInstance().reference

        setupToolbar()
        setupNavigationView()
        kullaniciBilgileriniGetir()

    }

    private fun kullaniciBilgileriniGetir() {

        //addValueEventListener realtime olarak verileri günceller
        myRef.child("users").child(myUser!!.uid).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {

                if (p0!!.getValue() != null) {

                    var okunanKullaniciBilgileri = p0!!.getValue(Users::class.java)

                    EventBus.getDefault().postSticky(EventBusDataEvents.KullaniciBilgileriniGonder(okunanKullaniciBilgileri))

                    tvProfileAdiToolbar.setText(okunanKullaniciBilgileri!!.user_name)
                    tvProfilGercekAdi.setText(okunanKullaniciBilgileri!!.adi_soyadi)
                    tvFollowerSayisi.setText(okunanKullaniciBilgileri!!.user_detail!!.follower)
                    tvFollowingSayisi.setText(okunanKullaniciBilgileri!!.user_detail!!.following)
                    tvPostSayisi.setText(okunanKullaniciBilgileri!!.user_detail!!.post)


                    if (ilkAcilis) {
                        ilkAcilis = false
                        var imgURL = okunanKullaniciBilgileri!!.user_detail!!.profile_picture!!
                        UniversalImageLoader.setImage(imgURL, circleProfileImage, progressBar, "")
                    }

                    if (!okunanKullaniciBilgileri!!.user_detail!!.biography!!.isNullOrEmpty()) {
                        tvBiyografi.visibility = View.VISIBLE
                        tvBiyografi.setText(okunanKullaniciBilgileri!!.user_detail!!.biography)
                    } else {
                        tvBiyografi.visibility = View.GONE
                    }
                    if (!okunanKullaniciBilgileri!!.user_detail!!.web_site!!.isNullOrEmpty()) {
                        tvWebSitesi.visibility = View.VISIBLE
                        tvWebSitesi.setText(okunanKullaniciBilgileri!!.user_detail!!.web_site)
                    } else {
                        tvWebSitesi.visibility = View.GONE
                    }
                }


            }
        })

    }


    private fun setupToolbar() {
        imgProfileSettings.setOnClickListener {

            var intent = Intent(this, ProfileSettingsActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }

        tvProfilDuzenleButunu.setOnClickListener {

            profileRoot.visibility = View.INVISIBLE
            profileContainer.visibility = View.VISIBLE
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
        var menuItem = menu.getItem(ACTIVITY_NO)
        menuItem.setChecked(true)
    }

    private fun setupAuthListener() {

        //bu tetiklenmeden buna ulaşmaya çalışırsak nullPointException çıkar dikkat!!!
        myAuthListener = object : FirebaseAuth.AuthStateListener {
            override fun onAuthStateChanged(p0: FirebaseAuth) {
                var user = FirebaseAuth.getInstance().currentUser

                //kullanıcı null ise
                if (user == null) {
                    //çıkış yapıldığında loginActivity'e yolla
                    var intent = Intent(
                        this@ProfileActivity,
                        LoginActivity::class.java
                    ).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    finish()
                } else {
                    myUser = myAuth.currentUser!!
                }
            }

        }
    }

    override fun onStart() {
        super.onStart()
        Log.e("HATA", "ProfilActivitydesin")
        myAuth.addAuthStateListener(myAuthListener)
    }

    override fun onStop() {
        super.onStop()
        if (myAuthListener != null) {
            myAuth.removeAuthStateListener(myAuthListener)
        }
    }

}

