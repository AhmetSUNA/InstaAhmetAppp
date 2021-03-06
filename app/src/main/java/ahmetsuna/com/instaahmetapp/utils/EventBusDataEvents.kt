package ahmetsuna.com.instaahmetapp.utils

import ahmetsuna.com.instaahmetapp.Models.Users

class EventBusDataEvents {

    internal class KayitBilgileriniGonder(var telNo: String?, var email: String?, var verificationID:String?, var code:String?, var emailKayit:Boolean)


    internal class KullaniciBilgileriniGonder(var kullanici: Users?)

    internal class PaylasilacakResmiGonder(var dosyaYolu: String?, var dosyaTuruResimmi: Boolean?)

    internal class GaleriSecilenDosyaYoluGonder(var dosyaYolu: String?)

    internal class KameraIzinBilgisiGonder(var kameraIzniVerildiMi: Boolean?)

    internal class YorumYapilacakGonderininIDsiniGonder(var gonderiID: String?)
}