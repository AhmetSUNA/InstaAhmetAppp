package ahmetsuna.com.instaahmetapp.utils

import ahmetsuna.com.instaahmetapp.Models.Users

class EventBusDataEvents {

    internal class KayitBilgileriniGonder(var telNo: String?, var email: String?, var verificationID:String?, var code:String?, var emailKayit:Boolean)


    internal class KullaniciBilgileriniGonder(var kullanici: Users?)

    internal class PaylasilacakResmiGonder(var resimYolu: String?, var dosyaTuruResimmi: Boolean?)
}