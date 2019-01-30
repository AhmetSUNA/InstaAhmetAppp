package ahmetsuna.com.instaahmetapp.Login

import ahmetsuna.com.instaahmetapp.Models.Users
import ahmetsuna.com.instaahmetapp.R
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    lateinit var myAuth: FirebaseAuth
    lateinit var myRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        myAuth = FirebaseAuth.getInstance()
        myRef = FirebaseDatabase.getInstance().reference
        init()

    }

    fun init() {

        tvKaydol.setOnClickListener {

            var intent = Intent(this, RegisterActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }

        etEmailTelOrUserName.addTextChangedListener(watcher)
        etSifre.addTextChangedListener(watcher)

        btnGirisYap.setOnClickListener {

            oturumAcacakKullaniciyiDenetle(etEmailTelOrUserName.text.toString(), etSifre.text.toString())

        }

    }

    private fun oturumAcacakKullaniciyiDenetle(emailPhoneNumberUserName: String, sifre: String) {

        var kullaniciBulundu = false

        myRef.child("users").orderByChild("email").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {


            }

            override fun onDataChange(p0: DataSnapshot) {
                for (ds in p0.children) {

                    var okunanKullanici = ds.getValue(Users::class.java)

                    if (okunanKullanici!!.email!!.toString().equals(emailPhoneNumberUserName)) {

                        oturumAc(okunanKullanici, sifre, false)
                        kullaniciBulundu = true
                        break

                    } else if (okunanKullanici.user_name!!.toString().equals(emailPhoneNumberUserName)) {
                        oturumAc(okunanKullanici, sifre, false)
                        kullaniciBulundu = true
                        break

                    } else if (okunanKullanici.phone_number!!.toString().equals(emailPhoneNumberUserName)) {
                        oturumAc(okunanKullanici, sifre, true)
                        kullaniciBulundu = true
                        break

                    }
                }
                if (kullaniciBulundu == false){
                    Toast.makeText(this@LoginActivity, "Kullanıcı bulunamadı", Toast.LENGTH_SHORT).show()
                }
            }
        })

    }

    private fun oturumAc(okunanKullanici: Users, sifre: String, telefonIleGiris: Boolean) {

        var girisYApacakEmail = ""

        if (telefonIleGiris == true) {

            girisYApacakEmail = okunanKullanici.email_phone_number.toString()
        } else {
            girisYApacakEmail = okunanKullanici.email.toString()
        }

        myAuth.signInWithEmailAndPassword(girisYApacakEmail, sifre)
            .addOnCompleteListener(object : OnCompleteListener<AuthResult> {
                override fun onComplete(p0: Task<AuthResult>) {

                    if (p0.isSuccessful) {
                        Toast.makeText(
                            this@LoginActivity,
                            "Oturum açıldı" + myAuth.currentUser!!.uid,
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(this@LoginActivity, "Kullanıcı Adı/ Şifre Hatalı", Toast.LENGTH_SHORT).show()
                    }

                }

            })

    }

    var watcher: TextWatcher = object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {

        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            if (etEmailTelOrUserName.text.toString().length >= 6 && etSifre.text.toString().length >= 6) {

                btnGirisYap.isEnabled = true
                btnGirisYap.setTextColor(ContextCompat.getColor(this@LoginActivity, R.color.beyaz))
                btnGirisYap.setBackgroundResource(R.drawable.register_button_aktif)

            } else {
                btnGirisYap.isEnabled = false
                btnGirisYap.setTextColor(ContextCompat.getColor(this@LoginActivity, R.color.sonukmavi))
                btnGirisYap.setBackgroundResource(R.drawable.register_button)
            }

        }

    }
}
