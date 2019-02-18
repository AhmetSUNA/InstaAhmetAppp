package ahmetsuna.com.instaahmetapp.Home

import ahmetsuna.com.instaahmetapp.Login.LoginActivity
import ahmetsuna.com.instaahmetapp.Models.Posts
import ahmetsuna.com.instaahmetapp.Models.UserPosts
import ahmetsuna.com.instaahmetapp.Models.Users
import ahmetsuna.com.instaahmetapp.R
import ahmetsuna.com.instaahmetapp.utils.BottomNavigationViewHelper
import ahmetsuna.com.instaahmetapp.utils.HomeFragmentRecyclerAdapter
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeFragment : Fragment(){

    lateinit var fragmentView:View
    private val ACTIVITY_NO = 0

    lateinit var tumGonderiler : ArrayList<UserPosts>

    lateinit var myAuth: FirebaseAuth
    lateinit var myAuthListener: FirebaseAuth.AuthStateListener
    lateinit var myUser: FirebaseUser
    lateinit var myRef: DatabaseReference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        fragmentView= inflater.inflate(R.layout.fragment_home, container, false)

        setupAuthListener()
        myAuth = FirebaseAuth.getInstance()
        myUser = myAuth.currentUser!!
        myRef = FirebaseDatabase.getInstance().reference
        tumGonderiler = ArrayList<UserPosts>()

        kullaniciPostlariniGetir(myUser.uid)

        return fragmentView
    }

    private fun kullaniciPostlariniGetir(kullaniciID:String) {


        myRef.child("users").child(kullaniciID).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {

                var userID = kullaniciID
                var kullaniciAdi = p0!!.getValue(Users::class.java)!!.user_name
                var kullanicFotoURL  = p0!!.getValue(Users::class.java)!!.user_detail!!.profile_picture

                myRef.child("posts").child(kullaniciID).addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {

                        if (p0!!.hasChildren()){

                            Log.e("HATA", "ÇOCUK VAR")
                            for (ds in p0!!.children){

                                var eklenecekUserPosts = UserPosts()

                                eklenecekUserPosts.userID = userID
                                eklenecekUserPosts.userName = kullaniciAdi
                                eklenecekUserPosts.userFotoURL = kullanicFotoURL

                                eklenecekUserPosts.postID = ds.getValue(Posts::class.java)!!.post_id
                                eklenecekUserPosts.postAciklama = ds.getValue(Posts::class.java)!!.aciklama
                                eklenecekUserPosts.postURL = ds.getValue(Posts::class.java)!!.file_url
                                eklenecekUserPosts.postYuklenmeTarih = ds.getValue(Posts::class.java)!!.yuklenme_tarih

                                tumGonderiler.add(eklenecekUserPosts)

                            }
                        }
                        setupRecylerView()
                    }
                })


            }


        })

    }

    private fun setupRecylerView() {

        var recylerView = fragmentView.recylerView
        var recyclerAdapter = HomeFragmentRecyclerAdapter(this.activity!!, tumGonderiler)

        recylerView.adapter = recyclerAdapter

        recylerView.layoutManager = LinearLayoutManager(this.activity!!,LinearLayoutManager.VERTICAL, true)
    }

    fun setupNavigationView() {

        var fragmentBottomNavView = fragmentView.bottomNavigationView

        BottomNavigationViewHelper.setupBottomNavigationView(fragmentBottomNavView)
        BottomNavigationViewHelper.setupNavigation(activity!!, fragmentBottomNavView)
        var menu = fragmentBottomNavView.menu
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
                    var intent = Intent(activity, LoginActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    activity!!.finish()
                } else {
                    myUser = myAuth.currentUser!!
                }
            }

        }
    }

    override fun onResume() {
        setupNavigationView()
        super.onResume()
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