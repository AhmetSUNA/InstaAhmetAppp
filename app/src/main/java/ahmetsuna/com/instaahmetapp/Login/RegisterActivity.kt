package ahmetsuna.com.instaahmetapp.Login

import ahmetsuna.com.instaahmetapp.Home.HomeActivity
import ahmetsuna.com.instaahmetapp.Models.Users
import ahmetsuna.com.instaahmetapp.R
import ahmetsuna.com.instaahmetapp.utils.EventBusDataEvents
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_register.*
import org.greenrobot.eventbus.EventBus

class RegisterActivity : AppCompatActivity(), FragmentManager.OnBackStackChangedListener {

    lateinit var manager: FragmentManager
    lateinit var myRef: DatabaseReference
    lateinit var myAuth: FirebaseAuth
    lateinit var myAuthListener: FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        setupAuthListener()

        myAuth = FirebaseAuth.getInstance()
        myRef = FirebaseDatabase.getInstance().reference

        manager = supportFragmentManager
        manager.addOnBackStackChangedListener(this)

        init()

    }

    private fun init() {

        tvGirisYap.setOnClickListener {

            var intent = Intent(this@RegisterActivity, LoginActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            finish()

        }


        //e-posta ile giriş yapılacak ise
        tvEposta.setOnClickListener {

            viewTelefon.visibility = View.INVISIBLE
            viewEposta.visibility = View.VISIBLE
            etGirisYontemi.setText("")
            etGirisYontemi.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            etGirisYontemi.setHint("E-Posta")

            btnIleri.isEnabled = false
            btnIleri.setTextColor(ContextCompat.getColor(this@RegisterActivity, R.color.sonukmavi))
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
            btnIleri.setTextColor(ContextCompat.getColor(this@RegisterActivity, R.color.sonukmavi))
            btnIleri.setBackgroundResource(R.drawable.register_button)

        }

        //giriş yöntemi telno mu e-posta mı
        etGirisYontemi.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if (s!!.length >= 10) {

                    btnIleri.isEnabled = true
                    btnIleri.setTextColor(ContextCompat.getColor(this@RegisterActivity, R.color.beyaz))
                    btnIleri.setBackgroundResource(R.drawable.register_button_aktif)
                } else {
                    btnIleri.isEnabled = false
                    btnIleri.setTextColor(ContextCompat.getColor(this@RegisterActivity, R.color.sonukmavi))
                    btnIleri.setBackgroundResource(R.drawable.register_button)
                }

            }
        })

        //telno veya e-posta girildikten sonra ileri butonuna basıldığında yapılacaklar
        btnIleri.setOnClickListener {

            if (etGirisYontemi.hint.toString().equals("Telefon")) {

                if (isValidTelefon((etGirisYontemi.text.toString()))) {

                    var cepTelefonuKullanimdami = false

                    myRef.child("users").addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {

                        }
                        //telno veri tabanında var ise telno kontrolü
                        override fun onDataChange(p0: DataSnapshot) {

                            if (p0!!.getValue() != null) {

                                for (user in p0!!.children) {

                                    var okunanKullanici = user.getValue(Users::class.java)
                                    if (okunanKullanici!!.phone_number!!.equals(etGirisYontemi.text.toString())) {
                                        Toast.makeText(this@RegisterActivity, "Telefon numarası kullanımda", Toast.LENGTH_SHORT).show()
                                        cepTelefonuKullanimdami = true
                                        break
                                    }
                                }
                                if (cepTelefonuKullanimdami == false) {
                                    loginRoot.visibility = View.GONE
                                    loginContainer.visibility = View.VISIBLE
                                    var transaction = supportFragmentManager.beginTransaction()
                                    transaction.replace(R.id.loginContainer, TelefonKoduGirFragment())
                                    transaction.addToBackStack("telefonKoduGirFragmenEklendi")
                                    transaction.commit()
                                    EventBus.getDefault().postSticky(
                                        EventBusDataEvents.KayitBilgileriniGonder(
                                            etGirisYontemi.text.toString(), null, null, null, false
                                        )
                                    )

                                }
                            }

                        }

                    })

                } else {
                    Toast.makeText(this, "Lütfen geçerli bir telefon numarası giriniz", Toast.LENGTH_SHORT).show()
                }

            } else {
                if (isValidEmail((etGirisYontemi.text.toString()))) {

                    var emailKullanimdami = false

                    myRef.child("users").addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {

                        }
                        //email veri tabanında var ise email kontrolü
                        override fun onDataChange(p0: DataSnapshot) {

                            if (p0.getValue() != null) {

                                for (user in p0.children) {

                                    var okunanKullanici = user.getValue(Users::class.java)
                                    if (okunanKullanici!!.email.equals(etGirisYontemi.text.toString())) {
                                        Toast.makeText(this@RegisterActivity, "Email Kullanımda", Toast.LENGTH_SHORT)
                                            .show()
                                        emailKullanimdami = true
                                        break
                                    }
                                }

                                if (emailKullanimdami == false) {

                                    loginRoot.visibility = View.GONE
                                    loginContainer.visibility = View.VISIBLE
                                    var transaction = supportFragmentManager.beginTransaction()
                                    transaction.replace(R.id.loginContainer, KayitFragment())
                                    transaction.addToBackStack("emailileGirisFragmenEklendi")
                                    transaction.commit()
                                    EventBus.getDefault().postSticky(
                                        EventBusDataEvents.KayitBilgileriniGonder(
                                            null, etGirisYontemi.text.toString(), null, null, true
                                        )
                                    )

                                }
                            }

                        }

                    })
                } else {
                    Toast.makeText(this, "Lütfen geçerli bir E-mail adresi giriniz", Toast.LENGTH_SHORT).show()
                }
            }

        }

    }

    //Bir hata durumunda doğru biçimde adım adım geri gitme
    override fun onBackStackChanged() {
        val elemanSayisi = manager.backStackEntryCount

        if (elemanSayisi == 0) {
            loginRoot.visibility = View.VISIBLE
        }

    }

    fun isValidEmail(kontrolEdilecekMail: String): Boolean {

        if (kontrolEdilecekMail == null) {
            return false
        }
        return android.util.Patterns.EMAIL_ADDRESS.matcher(kontrolEdilecekMail).matches()

    }

    fun isValidTelefon(kontrolEdilecekTelefon: String): Boolean {

        if (kontrolEdilecekTelefon == null || kontrolEdilecekTelefon.length > 14) {
            return false
        }
        return android.util.Patterns.PHONE.matcher(kontrolEdilecekTelefon).matches()

    }

    private fun setupAuthListener() {
        myAuthListener = object : FirebaseAuth.AuthStateListener{
            override fun onAuthStateChanged(p0: FirebaseAuth) {
                var user = FirebaseAuth.getInstance().currentUser

                if (user != null){

                    var intent = Intent(this@RegisterActivity, HomeActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    finish()
                }else{

                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        myAuth.addAuthStateListener(myAuthListener)
    }

    override fun onStop() {
        super.onStop()
        if (myAuthListener != null){
            myAuth.removeAuthStateListener(myAuthListener)
        }
    }

}
