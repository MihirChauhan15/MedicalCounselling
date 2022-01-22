package com.example.medicalcounselling.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.medicalcounselling.Dao.PostsDao
import com.example.medicalcounselling.R
import com.example.medicalcounselling.databinding.FragmentPostCreateBinding
import java.util.*


class CreatePostsFragment : Fragment() {
   lateinit var postDao:PostsDao
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentPostCreateBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_post_create, container, false)


        postDao = PostsDao()
        binding.post.setOnClickListener {
            val text = binding.postText.text.toString().trim()
            val disease = binding.diseasesText.text.toString().trim()
            val therapy = binding.therapyText.text.toString().trim()
            if(text.isNotEmpty() && disease.isNotEmpty() && therapy.isNotEmpty()){
                postDao.addPosts(text, disease, therapy)
            }
            else{
                Toast.makeText(context, "Please fill out necessary details", Toast.LENGTH_SHORT).show()
            }
        }
        return binding.root
    }

}