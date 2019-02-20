package ahmetsuna.com.instaahmetapp.Generic


import ahmetsuna.com.instaahmetapp.Models.Comments
import ahmetsuna.com.instaahmetapp.R
import ahmetsuna.com.instaahmetapp.utils.EventBusDataEvents
import android.content.Context
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_comment.view.*
import kotlinx.android.synthetic.main.tek_satir_comment_item.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class CommentFragment : Fragment() {

    var yorumYapilacakGonderininID : String?=null

    lateinit var myAuth: FirebaseAuth
    lateinit var myUser: FirebaseUser
    lateinit var myRef: DatabaseReference
    lateinit var myAdapter: FirebaseRecyclerAdapter<Comments, CommentViewHolder>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.fragment_comment, container, false)

        myAuth = FirebaseAuth.getInstance()
        myUser = myAuth.currentUser!!
        myRef = FirebaseDatabase.getInstance().reference.child("comments").child(yorumYapilacakGonderininID!!)


        //veri tabanında hangi düğümü dinleyip, hangi verileri getirip listeye atacağız, onunla ilgili verileri içerir
        val options = FirebaseRecyclerOptions.Builder<Comments>()
            .setQuery(myRef, Comments::class.java)
            .build()

        myAdapter =object : FirebaseRecyclerAdapter<Comments,CommentViewHolder>(options){

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {

                var commentViewHolder = inflater.inflate(R.layout.tek_satir_comment_item, parent, false)

                return CommentViewHolder(commentViewHolder)
            }

            override fun onBindViewHolder(holder: CommentViewHolder, position: Int, model: Comments) {

                holder.setData(model)

            }
        }

        view.yorumlarRecyclerView.adapter = myAdapter
        view.yorumlarRecyclerView.layoutManager = LinearLayoutManager(this!!.activity,LinearLayoutManager.VERTICAL, false)

        return view
    }

    class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tumCommentLayoutu = itemView as ConstraintLayout
        var yorumYapanUserPhoto = tumCommentLayoutu.yorumYapanUserProfile
        var kullaniciAdiveYorum = tumCommentLayoutu.tvUserNameAndYorum
        var yorumBegeni = tumCommentLayoutu.imgBegen
        var yorumSure = tumCommentLayoutu.tvYorumSure
        var yorumBegenmeSayisi = tumCommentLayoutu.tvBegenmeSayisi

        fun setData(oAnOlusturulanYorum: Comments) {

            kullaniciAdiveYorum.setText(oAnOlusturulanYorum.yorum)
            yorumSure.setText("" + oAnOlusturulanYorum.yorum_tarih)
            yorumBegenmeSayisi.setText(oAnOlusturulanYorum.yorum_begeni)

        }


    }
    ///////////////////////////////////EVENTBUS///////////////////////////////

    @Subscribe(sticky = true)
    internal fun onYorumYapilacakGonderi(gonderi: EventBusDataEvents.YorumYapilacakGonderininIDsiniGonder) {

        yorumYapilacakGonderininID = gonderi!!.gonderiID!!

    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        EventBus.getDefault().register(this)
    }

    override fun onDetach() {
        super.onDetach()
        EventBus.getDefault().unregister(this)
    }

    override fun onStart() {
        super.onStart()
        myAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        myAdapter.stopListening()
    }


}
