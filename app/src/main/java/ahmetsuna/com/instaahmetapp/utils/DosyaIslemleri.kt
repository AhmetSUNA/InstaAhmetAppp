package ahmetsuna.com.instaahmetapp.utils

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

    }
}