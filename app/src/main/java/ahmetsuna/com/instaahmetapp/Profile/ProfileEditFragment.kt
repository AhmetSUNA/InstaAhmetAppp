package ahmetsuna.com.instaahmetapp.Profile


import ahmetsuna.com.instaahmetapp.Models.Users
import ahmetsuna.com.instaahmetapp.utils.EventBusDataEvents
import ahmetsuna.com.instaahmetapp.utils.UniversalImageLoader
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_profile_edit.*
import kotlinx.android.synthetic.main.fragment_profile_edit.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


class ProfileEditFragment : Fragment() {

    lateinit var circliProfileImageFragment: CircleImageView
    lateinit var gelenKullaniciBilgileri: Users
    lateinit var myDatabaseRef: DatabaseReference
    lateinit var myStorageRef: StorageReference

    var profilPhotoUri: Uri? = null

    val RESİM_SEC = 100

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(ahmetsuna.com.instaahmetapp.R.layout.fragment_profile_edit, container, false)

        myDatabaseRef = FirebaseDatabase.getInstance().reference
        myStorageRef = FirebaseStorage.getInstance().reference

        setupKullaniciBilgileri(view)

        view.imgClose.setOnClickListener {

            activity!!.onBackPressed()
        }

        view.tvFotografiDegistir.setOnClickListener {

            var intent = Intent()
            intent.setType("image/*")  //hangi tür image'ler gösterilsin?, bizde her tür dedik, jpg,png...
            intent.setAction(Intent.ACTION_PICK) //butona tıklandığında seçme özelliği verildi
            startActivityForResult(intent, RESİM_SEC)

        }

        view.imgBtnKayit.setOnClickListener {

            var profilGuncellendiMi =  false

            if (!gelenKullaniciBilgileri.adi_soyadi!!.equals(view.etProfilName.text.toString())) {
                myDatabaseRef.child("users").child(gelenKullaniciBilgileri.user_id!!).child("adi_soyadi")
                    .setValue(view.etProfilName.text.toString())
                profilGuncellendiMi =  true
            }

            if (!gelenKullaniciBilgileri.user_detail!!.biography.equals(view.etUserBiyografi.text.toString())) {
                myDatabaseRef.child("users").child(gelenKullaniciBilgileri.user_id!!).child("user_detail")
                    .child("biography").setValue(view.etUserBiyografi.text.toString())
                profilGuncellendiMi =  true
            }
            if (!gelenKullaniciBilgileri.user_detail!!.web_site.equals(view.etUserWebSite.text.toString())) {
                myDatabaseRef.child("users").child(gelenKullaniciBilgileri.user_id!!).child("user_detail")
                    .child("web_site").setValue(view.etUserWebSite.text.toString())
                profilGuncellendiMi =  true
            }
            if (!gelenKullaniciBilgileri.user_name!!.equals(view.etUserName.text.toString())) {
                myDatabaseRef.child("users").orderByChild("user_name")
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {

                        }

                        override fun onDataChange(p0: DataSnapshot) {

                            var userNameKullanimdaMi = false

                            for (ds in p0.children) {

                                var okunanKullaniciAdi = ds!!.getValue(Users::class.java)!!.user_name
                                //Log.e("HATA","okunan kullanici adı: " + okunanKullaniciAdi)

                                if (okunanKullaniciAdi!!.equals(view.etUserName.text.toString())) {
                                    Toast.makeText(activity, "Kullanıcı adı kullanımda", Toast.LENGTH_SHORT).show()
                                    userNameKullanimdaMi = true
                                    break
                                }
                            }
                            if (userNameKullanimdaMi == false) {
                                myDatabaseRef.child("users").child(gelenKullaniciBilgileri!!.user_id!!)
                                    .child("user_name").setValue(view.etUserName.text.toString())
                                profilGuncellendiMi =  true
                            }

                        }
                    })
            }

            if (profilPhotoUri != null){

                var dialogYukleniyor = YukleniyorFragment()
                dialogYukleniyor.show(activity!!.supportFragmentManager, "yukleniyor")

                var ref =myStorageRef.child("users").child(gelenKullaniciBilgileri.user_id!!).child(profilPhotoUri!!.lastPathSegment)

                var uploadTask = ref.putFile(profilPhotoUri!!)
                val urlTask = uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                        }
                    }
                    return@Continuation ref.downloadUrl
                }).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val downloadUri = task.result
                        myDatabaseRef.child("users").child(gelenKullaniciBilgileri.user_id!!).child("user_detail").child("profile_picture")
                            .setValue(downloadUri.toString())

                        profilGuncellendiMi=true

                        dialogYukleniyor.dismiss()
                    } else {

                    }
                }
            }

            if (profilGuncellendiMi == true){
                Toast.makeText(activity, "Kullanıcı Güncellendi", Toast.LENGTH_SHORT).show()
            }

        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RESİM_SEC && resultCode == AppCompatActivity.RESULT_OK && data!!.data != null) {

            profilPhotoUri = data!!.data!!

            circleProfileImage.setImageURI(profilPhotoUri)

        }
    }

    private fun setupKullaniciBilgileri(view: View?) {

        view!!.etProfilName.setText(gelenKullaniciBilgileri.adi_soyadi)
        view!!.etUserName.setText(gelenKullaniciBilgileri.user_name)

        if (!gelenKullaniciBilgileri!!.user_detail!!.biography!!.isNullOrEmpty()) {
            view!!.etUserBiyografi.setText(gelenKullaniciBilgileri!!.user_detail!!.biography)
        }
        if (!gelenKullaniciBilgileri!!.user_detail!!.web_site!!.isNullOrEmpty()) {
            view!!.etUserWebSite.setText(gelenKullaniciBilgileri!!.user_detail!!.web_site)
        }

        var imgURL = gelenKullaniciBilgileri!!.user_detail!!.profile_picture
        UniversalImageLoader.setImage(imgURL!!, view!!.circleProfileImage, view!!.progressBar, "")

    }


    ///////////////////////////////////EVENTBUS///////////////////////////////

    @Subscribe(sticky = true)
    internal fun onKullaniciBilgileriEvent(kullaniBilgileri: EventBusDataEvents.KullaniciBilgileriniGonder) {

        gelenKullaniciBilgileri = kullaniBilgileri!!.kullanici!!

    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        EventBus.getDefault().register(this)
    }

    override fun onDetach() {
        super.onDetach()
        EventBus.getDefault().unregister(this)
    }


}
