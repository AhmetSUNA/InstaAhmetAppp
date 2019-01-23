package ahmetsuna.com.instaahmetapp.Profile


import ahmetsuna.com.instaahmetapp.R
import ahmetsuna.com.instaahmetapp.utils.UniversalImageLoader
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_profile_edit.view.*


class ProfileEditFragment : Fragment() {

    lateinit var circliProfileImageFragment: CircleImageView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(ahmetsuna.com.instaahmetapp.R.layout.fragment_profile_edit, container, false)

        circliProfileImageFragment = view.findViewById(R.id.circleProfileImage)

        setupProfilePicture()

        view.imgClose.setOnClickListener {

            activity!!.onBackPressed()
        }

        return view
    }



    private fun setupProfilePicture() {
       //http@ //www.cultureblueprint.com/wp-content/uploads/2014/11/android_icon_by_gabrydesign-d4m7he9.png

        var imgURL = "worldofpcgames.co/wp-content/uploads/2016/09/android-apps.png"
        UniversalImageLoader.setImage(imgURL, circliProfileImageFragment, null,"https://")


    }


}
