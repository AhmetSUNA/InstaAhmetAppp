package ahmetsuna.com.instaahmetapp.Share

import ahmetsuna.com.instaahmetapp.Login.LoginActivity
import ahmetsuna.com.instaahmetapp.R
import ahmetsuna.com.instaahmetapp.utils.SharePagerAdapter
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.DexterError
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.PermissionRequestErrorListener
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_share.*

class ShareActivity : AppCompatActivity() {

    private val ACTIVITY_NO=2
    private val TAG="ShareActivity"

    lateinit var myAuthListener: FirebaseAuth.AuthStateListener


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)

        setupAuthListener()

        storageVeKameraIzniIste()

    }

    //kullanıcıdan izin isteme
    private fun storageVeKameraIzniIste() {

        Dexter.withActivity(this)
            .withPermissions(android.Manifest.permission.READ_EXTERNAL_STORAGE,
                             android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                             android.Manifest.permission.CAMERA)

            .withListener(object : MultiplePermissionsListener{
                //kullanıcının izin verme işleminde çalışır duruma geçer
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {

                    //bütün izinler verildiyse burası tetiklenir
                    if (report!!.areAllPermissionsGranted()){
                        Log.e("HATA", "tüm izinler verilmiş")
                        setupShareViewPager()
                    }
                    //kullanıcının izini birdaha sorma demesi
                    if (report!!.isAnyPermissionPermanentlyDenied){
                        Log.e("HATA", "izinlerden birini bidaha sorma denmiş")

                        var builder = AlertDialog.Builder(this@ShareActivity)
                        builder.setTitle("İzin Gerekli")
                        builder.setMessage("Ayarlar kısmından uygulamaya izin vermeniz gerekiyor.")
                        builder.setPositiveButton("AYARLARA GİT", object : DialogInterface.OnClickListener{
                            override fun onClick(dialog: DialogInterface?, wich: Int) {
                                dialog!!.cancel()
                                var intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                var uri = Uri.fromParts("package", packageName, null)
                                intent.setData(uri)
                                startActivity(intent)
                                finish()
                            }
                        })
                        builder.setNegativeButton("REDDET", object : DialogInterface.OnClickListener{
                            override fun onClick(dialog: DialogInterface?, wich: Int) {
                                dialog!!.cancel()
                                finish()
                            }

                        })
                        builder.show()
                    }
                }
                //izin verme işleminde kullanıcıyı bilgilendireceğimiz kısım
                override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>?, token: PermissionToken?) {

                    Log.e("HATA", "izinlerden biri red edilmiş, kullanıcıyı ikna et")

                    var builder = AlertDialog.Builder(this@ShareActivity)
                    builder.setTitle("İzin Gerekli")
                    builder.setMessage("Uygulamaya izin vermeniz gerekiyor.")
                    builder.setPositiveButton("İZİN VER", object : DialogInterface.OnClickListener{
                        override fun onClick(dialog: DialogInterface?, wich: Int) {
                            dialog!!.cancel()
                            token!!.continuePermissionRequest()
                        }
                    })
                    builder.setNegativeButton("REDDET", object : DialogInterface.OnClickListener{
                        override fun onClick(dialog: DialogInterface?, wich: Int) {
                            dialog!!.cancel()
                            token!!.cancelPermissionRequest()
                            finish()
                        }

                    })
                    builder.show()
                }
            })
            .withErrorListener(object : PermissionRequestErrorListener{
                override fun onError(error: DexterError?) {
                    Log.e("HATA", error!!.toString())
                }
            }).check()

    }

    private fun setupShareViewPager() {

        var tabAdlari = ArrayList<String>()

        tabAdlari.add("GALERİ")
        tabAdlari.add("KAMERA")
        tabAdlari.add("VİDEO")

        var sharePagerAdapter = SharePagerAdapter(supportFragmentManager, tabAdlari)
        sharePagerAdapter.addFragment(ShareGalleryFragment())
        sharePagerAdapter.addFragment(ShareCameraFragment())
        sharePagerAdapter.addFragment(ShareVideoFragment())

        shareViewPager.adapter = sharePagerAdapter

        shareTabLayout.setupWithViewPager(shareViewPager)

    }

    override fun onBackPressed() {

        anaLayout.visibility = View.VISIBLE
        fragmenContainerLayout.visibility = View.GONE
        super.onBackPressed()
    }

    private fun setupAuthListener() {

        //bu tetiklenmeden buna ulaşmaya çalışırsak nullPointException çıkar dikkat!!!
        myAuthListener = object : FirebaseAuth.AuthStateListener {
            override fun onAuthStateChanged(p0: FirebaseAuth) {
                var user = FirebaseAuth.getInstance().currentUser

                //kullanıcı null ise
                if (user == null) {
                    //çıkış yapıldığında loginActivity'e yolla
                    var intent = Intent(this@ShareActivity, LoginActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    finish()
                } else {


                }
            }

        }
    }

}
