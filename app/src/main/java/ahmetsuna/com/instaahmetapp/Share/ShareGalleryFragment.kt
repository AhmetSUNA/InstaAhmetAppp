package ahmetsuna.com.instaahmetapp.Share


import ahmetsuna.com.instaahmetapp.R
import ahmetsuna.com.instaahmetapp.utils.DosyaIslemleri
import ahmetsuna.com.instaahmetapp.utils.EventBusDataEvents
import ahmetsuna.com.instaahmetapp.utils.ShareActivityGridViewAdapter
import ahmetsuna.com.instaahmetapp.utils.UniversalImageLoader
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_share.*
import kotlinx.android.synthetic.main.fragment_share_gallery.*
import kotlinx.android.synthetic.main.fragment_share_gallery.view.*
import org.greenrobot.eventbus.EventBus

class ShareGalleryFragment : Fragment() {

    var secilenResimYolu: String? = null
    var dosyaTuruResimmi: Boolean? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.fragment_share_gallery, container, false)

        var klasorPaths = ArrayList<String>()

        var klasorAdlari = ArrayList<String>()

        var root = Environment.getExternalStorageDirectory().path

        var indirilenResimler = root + "/Download"
        var ozelResimler = root + "/Pictures/Ozel"
        var kameraResimleri = root + "/DCIM/Camera"
        var whatsappResimleri = root + "/WhatsApp/Media/WhatsApp Images"

        klasorPaths.add(indirilenResimler)
        klasorPaths.add(ozelResimler)
        klasorPaths.add(kameraResimleri)
        klasorPaths.add(whatsappResimleri)

        klasorAdlari.add("İndirilenler")
        klasorAdlari.add("Özel")
        klasorAdlari.add("Kamera")
        klasorAdlari.add("WhatsApp")

        var spinnerArrayAdapter = ArrayAdapter(activity!!, android.R.layout.simple_spinner_item, klasorAdlari)
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        view.spnKlasorAdlari.adapter = spinnerArrayAdapter

        //ilk açıldığında enson yüklenen dosya gösterilir
        view.spnKlasorAdlari.setSelection(0)

        view.spnKlasorAdlari.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, id: Long) {

                setupGridView(DosyaIslemleri.klasordekiDosyalariGetir(klasorPaths.get(position)))

            }
        }

        view.tvIleriButton.setOnClickListener {

            activity!!.anaLayout.visibility = View.GONE
            activity!!.fragmenContainerLayout.visibility = View.VISIBLE
            var transaction = activity!!.supportFragmentManager.beginTransaction()

            EventBus.getDefault().postSticky(EventBusDataEvents.PaylasilacakResmiGonder(secilenResimYolu, dosyaTuruResimmi) )
            videoView.stopPlayback() //bir sonraki fragmentte video sesini durdurur
            transaction.replace(R.id.fragmenContainerLayout, ShareNextFragment())
            transaction.addToBackStack("shareNextFragmentEklendi")
            transaction.commit()

        }

        view.imgClose.setOnClickListener {

            activity!!.onBackPressed()

        }

        return view
    }

    fun setupGridView(secilenKlasordekiDosyalar : ArrayList<String>){

        var gridViewAdapter = ShareActivityGridViewAdapter(this.activity!!, R.layout.tek_sutun_grid_resim, secilenKlasordekiDosyalar)

        gridResimler.adapter = gridViewAdapter

        //ilk açıldığında ilk dosya gösterilir
        secilenResimYolu = secilenKlasordekiDosyalar.get(0)
        resimVeyaVideoGoster(secilenKlasordekiDosyalar.get(0))

        gridResimler.setOnItemClickListener(object : AdapterView.OnItemClickListener{
            override fun onItemClick(p0: AdapterView<*>?, view: View?, position: Int, id: Long) {
            secilenResimYolu= secilenKlasordekiDosyalar.get(position)
            resimVeyaVideoGoster(secilenKlasordekiDosyalar.get(position))

            }

        })
    }

    private fun resimVeyaVideoGoster(dosyaYolu: String) {

        var dosyaTuru = dosyaYolu.substring(dosyaYolu.lastIndexOf("."))
        //file://video.mp4

        if (dosyaTuru != null){
            if (dosyaTuru.equals(".mp4")){

                videoView.visibility = View.VISIBLE
                imgCropView.visibility = View.GONE
                dosyaTuruResimmi = false
                videoView.setVideoURI(Uri.parse("file://" + dosyaYolu))
                videoView.start()
            }else{

                videoView.visibility = View.GONE
                imgCropView.visibility = View.VISIBLE
                dosyaTuruResimmi = true
                UniversalImageLoader.setImage(dosyaYolu, imgCropView, null, "file://")
            }
        }



    }


    override fun onResume() {
        super.onResume()
        Log.e("HATA2", "GALERY FRAGMENTI ON RESUME")
    }

    override fun onPause() {
        super.onPause()
        Log.e("HATA2", "GALERY FRAGMENTI ON PAUSE")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("HATA2", "GALERY FRAGMENTI ON DESTROY")
    }
}
