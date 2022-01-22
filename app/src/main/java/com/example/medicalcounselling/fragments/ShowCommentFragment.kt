package com.example.medicalcounselling.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.medicalcounselling.Authentication.AuthenticationPageActivity
import com.example.medicalcounselling.CommentAdapter
import com.example.medicalcounselling.Dao.CommentDao
import com.example.medicalcounselling.Dao.PostsDao
import com.example.medicalcounselling.PostsAdapter
import com.example.medicalcounselling.R
import com.example.medicalcounselling.databinding.FragmentHomeBinding
import com.example.medicalcounselling.databinding.FragmentShowCommentBinding
import com.example.medicalcounselling.models.Comment
import com.example.medicalcounselling.models.Posts
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query


class ShowCommentFragment : Fragment() {
    val args:ShowCommentFragmentArgs by navArgs()
    lateinit var postDao: PostsDao
    lateinit var commentDao: CommentDao
    lateinit var adapter: CommentAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding: FragmentShowCommentBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_show_comment, container, false)
        val postId = args.postId
        postDao = PostsDao()

        Log.d("msg",""+postId)
        val recyclerView =binding.recyclerView
        setUpRecyclerView(recyclerView,postId)
        return binding.root
    }
    override fun onStart(){
        super.onStart()
        adapter.startListening()
    }
    override fun onStop(){
        super.onStop()
        adapter.stopListening()
    }

    private fun setUpRecyclerView(recyclerView: RecyclerView,id:String) {
        commentDao = CommentDao()
        val db =commentDao.commentCollection
        val query: Query = db.whereEqualTo("postId",id).orderBy("createdAt", Query.Direction.DESCENDING)

        val recyclerOptions = FirestoreRecyclerOptions.Builder<Comment>().setQuery(query, Comment::class.java).build()

        adapter = CommentAdapter(recyclerOptions)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter =adapter

    }
}


