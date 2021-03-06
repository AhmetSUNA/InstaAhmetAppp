package ahmetsuna.com.instaahmetapp.Login


import ahmetsuna.com.instaahmetapp.R
import ahmetsuna.com.instaahmetapp.utils.EventBusDataEvents
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.fragment_telefon_kodu_gir.*
import kotlinx.android.synthetic.main.fragment_telefon_kodu_gir.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.util.concurrent.TimeUnit

class TelefonKoduGirFragment : Fragment() {

    var gelenTelNo = ""
    lateinit var callbacks:PhoneAuthProvider.OnVerificationStateChangedCallbacks
    var verificationID = ""
    var gelenKod = ""
    lateinit var progressBar:ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.fragment_telefon_kodu_gir, container, false)

        view.tvKullaniciTelNo.setText(gelenTelNo)
        progressBar=view.pbTelefonKoduGir
        setupCallback()

        view.btnTelKodIleri.setOnClickListener {

            if(gelenKod.equals(etOnayKodu.text.toString())){

                EventBus.getDefault().postSticky(EventBusDataEvents.KayitBilgileriniGonder(gelenTelNo,null,verificationID,gelenKod,false))
                var transaction = activity!!.supportFragmentManager.beginTransaction()
                transaction.replace(R.id.loginContainer, KayitFragment())
                transaction.addToBackStack("kayitFragmenEklendi")
                transaction.commit()

            }else{
                Toast.makeText(activity,"kod hatalı",Toast.LENGTH_SHORT).show()
            }
        }

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            gelenTelNo,       // Phone number to verify
            60,               // Timeout duration
            TimeUnit.SECONDS, // Unit of timeout
            this.activity!!,  // Activity (for callback binding)
            callbacks)        // OnVerificationStateChangedCallbacks

        return view
    }

    private fun setupCallback() {

        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            //başarılı şekilde code gelince tetiklenen method
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {

                if(!credential.smsCode.isNullOrEmpty()) {
                    gelenKod = credential.smsCode!!
                    progressBar.visibility=View.INVISIBLE
                    Log.e("HATA","on verification completed sms gelmiş: " + gelenKod)
                }else{
                    Log.e("HATA","on verification completed sms gelmeyecek")
                }
            }

            //bir hata durumunda tetiklenecek method
            override fun onVerificationFailed(e: FirebaseException) {

                Log.e("hata","hata çıktısı:" + e.message)

            }

            //sms gönderilince tetiklenen method
            override fun onCodeSent(verificationId: String?, token: PhoneAuthProvider.ForceResendingToken) {

                verificationID = verificationId!!
                progressBar.visibility=View.VISIBLE
                Log.e("HATA","oncodesent çalıştı")

            }
        }

    }

    @Subscribe(sticky = true)
    internal fun onTelefonNoEven(kayitBilgileri: EventBusDataEvents.KayitBilgileriniGonder){

        gelenTelNo = kayitBilgileri.telNo!!
        Log.e("ahmet", "gelen tel no: " + gelenTelNo)
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
