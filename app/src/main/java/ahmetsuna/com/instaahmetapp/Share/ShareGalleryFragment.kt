package ahmetsuna.com.instaahmetapp.Share


import ahmetsuna.com.instaahmetapp.R
import ahmetsuna.com.instaahmetapp.utils.DosyaIslemleri
import ahmetsuna.com.instaahmetapp.utils.ShareActivityGridViewAdapter
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.fragment_share_gallery.*
import kotlinx.android.synthetic.main.fragment_share_gallery.view.*

class ShareGalleryFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.fragment_share_gallery, container, false)

        var klasorPaths = ArrayList<String>()

        var klasorAdlari = ArrayList<String>()

        var root = Environment.getExternalStorageDirectory().path
        var kameraResimleri = root + "/DCIM/Camera"
        var indirilenResimler = root + "/Download"
        var whatsappResimleri = root + "/WhatsApp/Media/WhatsApp Images"

        klasorPaths.add(kameraResimleri)
        klasorPaths.add(indirilenResimler)
        klasorPaths.add(whatsappResimleri)

        klasorAdlari.add("Kamera")
        klasorAdlari.add("İndirilenler")
        klasorAdlari.add("WhatsApp")

        var spinnerArrayAdapter = ArrayAdapter(activity, android.R.layout.simple_spinner_item, klasorAdlari)
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        view.spnKlasorAdlari.adapter = spinnerArrayAdapter

        view.spnKlasorAdlari.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var klasordekiDosyalar = DosyaIslemleri.klasordekiDosyalariGetir(klasorPaths.get(position))

                var gridViewAdapter = ShareActivityGridViewAdapter(activity!!, R.layout.tek_sutun_grid_resim, klasordekiDosyalar)

                gridResimler.adapter = gridViewAdapter

                /*for (str in klasordekiDosyalar){
                    Log.e("HATA",str)
                }*/
            }
        }




        return view
    }
}
