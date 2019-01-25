package ahmetsuna.com.instaahmetapp.Login


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
import kotlinx.android.synthetic.main.fragment_kayit.*
import kotlinx.android.synthetic.main.fragment_kayit.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


class KayitFragment : Fragment() {

    var telNo = ""
    var verification = ""
    var gelenKod = ""
    var gelenEmail=""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view =inflater.inflate(R.layout.fragment_kayit, container, false)

        view.etAdSoyad.addTextChangedListener(watcher)
        view.etSifre.addTextChangedListener(watcher)
        view.etKullaniciAdi.addTextChangedListener(watcher)

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

    @Subscribe(sticky = true)
    internal fun onKayitEvent(kayitBilgileri: EventBusDataEvents.KayitBilgileriniGonder) {

        if (kayitBilgileri.emailKayit == true) {
            gelenEmail = kayitBilgileri.email!!
            Toast.makeText(activity,"gelenEmail: " + gelenEmail ,Toast.LENGTH_SHORT).show()
            Log.e("ahmet", "gelen tel no: " + gelenEmail)
        } else {
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

}
