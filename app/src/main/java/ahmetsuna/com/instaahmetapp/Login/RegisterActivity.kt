package ahmetsuna.com.instaahmetapp.Login

import ahmetsuna.com.instaahmetapp.R
import ahmetsuna.com.instaahmetapp.utils.EventBusDataEvents
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import kotlinx.android.synthetic.main.activity_register.*
import org.greenrobot.eventbus.EventBus

class RegisterActivity : AppCompatActivity(), FragmentManager.OnBackStackChangedListener {

    lateinit var manager:FragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        manager = supportFragmentManager
        manager.addOnBackStackChangedListener(this)

        init()
    }

    private fun init() {

        //e-posta ile giriş yapılacak ise
        tvEposta.setOnClickListener {

            viewTelefon.visibility = View.INVISIBLE
            viewEposta.visibility = View.VISIBLE
            etGirisYontemi.setText("")
            etGirisYontemi.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            etGirisYontemi.setHint("E-Posta")

            btnIleri.isEnabled = false
            btnIleri.setTextColor(ContextCompat.getColor(this@RegisterActivity,R.color.sonukmavi))
            btnIleri.setBackgroundResource(R.drawable.register_button)

        }
        //telno ile giriş yapılacak ise
        tvTelefon.setOnClickListener {

            viewTelefon.visibility = View.VISIBLE
            viewEposta.visibility = View.INVISIBLE
            etGirisYontemi.setText("")
            etGirisYontemi.inputType = InputType.TYPE_CLASS_NUMBER
            etGirisYontemi.setHint("Telefon")

            btnIleri.isEnabled = false
            btnIleri.setTextColor(ContextCompat.getColor(this@RegisterActivity,R.color.sonukmavi))
            btnIleri.setBackgroundResource(R.drawable.register_button)

        }

        //giriş yöntemi telno mu e-posta mı
        etGirisYontemi.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {



            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if(start+before+count >= 10){

                    btnIleri.isEnabled = true
                    btnIleri.setTextColor(ContextCompat.getColor(this@RegisterActivity,R.color.beyaz))
                    btnIleri.setBackgroundResource(R.drawable.register_button_aktif)
                }else{
                    btnIleri.isEnabled = false
                    btnIleri.setTextColor(ContextCompat.getColor(this@RegisterActivity,R.color.sonukmavi))
                    btnIleri.setBackgroundResource(R.drawable.register_button)
                }

            }
        })

        //telno veya e-posta girildikten sonra ileri butonuna basıldığında yapılacaklar
        btnIleri.setOnClickListener {

            if(etGirisYontemi.hint.toString().equals("Telefon")){

                loginRoot.visibility = View.GONE
                loginContainer.visibility = View.VISIBLE
                var transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.loginContainer, TelefonKoduGirFragment())
                transaction.addToBackStack("telefonKoduGirFragmenEklendi")
                transaction.commit()

                EventBus.getDefault().postSticky(EventBusDataEvents.TelefonNoGonder(etGirisYontemi.text.toString()))

            }else{
                loginRoot.visibility = View.GONE
                loginContainer.visibility = View.VISIBLE
                var transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.loginContainer, KayitFragment())
                transaction.addToBackStack("emailileGirisFragmenEklendi")
                transaction.commit()

                EventBus.getDefault().postSticky(EventBusDataEvents.EmailGonder(etGirisYontemi.text.toString()))
            }

        }

    }

    //Bir hata durumunda doğru biçimde adım adım geri gitme
    override fun onBackStackChanged() {
        val elemanSayisi = manager.backStackEntryCount

        if(elemanSayisi==0){
            loginRoot.visibility = View.VISIBLE
        }

    }

}
