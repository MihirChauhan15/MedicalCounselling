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
import com.example.medicalcounselling.databinding.FragmentDiseaseBinding
import com.example.medicalcounselling.databinding.FragmentMyPostsBinding
import com.example.medicalcounselling.models.Posts
import com.example.medicalcounselling.models.Users
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query


class DiseaseFragment : Fragment(),PostsAdapter.IPostAdapter {
    lateinit var auth: FirebaseAuth
    lateinit var adapter: PostsAdapter
    lateinit var postDao: PostsDao
    lateinit var commentDao: CommentDao

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentDiseaseBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_disease, container, false)
        auth = FirebaseAuth.getInstance()
        postDao = PostsDao()
        val db = FirebaseFirestore.getInstance();
        val postCollection = db.collection("Posts")
        var query: Query =postCollection.orderBy("createdAt", Query.Direction.DESCENDING);
        val recyclerView =binding.recyclerView
        binding.imageView4.setOnClickListener {
            if(binding.editTextTextPersonName.text.toString()!=""){
                val search = binding.editTextTextPersonName.text.toString();
                Log.d("msg",search);
                query = postCollection.whereEqualTo("disease",search);
                setUpRecyclerView(recyclerView,query);
                onStop();
                onStart();
            }
        }
        setUpRecyclerView(recyclerView,query);
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
    private fun setUpRecyclerView(recyclerView: RecyclerView,query: Query) {
        val recyclerOptions = FirestoreRecyclerOptions.Builder<Posts>().setQuery(query, Posts::class.java).build();
        adapter = PostsAdapter(recyclerOptions,this)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter =adapter

    }

    override fun OnClickLikeButton(postId: String) {
        postDao.updateLikes(postId)
    }

    override fun OnClickAddComment(postId: String,comment: String) {
        commentDao= CommentDao()
        postDao.addComment(postId,comment)
        commentDao.addComment(postId,comment)
    }

    override fun OnClickShowComment(postId: String) {
        val bundle =Bundle().apply {
            putString("postId",postId)
        }

        findNavController().navigate(R.id.action_diseaseFragment_to_showCommentFragment,bundle)
    }

}