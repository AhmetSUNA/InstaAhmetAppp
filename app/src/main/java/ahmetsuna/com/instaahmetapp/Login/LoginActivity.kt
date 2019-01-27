package ahmetsuna.com.instaahmetapp.Login

import ahmetsuna.com.instaahmetapp.R
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        init()



    }

     fun init() {

         tvKaydol.setOnClickListener {

             var intent = Intent(this,RegisterActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
             startActivity(intent)
         }

         etEmailTelOrUserName.addTextChangedListener(watcher)
         etSifre.addTextChangedListener(watcher)

    }

    var watcher : TextWatcher = object : TextWatcher{
        override fun afterTextChanged(p0: Editable?) {

        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            if (etEmailTelOrUserName.text.toString().length >= 6 && etSifre.text.toString().length >=6){

                btnGirisYap.isEnabled = true
                btnGirisYap.setTextColor(ContextCompat.getColor(this@LoginActivity, R.color.beyaz))
                btnGirisYap.setBackgroundResource(R.drawable.register_button_aktif)

            }else{
                btnGirisYap.isEnabled = false
                btnGirisYap.setTextColor(ContextCompat.getColor(this@LoginActivity, R.color.sonukmavi))
                btnGirisYap.setBackgroundResource(R.drawable.register_button)
            }

        }

    }
}
