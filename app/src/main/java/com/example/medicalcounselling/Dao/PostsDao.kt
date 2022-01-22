package com.example.medicalcounselling.Dao

import android.util.Log
import com.example.medicalcounselling.models.Comment
import com.example.medicalcounselling.models.Posts
import com.example.medicalcounselling.models.Users
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import java.util.*
import kotlin.collections.ArrayList

class PostsDao {
    private val db = FirebaseFirestore.getInstance()
    val postCollection =db.collection("Posts")

    val auth= FirebaseAuth.getInstance()
    fun addPosts(text: String, disease: String, therapy: String){
        val userId:String=auth.currentUser!!.uid
        GlobalScope.launch(Dispatchers.IO) {
            val userDao=UsersDao()
            val users:Users= userDao.getUserById(userId).await().toObject(Users::class.java)!!
            val currentTime =System.currentTimeMillis()
            val uuid = UUID.randomUUID()
            val randomUUIDString = uuid.toString()
            val posts: Posts =Posts(randomUUIDString,text, users, disease, therapy, currentTime)
            postCollection.document().set(posts)
                    .addOnSuccessListener { Log.d("Post", "DocumentSnapshot successfully written!") }
                    .addOnFailureListener { e -> Log.w("Post", "Error writing document", e) }
        }

    }
    fun getPostById(postId: String): Task<DocumentSnapshot> {
        return postCollection.document(postId).get()
    }
    fun updateLikes(postId: String) {
        GlobalScope.launch {
            val currentUserId = auth.currentUser!!.uid
            val post = getPostById(postId).await().toObject(Posts::class.java)!!
            val isLiked = post.likedBy.contains(currentUserId)
            if(isLiked ){
                post.likedBy.remove(currentUserId)
            }
            else{
                post.likedBy.add(currentUserId)
            }
            postCollection.document(postId).set(post)
        }
    }

    fun addComment(postId: String, comment: String) {
        GlobalScope.launch {
            val currentUser = auth.currentUser!!
            val post = getPostById(postId).await().toObject(Posts::class.java)!!
            val currentTime =System.currentTimeMillis()
            currentUser.displayName?.let {
                Comment(
                        Users(currentUser.uid, it,currentUser.photoUrl.toString()),
                        postId,
                        comment,
                        currentTime
                )
            }?.let { post.comments.add(it) }
            postCollection.document(postId).set(post)
        }
    }


}


