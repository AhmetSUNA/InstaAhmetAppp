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
import android.widget.Toast
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
        return inflater.inflate(R.layout.fragment_kayit, container, false)
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
