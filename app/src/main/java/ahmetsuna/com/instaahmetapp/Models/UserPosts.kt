package ahmetsuna.com.instaahmetapp.Models

class UserPosts {

    var userID: String? = null
    var userName: String? = null
    var userFotoURL: String? = null
    var postID: String? = null
    var postAciklama: String? = null
    var postURL: String? = null
    var postYuklenmeTarih: Long? = null

    constructor(
        userID: String?,
        userName: String?,
        userFotoURL: String?,
        postID: String?,
        postAciklama: String?,
        postURL: String?,
        postYuklenmeTarih: Long?
    ) {
        this.userID = userID
        this.userName = userName
        this.userFotoURL = userFotoURL
        this.postID = postID
        this.postAciklama = postAciklama
        this.postURL = postURL
        this.postYuklenmeTarih = postYuklenmeTarih
    }

    constructor()

    override fun toString(): String {
        return "UserPosts(userID=$userID, userName=$userName, userFotoURL=$userFotoURL, postID=$postID, postAciklama=$postAciklama, postURL=$postURL, postYuklenmeTarih=$postYuklenmeTarih)"
    }


}