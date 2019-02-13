package ahmetsuna.com.instaahmetapp.Share


import ahmetsuna.com.instaahmetapp.R
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.otaliastudios.cameraview.CameraView
import kotlinx.android.synthetic.main.fragment_share_video.view.*

class ShareCameraFragment : Fragment() {

    lateinit var cameraView : CameraView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.fragment_share_camera, container, false)

        cameraView = view.videoView



        return view
    }

    override fun onResume() {
        super.onResume()
        Log.e("HATA2", "CAMERA FRAGMENTİ ON RESUME")
        cameraView.start()
    }

    override fun onPause() {
        super.onPause()
        Log.e("HATA2", "CAMERA FRAGMENTİ ON PAUSE")
        cameraView.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("HATA2", "CAMERA FRAGMENTİ ON DESTROY")
        cameraView.destroy()
    }
}
