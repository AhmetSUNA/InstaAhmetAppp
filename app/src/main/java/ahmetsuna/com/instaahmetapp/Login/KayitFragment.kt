package ahmetsuna.com.instaahmetapp.Login


import ahmetsuna.com.instaahmetapp.Models.Users
import ahmetsuna.com.instaahmetapp.R
import ahmetsuna.com.instaahmetapp.utils.EventBusDataEvents
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_kayit.*
import kotlinx.android.synthetic.main.fragment_kayit.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


class KayitFragment : Fragment() {

    var telNo = ""
    var verification = ""
    var gelenKod = ""
    var gelenEmail=""
    var emailIleKayitIslemi = true
    lateinit var myAuth: FirebaseAuth
    lateinit var myRef:DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.fragment_kayit, container, false)

        myAuth = FirebaseAuth.getInstance()

        //sistemdeki kullanıcıyı atma
        if(myAuth.currentUser != null){
            myAuth.signOut()
        }

        myRef = FirebaseDatabase.getInstance().reference

        view.etAdSoyad.addTextChangedListener(watcher)
        view.etSifre.addTextChangedListener(watcher)
        view.etKullaniciAdi.addTextChangedListener(watcher)

        view.btnGiris.setOnClickListener {

            //kullanıcı email ile kayıt olmak istiyor
            if (emailIleKayitIslemi) {

                var sifre = view.etSifre.text.toString()

                myAuth.createUserWithEmailAndPassword(gelenEmail,sifre)
                    .addOnCompleteListener(object : OnCompleteListener<AuthResult>{
                        override fun onComplete(p0: Task<AuthResult>) {
                            if (p0.isSuccessful){
                                Toast.makeText(activity,"Oturum email ile açıldı"+ myAuth.currentUser!!.uid, Toast.LENGTH_SHORT).show()
                            }else{
                                Toast.makeText(activity,"Oturum açılamadı:" + p0.exception, Toast.LENGTH_SHORT).show()
                            }
                        }
                    })
            }

            //kullanıcı telefon no ile kayıt olmak istiyor
            else {

                var sifre = view.etSifre.text.toString()
                var userName = view.etKullaniciAdi.text.toString()
                var adSoyad = view.etAdSoyad.text.toString()

                myAuth.createUserWithEmailAndPassword(gelenEmail,sifre)
                    .addOnCompleteListener(object : OnCompleteListener<AuthResult>{
                        override fun onComplete(p0: Task<AuthResult>) {
                            if (p0.isSuccessful){
                                Toast.makeText(activity,"Oturum tel no ile açıldı Uid" + myAuth.currentUser!!.uid, Toast.LENGTH_SHORT).show()

                                var userID =myAuth.currentUser?.uid.toString()
                                //oturum açan kullanıcının verilerini database'e kayıt edelim
                                var kaydedilecekKullanici = Users(gelenEmail,sifre, userName,adSoyad,userID)

                                myRef.child("users").child(userID).setValue(kaydedilecekKullanici)
                                    .addOnCompleteListener(object : OnCompleteListener<Void>{
                                        override fun onComplete(p0: Task<Void>) {
                                            if (p0.isSuccessful){
                                                Toast.makeText(activity,"Kullanıcı kayıt edildi" , Toast.LENGTH_SHORT).show()
                                            }else{
                                                Toast.makeText(activity,"Kullanıcı kayıt edilmedi" , Toast.LENGTH_SHORT).show()

                                            }
                                        }

                                    })



                            }else{
                                Toast.makeText(activity,"Oturum açılamadı:" + p0.exception, Toast.LENGTH_SHORT).show()
                            }
                        }
                    })

            }


        }
        return view
    }
    var watcher : TextWatcher = object : TextWatcher{
        override fun afterTextChanged(p0: Editable?) {

        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            if(p0!!.length>5){

                if(etAdSoyad.text.toString().length>5 && etSifre.text.toString().length>5 && etKullaniciAdi.text.toString().length>5){
                    btnGiris.isEnabled = true
                    btnGiris.setTextColor(ContextCompat.getColor(activity!!, R.color.beyaz))
                    btnGiris.setBackgroundResource(R.drawable.register_button_aktif)

                }else{
                    btnGiris.isEnabled = false
                    btnGiris.setTextColor(ContextCompat.getColor(activity!!, R.color.sonukmavi))
                    btnGiris.setBackgroundResource(R.drawable.register_button)

                }

            }else{

                btnGiris.isEnabled = false
                btnGiris.setTextColor(ContextCompat.getColor(activity!!, R.color.sonukmavi))
                btnGiris.setBackgroundResource(R.drawable.register_button)
            }
        }
    }


    ///////////////////////////////////EVENTBUS///////////////////////////////

    @Subscribe(sticky = true)
    internal fun onKayitEvent(kayitBilgileri: EventBusDataEvents.KayitBilgileriniGonder) {

        if (kayitBilgileri.emailKayit == true) {
            emailIleKayitIslemi=true
            gelenEmail = kayitBilgileri.email!!
            Toast.makeText(activity,"gelenEmail: " + gelenEmail ,Toast.LENGTH_SHORT).show()
            Log.e("ahmet", "gelen tel no: " + gelenEmail)
        } else {
            emailIleKayitIslemi=false
            telNo=kayitBilgileri.telNo!!
            verification = kayitBilgileri.verificationID!!
            gelenKod = kayitBilgileri.code!!

            Toast.makeText(activity,"gelencode: " + gelenKod + "verificatinoID: " + verification,Toast.LENGTH_SHORT).show()
        }


    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        EventBus.getDefault().register(this)
    }

    override fun onDetach() {
        super.onDetach()
        EventBus.getDefault().unregister(this)
    }
    ///////////////////////////////////EVENTBUS///////////////////////////////

}
