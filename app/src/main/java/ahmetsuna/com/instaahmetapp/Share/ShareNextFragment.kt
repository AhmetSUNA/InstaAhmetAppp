package ahmetsuna.com.instaahmetapp.Share


import ahmetsuna.com.instaahmetapp.R
import ahmetsuna.com.instaahmetapp.utils.EventBusDataEvents
import ahmetsuna.com.instaahmetapp.utils.UniversalImageLoader
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_share_next.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


class ShareNextFragment : Fragment() {

    var secilenResimYolu:String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.fragment_share_next, container, false)

        UniversalImageLoader.setImage(secilenResimYolu!!, view!!.imgSecilenResim, null, "file://")

        return view
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
