package ahmetsuna.com.instaahmetapp.Share


import ahmetsuna.com.instaahmetapp.Models.Posts
import ahmetsuna.com.instaahmetapp.Profile.YukleniyorFragment
import ahmetsuna.com.instaahmetapp.R
import ahmetsuna.com.instaahmetapp.utils.EventBusDataEvents
import ahmetsuna.com.instaahmetapp.utils.UniversalImageLoader
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.OnProgressListener
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.fragment_share_next.*
import kotlinx.android.synthetic.main.fragment_share_next.view.*
import kotlinx.android.synthetic.main.fragment_yukleniyor.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


class ShareNextFragment : Fragment() {

    var secilenResimYolu:String? = null
    lateinit var photoURI: Uri

    lateinit var myAuth: FirebaseAuth
    lateinit var myUser: FirebaseUser
    lateinit var myRef: DatabaseReference
    lateinit var myStorageReference: StorageReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.fragment_share_next, container, false)

        UniversalImageLoader.setImage(secilenResimYolu!!, view!!.imgSecilenResim, null, "file://")

        photoURI = Uri.parse("file://" + secilenResimYolu)

        myAuth = FirebaseAuth.getInstance()
        myUser = myAuth.currentUser!!
        myRef = FirebaseDatabase.getInstance().reference
        myStorageReference = FirebaseStorage.getInstance().reference

            view.tvPaylasButton.setOnClickListener {

            var dialogYukleniyor = YukleniyorFragment()
            dialogYukleniyor.show(activity!!.supportFragmentManager, "yukleniyor")
            dialogYukleniyor.isCancelable = false

            var ref =myStorageReference.child("users").child(myUser.uid).child(photoURI.lastPathSegment)

            var uploadTask = ref.putFile(photoURI)
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
                    dialogYukleniyor.dismiss()
                    veritabaninaBilgileriYaz(downloadUri.toString())

                } else {
                    dialogYukleniyor.dismiss()
                    Toast.makeText(activity, "Hata oluştu" + task.exception!!.message, Toast.LENGTH_SHORT).show()
                }
            }
                uploadTask.addOnProgressListener ( object : OnProgressListener<UploadTask.TaskSnapshot> {
                    override fun onProgress(p0: UploadTask.TaskSnapshot?) {

                        var progress = 100.0 * p0!!.bytesTransferred / p0!!.totalByteCount
                        dialogYukleniyor.tvBilgi.text = "%" + progress.toInt().toString() + "yüklendi.."
                    }
                } )



        }

        return view
    }

    private fun veritabaninaBilgileriYaz(yuklenenFotoURL: String) {

        var postID = myRef.child("posts").child(myUser.uid).push().key

        var yuklenenPost = Posts(myUser.uid, postID,"",etPostAciklama.text.toString(), yuklenenFotoURL)

        myRef.child("posts").child(myUser.uid).child(postID!!).setValue(yuklenenPost)
        myRef.child("posts").child(myUser.uid).child(postID).child("yuklenme_tarih").setValue(ServerValue.TIMESTAMP)

    }

    ///////////////////////////////////EVENTBUS///////////////////////////////

    @Subscribe(sticky = true)
    internal fun onSecilenResimEvent(secilenResim: EventBusDataEvents.PaylasilacakResmiGonder) {
        secilenResimYolu = secilenResim!!.resimYolu!!
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
