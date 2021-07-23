package com.example.medicalcounselling.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.example.medicalcounselling.Dao.PostsDao
import com.example.medicalcounselling.R
import com.example.medicalcounselling.databinding.FragmentCreatePostsBinding



class CreatePostsFragment : Fragment() {
   lateinit var postDao:PostsDao
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentCreatePostsBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_create_posts, container, false)


        postDao = PostsDao()
        binding.post.setOnClickListener {
            val text = binding.postText.text.toString().trim()
            val disease = binding.diseasesText.text.toString().trim()
            val therapy = binding.therapyText.text.toString().trim()
            Toast.makeText(context,text+disease+therapy, Toast.LENGTH_SHORT).show()
            if(text.isNotEmpty() && disease.isNotEmpty() && therapy.isNotEmpty()){
                postDao.addPosts(text,disease,therapy)
            }
            else{
                Toast.makeText(context,"Please fill out necessary details", Toast.LENGTH_SHORT).show()
            }
        }
        return binding.root
    }

}