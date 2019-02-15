package ahmetsuna.com.instaahmetapp.utils

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.TextView
import kotlinx.android.synthetic.main.tek_sutun_grid_resim.view.*


class ShareActivityGridViewAdapter(context: Context, resource: Int, var klasordekiDosyalar: ArrayList<String>) :
    ArrayAdapter<String>(context, resource, klasordekiDosyalar) {

    var inflater: LayoutInflater
    var tekSutunResim: View? = null
    lateinit var viewHolder: ViewHolder

    init {
        inflater = LayoutInflater.from(context)
    }

    inner class ViewHolder() {

        lateinit var imageView: GridImageView
        lateinit var progressBar: ProgressBar
        lateinit var tvSure: TextView
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        tekSutunResim = convertView

        if (tekSutunResim == null) {
            tekSutunResim = inflater.inflate(ahmetsuna.com.instaahmetapp.R.layout.tek_sutun_grid_resim, parent, false)
            viewHolder = ViewHolder()

            viewHolder.imageView = tekSutunResim!!.imgTekSutunImage
            viewHolder.progressBar = tekSutunResim!!.progressBar
            viewHolder.tvSure = tekSutunResim!!.tvSure

            tekSutunResim!!.setTag(viewHolder)
        } else {

            viewHolder = tekSutunResim!!.getTag() as ViewHolder

        }

        var dosyaYolu = klasordekiDosyalar.get(position)
        var dosyaTuru = dosyaYolu.substring(dosyaYolu.lastIndexOf("."))

        if (dosyaTuru.equals(".mp4")) {

            viewHolder.tvSure.visibility = View.VISIBLE
            var retriver = MediaMetadataRetriever()
            retriver.setDataSource(context, Uri.parse("file://" + dosyaYolu))

            var videoSuresi = retriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            var videoSuresiLong = videoSuresi.toLong()

            Log.e("HATA3", "VİDEO SÜRESİ: " + videoSuresiLong)


            viewHolder.tvSure.setText(sureDonustur(videoSuresiLong))
            UniversalImageLoader.setImage(
                klasordekiDosyalar.get(position),
                viewHolder.imageView,
                viewHolder.progressBar,
                "file:/"
            )

        } else {

            viewHolder.tvSure.visibility = View.GONE
            UniversalImageLoader.setImage(
                klasordekiDosyalar.get(position),
                viewHolder.imageView,
                viewHolder.progressBar,
                "file:/"
            )

        }
        return tekSutunResim!!
    }

    //bir video veya şarkının süresini bulma
    fun sureDonustur(sure: Long): String {

        val saniye = sure / 1000 % 60
        val dakika = sure / (1000 * 60) % 60
        val saat = sure / (1000 * 60 * 60) % 24

        var zaman = ""
        if (saat > 0) {
            zaman = String().format("%02d:%02d:%02d", saat, dakika, saniye)
        } else {
            zaman = String().format("%02d:%02d", dakika, saniye)
        }
        return zaman
    }

}