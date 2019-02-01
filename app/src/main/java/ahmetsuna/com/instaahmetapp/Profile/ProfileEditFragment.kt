package ahmetsuna.com.instaahmetapp.Profile


import ahmetsuna.com.instaahmetapp.Models.Users
import ahmetsuna.com.instaahmetapp.utils.EventBusDataEvents
import ahmetsuna.com.instaahmetapp.utils.UniversalImageLoader
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_profile_edit.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


class ProfileEditFragment : Fragment() {

    lateinit var circliProfileImageFragment: CircleImageView
    lateinit var gelenKullaniciBilgileri:Users

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(ahmetsuna.com.instaahmetapp.R.layout.fragment_profile_edit, container, false)

        setupKullaniciBilgileri(view)

        view.imgClose.setOnClickListener {

            activity!!.onBackPressed()
        }

        return view
    }

    private fun setupKullaniciBilgileri(view: View?) {

        view!!.etProfilName.setText(gelenKullaniciBilgileri.adi_soyadi)
        view!!.etUserName.setText(gelenKullaniciBilgileri.user_name)

        if (!gelenKullaniciBilgileri!!.user_detail!!.biography!!.isNullOrEmpty()){
            view!!.etUserBiyografi.setText(gelenKullaniciBilgileri!!.user_detail!!.biography)
        }
        if (!gelenKullaniciBilgileri!!.user_detail!!.web_site!!.isNullOrEmpty()){
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
