package ahmetsuna.com.instaahmetapp.Profile


import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth


class SignOutFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        var alert = AlertDialog.Builder(activity!!)
            .setTitle("Instagram'dan Çıkış Yap")
            .setMessage("Emin misiniz?")
            .setPositiveButton("Çıkış Yap", object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {

                    FirebaseAuth.getInstance().signOut()
                    activity!!.finish()

                }
            })
            .setNegativeButton("İptal", object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {

                    dismiss()

                }
            })
            .create()

        return alert

    }
}
