package ahmetsuna.com.instaahmetapp.Home

import ahmetsuna.com.instaahmetapp.R
import ahmetsuna.com.instaahmetapp.utils.EventBusDataEvents
import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.otaliastudios.cameraview.*
import kotlinx.android.synthetic.main.fragment_camera.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.io.File
import java.io.FileOutputStream

class CameraFragment : Fragment(){

    var myCamera: CameraView? = null
    var kameraIzniVerildiMi = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view = inflater?.inflate(R.layout.fragment_camera, container, false)

        myCamera = view!!.camera_view
        myCamera!!.mapGesture(Gesture.PINCH, GestureAction.ZOOM)
        myCamera!!.mapGesture(Gesture.TAP, GestureAction.FOCUS_WITH_MARKER)

        myCamera!!.addCameraListener(object : CameraListener(){

            override fun onPictureTaken(jpeg: ByteArray?) {
                super.onPictureTaken(jpeg)

                var cekilenFotoAdi = System.currentTimeMillis()
                //gelen degeri nereye yazacagımıza dair dosya oluştur
                var cekilenFoto = File(Environment.getExternalStorageDirectory().absolutePath + "/Pictures/Ozel/" + cekilenFotoAdi + ".jpg")

                //gelen değeride streama aktararak, foto write edilir
                var dosyaOlustur = FileOutputStream(cekilenFoto)
                dosyaOlustur.write(jpeg)
                dosyaOlustur.close()

                Log.e("HATA2", "çekilen resim buraya kayıt edildi: " + cekilenFoto.absolutePath.toString())
            }

        })

        view.imgCameraSwitch.setOnClickListener {

            if (myCamera!!.facing == Facing.BACK){
                myCamera!!.facing = Facing.FRONT
            }else{
                myCamera!!.facing = Facing.BACK
            }

        }

        view.imgFotoCek.setOnClickListener {

            if (myCamera!!.facing == Facing.BACK){
                myCamera!!.capturePicture()
            }else{
                myCamera!!.captureSnapshot()
            }
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        Log.e("HATA2", "CAMERA FRAGMENTİ ON RESUME")
        if (kameraIzniVerildiMi == true)
        myCamera!!.start()
    }

    override fun onPause() {
        super.onPause()
        Log.e("HATA2", "CAMERA FRAGMENTİ ON PAUSE")
        myCamera!!.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("HATA2", "CAMERA FRAGMENTİ ON DESTROY")

        if (myCamera!=null)
            myCamera!!.destroy()
    }

    ///////////////////////////////////EVENTBUS///////////////////////////////
    @Subscribe(sticky = true)
    internal fun onKameraIzinEvent(IzinDurumu: EventBusDataEvents.KameraIzinBilgisiGonder) {
       kameraIzniVerildiMi = IzinDurumu.kameraIzniVerildiMi!!
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