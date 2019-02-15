package ahmetsuna.com.instaahmetapp.utils

import ahmetsuna.com.instaahmetapp.R
import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.tek_sutun_grid_resim.view.*
import org.greenrobot.eventbus.EventBus

class ShareActivityGalleryRecylerViewAdapter(var klasordekiDosyalar: ArrayList<String>, var myContext: Context) :
    RecyclerView.Adapter<ShareActivityGalleryRecylerViewAdapter.MyViewHolder>() {

    lateinit var inflater: LayoutInflater

    init {
        inflater = LayoutInflater.from(myContext)
    }

    //listemizde kaçtane eleman oldugunu belirten method
    override fun getItemCount(): Int {

        return klasordekiDosyalar.size

    }

    //öğelerin ilk defa yapıldığı yani inflate edildiği kısım
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        //XML'den java ya dönüştürme
        var tekSutunDosya = inflater.inflate(R.layout.tek_sutun_grid_resim, parent, false)

        return MyViewHolder(tekSutunDosya)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        var dosyaYolu = klasordekiDosyalar.get(position)
        var dosyaTuru = dosyaYolu.substring(dosyaYolu.lastIndexOf("."))

        if (dosyaTuru.equals(".mp4")) {

            holder.videoSure.visibility = View.VISIBLE
            var retriver = MediaMetadataRetriever()
            retriver.setDataSource(myContext, Uri.parse("file://" + dosyaYolu))

            var videoSuresi = retriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            var videoSuresiLong = videoSuresi.toLong()

            holder.videoSure.setText(sureDonustur(videoSuresiLong))
            UniversalImageLoader.setImage(dosyaYolu, holder.dosyaResim, holder.progressBar, "file:/")
        } else {

            holder.videoSure.visibility = View.GONE
            UniversalImageLoader.setImage(dosyaYolu, holder.dosyaResim, holder.progressBar, "file:/")
        }

        holder.tekSutunDosya.setOnClickListener {
            EventBus.getDefault().post(EventBusDataEvents.GaleriSecilenDosyaYoluGonder(dosyaYolu))
        }

    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tekSutunDosya = itemView as ConstraintLayout

        var dosyaResim = tekSutunDosya.imgTekSutunImage
        var videoSure = tekSutunDosya.tvSure
        var progressBar = tekSutunDosya.progressBar



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