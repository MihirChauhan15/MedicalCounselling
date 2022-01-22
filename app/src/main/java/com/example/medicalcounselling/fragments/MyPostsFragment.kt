package com.example.medicalcounselling.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.medicalcounselling.Authentication.AuthenticationPageActivity
import com.example.medicalcounselling.Dao.CommentDao
import com.example.medicalcounselling.Dao.PostsDao
import com.example.medicalcounselling.PostsAdapter
import com.example.medicalcounselling.R
import com.example.medicalcounselling.databinding.FragmentMyPostsBinding
import com.example.medicalcounselling.models.Posts
import com.example.medicalcounselling.models.Users
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query


class MyPostsFragment : Fragment(),PostsAdapter.IPostAdapter {
    lateinit var auth: FirebaseAuth
    lateinit var adapter: PostsAdapter
    lateinit var postDao: PostsDao
    lateinit var commentDao: CommentDao
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding: FragmentMyPostsBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_my_posts, container, false)
        auth = FirebaseAuth.getInstance()
        val recyclerView =binding.recyclerView

        setUpRecyclerView(recyclerView)

        return binding.root
    }

    override fun onStart(){
        super.onStart()
        if(auth.currentUser==null){
            startActivity(Intent(context, AuthenticationPageActivity::class.java))
        }
        adapter.startListening()
    }
    override fun onStop(){
        super.onStop()
        adapter.stopListening()
    }
    private fun setUpRecyclerView(recyclerView: RecyclerView) {
        postDao = PostsDao()
        val postCollection =postDao.postCollection

        val user = Users(auth.currentUser!!.uid,auth.currentUser!!.displayName.toString(),auth.currentUser!!.photoUrl.toString())
        Log.d("msg",""+user)
        val query: Query =postCollection.orderBy("createdAt", Query.Direction.DESCENDING).whereEqualTo("createdBy",user)

        val recyclerOptions = FirestoreRecyclerOptions.Builder<Posts>().setQuery(query, Posts::class.java).build()

        adapter = PostsAdapter(recyclerOptions,this)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter =adapter

    }

    override fun OnClickLikeButton(postId: String) {
        postDao.updateLikes(postId)
    }

    override fun OnClickAddComment(postId: String,comment: String) {
        commentDao=CommentDao()
        postDao.addComment(postId,comment)
        commentDao.addComment(postId,comment)
    }

    override fun OnClickShowComment(postId: String) {
        val bundle =Bundle().apply {
            putString("postId",postId)
        }

        findNavController().navigate(R.id.action_myPostsFragment_to_showCommentFragment,bundle)
    }



}