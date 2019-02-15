package ahmetsuna.com.instaahmetapp.Share


import ahmetsuna.com.instaahmetapp.R
import ahmetsuna.com.instaahmetapp.utils.EventBusDataEvents
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.CameraView
import kotlinx.android.synthetic.main.activity_share.*
import kotlinx.android.synthetic.main.fragment_share_video.view.*
import org.greenrobot.eventbus.EventBus
import java.io.File

class ShareVideoFragment : Fragment() {

    var videoView : CameraView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view =  inflater.inflate(R.layout.fragment_share_video, container, false)

        videoView = view.videoView

        var olusacakVideoDosyaAdi = System.currentTimeMillis()
        var olusacakVideoDosya = File(Environment.getExternalStorageDirectory().absolutePath + "/Pictures/Ozel/" + olusacakVideoDosyaAdi + ".mp4")

        videoView!!.addCameraListener(object : CameraListener(){

            override fun onVideoTaken(video: File?) {
                super.onVideoTaken(video)

                activity!!.anaLayout.visibility = View.GONE
                activity!!.fragmenContainerLayout.visibility = View.VISIBLE
                var transaction = activity!!.supportFragmentManager.beginTransaction()

                EventBus.getDefault().postSticky(EventBusDataEvents.PaylasilacakResmiGonder(video!!.absolutePath.toString(), false) )
                transaction.replace(R.id.fragmenContainerLayout, ShareNextFragment())
                transaction.addToBackStack("shareNextFragmentEklendi")
                transaction.commit()

                Log.e("HATA2", "çekilen video buraya kayıt edildi: " + video.absolutePath.toString())
            }

        })

        view.imgVideoCek.setOnTouchListener(object : View.OnTouchListener{
            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {

                if (p1!!.action==MotionEvent.ACTION_DOWN){

                    videoView!!.startCapturingVideo(olusacakVideoDosya)
                    Toast.makeText(activity, "Video kaydediliyor", Toast.LENGTH_SHORT).show()

                    return true
                }else if (p1!!.action==MotionEvent.ACTION_UP){
                    Toast.makeText(activity, "Video kaydedildi", Toast.LENGTH_SHORT).show()
                    videoView!!.stopCapturingVideo()

                    return true
                }

                return false

            }


        })

        view.imgClose.setOnClickListener {

            activity!!.onBackPressed()
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        Log.e("HATA2", "VİDEO FRAGMENTİ ON RESUME")
        videoView!!.start()
    }

    override fun onPause() {
        super.onPause()
        Log.e("HATA2", "VİDEO FRAGMENTİ ON PAUSE")
        videoView!!.stop()

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("HATA2", "VİDEO FRAGMENTİ ON DESTROY")
        if (videoView!=null)
        videoView!!.destroy()

    }


}
