package ahmetsuna.com.instaahmetapp.utils

import ahmetsuna.com.instaahmetapp.Models.UserPosts
import ahmetsuna.com.instaahmetapp.R
import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.tek_post_recycler_item.view.*
import java.util.*

class HomeFragmentRecyclerAdapter(var context: Context, var tumGonderiler: ArrayList<UserPosts>) :
    RecyclerView.Adapter<HomeFragmentRecyclerAdapter.MyViewHolder>() {

    init {

        Collections.sort(tumGonderiler, object : Comparator<UserPosts>{
            override fun compare(p0: UserPosts?, p1: UserPosts?): Int {

                if (p0!!.postYuklenmeTarih!! > p1!!.postYuklenmeTarih!!){
                    return -1
                }else
                    return 1
            }
        })
    }

    //listede kaçtane eleman olacağını geri döner
    override fun getItemCount(): Int {
        return tumGonderiler.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        var viewHolder = LayoutInflater.from(context).inflate(R.layout.tek_post_recycler_item, parent, false)

        return MyViewHolder(viewHolder)
    }

    //alanlara erişip gerekli veri atamalarının yapılması
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.setData(position, tumGonderiler.get(position))


    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tumLayout = itemView as ConstraintLayout
        var profileImage = tumLayout.imgUserProfile
        var userNameTitle = tumLayout.tvKullaniciAdiBaslik
        var gonderi = tumLayout.imgPostResim
        var userName = tumLayout.tvKullaniciAdi
        var gonderiYorum = tumLayout.tvPostAciklama
        var gonderiKacZamanOnce = tumLayout.tvKacZamanOnce

        fun setData(position: Int, oankiGonderi: UserPosts) {

            userNameTitle.setText(oankiGonderi.userName)
            UniversalImageLoader.setImage(oankiGonderi.postURL!!, gonderi, null, "")
            userName.setText(oankiGonderi.userName)
            gonderiYorum.setText(oankiGonderi.postAciklama)
            UniversalImageLoader.setImage(oankiGonderi.userFotoURL!!, profileImage, null, "")
            gonderiKacZamanOnce.setText(TimeAgo.getTimeAgo(oankiGonderi.postYuklenmeTarih!!))

        }

    }
}