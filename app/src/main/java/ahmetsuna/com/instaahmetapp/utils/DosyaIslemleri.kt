package ahmetsuna.com.instaahmetapp.utils

import ahmetsuna.com.instaahmetapp.Profile.YukleniyorFragment
import ahmetsuna.com.instaahmetapp.Share.ShareNextFragment
import android.os.AsyncTask
import android.os.Environment
import android.support.v4.app.Fragment
import android.util.Log
import com.iceteck.silicompressorr.SiliCompressor
import java.io.File
import java.util.*

class DosyaIslemleri {

    companion object {

        fun klasordekiDosyalariGetir(klasorAdi: String): ArrayList<String>{

            var tumDosyalar = ArrayList<String>()

            var file = File(klasorAdi)

            //parametre olarak klasordeki tum dosyalar alınır
            var klasordekiTumDosyalar = file.listFiles()

            //parametre olarak gönderdiğimin klasor yolunda eleman olup olmadığı kontrol edildi
            if (klasordekiTumDosyalar != null){

                //galeriden getirilen resimlerin tarihe göre sondan başa listelenmesi
                if (klasordekiTumDosyalar.size>1){

                    Arrays.sort(klasordekiTumDosyalar, object : Comparator<File>{
                        override fun compare(p0: File?, p1: File?): Int {

                            if (p0!!.lastModified() > p1!!.lastModified()){

                                return -1
                            }else{
                                return 1
                            }
                        }
                    })
                }

                for (i in 0..klasordekiTumDosyalar.size-1){

                    //sadece dosyalara bakılır
                    if (klasordekiTumDosyalar[i].isFile){

                        //Log.e("HATA", "okunan veri bir dosya")

                        //okuduğumuz dosyanın telefondaki yeri ve adını içerir
                        //files://root/logo.png
                        var okunanDosyaYolu = klasordekiTumDosyalar[i].absolutePath

                        //Log.e("HATA", "okunan dosya yolu" + okunanDosyaYolu)

                        var dosyaTuru = okunanDosyaYolu.substring(okunanDosyaYolu.lastIndexOf("."))

                        //Log.e("HATA", "dosya türü" + dosyaTuru)

                        if (dosyaTuru.equals(".jpg") || dosyaTuru.equals(".jpeg") || dosyaTuru.equals(".png") || dosyaTuru.equals(".mp4")){

                            tumDosyalar.add(okunanDosyaYolu)
                            //Log.e("HATA", "arrayListe eklenen dosya" + okunanDosyaYolu)
                        }
                    }
                }
            }

            return tumDosyalar


        }

        fun compressResimDosya(fragment: Fragment, secilenResimYolu: String?) {

            ResimCompressAsyncTask(fragment).execute(secilenResimYolu)

        }
    }

    internal class ResimCompressAsyncTask(fragment: Fragment):AsyncTask<String, String, String>(){

        var myFragment = fragment
        var compressFragment = YukleniyorFragment()

        override fun onPreExecute() {

            compressFragment.show(myFragment.activity!!.supportFragmentManager,"compressDialogBasladi")
            compressFragment.isCancelable = false
            super.onPreExecute()
        }

        override fun doInBackground(vararg params: String?): String {

            var yeniOlusanDosyaninKlasoru = File(Environment.getExternalStorageDirectory().absolutePath + "/DCIM/Ozel/compressed/")

            var yeniDosyaYolu =  SiliCompressor.with(myFragment.context).compress(params[0],yeniOlusanDosyaninKlasoru)

            //sıkıştırılırak oluşturulmuş yeni dosyanın yolunu verir
            return yeniDosyaYolu
        }

        override fun onPostExecute(filePath: String?) {

            Log.e("HATA", "yeni dosyanın pathi: " + filePath)
            compressFragment.dismiss()
            (myFragment as ShareNextFragment).uploadStorage(filePath)

            super.onPostExecute(filePath)
        }


    }
}