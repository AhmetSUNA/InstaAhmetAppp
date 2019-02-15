package ahmetsuna.com.instaahmetapp.Share


import ahmetsuna.com.instaahmetapp.R
import ahmetsuna.com.instaahmetapp.utils.EventBusDataEvents
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.CameraView
import com.otaliastudios.cameraview.Gesture
import com.otaliastudios.cameraview.GestureAction
import kotlinx.android.synthetic.main.activity_share.*
import kotlinx.android.synthetic.main.fragment_share_camera.view.*
import org.greenrobot.eventbus.EventBus
import java.io.File
import java.io.FileOutputStream

class ShareCameraFragment : Fragment() {

     var cameraView : CameraView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.fragment_share_camera, container, false)

        cameraView = view.videoView
        cameraView!!.mapGesture(Gesture.PINCH, GestureAction.ZOOM)
        cameraView!!.mapGesture(Gesture.TAP, GestureAction.FOCUS_WITH_MARKER)

        view.imgFotoCek.setOnClickListener {
            cameraView!!.capturePicture()
        }

        cameraView!!.addCameraListener(object : CameraListener(){

            override fun onPictureTaken(jpeg: ByteArray?) {
                super.onPictureTaken(jpeg)

                var cekilenFotoAdi = System.currentTimeMillis()
                //gelen degeri nereye yazacagımıza dair dosya oluştur
                var cekilenFoto = File(Environment.getExternalStorageDirectory().absolutePath + "/Pictures/Ozel/" + cekilenFotoAdi + ".jpg")

                //gelen değeride streama aktararak, foto write edilir
                var dosyaOlustur =FileOutputStream(cekilenFoto)
                dosyaOlustur.write(jpeg)
                dosyaOlustur.close()

                activity!!.anaLayout.visibility = View.GONE
                activity!!.fragmenContainerLayout.visibility = View.VISIBLE
                var transaction = activity!!.supportFragmentManager.beginTransaction()

                EventBus.getDefault().postSticky(EventBusDataEvents.PaylasilacakResmiGonder(cekilenFoto.absolutePath.toString(), true) )
                transaction.replace(R.id.fragmenContainerLayout, ShareNextFragment())
                transaction.addToBackStack("shareNextFragmentEklendi")
                transaction.commit()

                Log.e("HATA2", "çekilen resim buraya kayıt edildi: " + cekilenFoto.absolutePath.toString())
            }
        })


        return view
    }

    override fun onResume() {
        super.onResume()
        Log.e("HATA2", "CAMERA FRAGMENTİ ON RESUME")
        cameraView!!.start()
    }

    override fun onPause() {
        super.onPause()
        Log.e("HATA2", "CAMERA FRAGMENTİ ON PAUSE")
        cameraView!!.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("HATA2", "CAMERA FRAGMENTİ ON DESTROY")
        cameraView!!.destroy()
    }
}
