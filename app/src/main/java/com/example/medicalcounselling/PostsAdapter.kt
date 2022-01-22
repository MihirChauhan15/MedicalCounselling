package com.example.medicalcounselling

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.medicalcounselling.Constants.Utils
import com.example.medicalcounselling.fragments.HomeFragment
import com.example.medicalcounselling.models.Posts
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class PostsAdapter(options: FirestoreRecyclerOptions<Posts>, val listener: IPostAdapter): FirestoreRecyclerAdapter<Posts, PostsAdapter.PostViewHolder>(options) {
    class PostViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val postText: TextView = itemView.findViewById(R.id.postText)
        val userText: TextView = itemView.findViewById(R.id.userText)
        val createdAt: TextView = itemView.findViewById(R.id.createdAt)
        val userImage: ImageView = itemView.findViewById(R.id.userImage)
        val likedBy: TextView = itemView.findViewById(R.id.likedByCount)
        val likedButton: ImageView =itemView.findViewById(R.id.imageView3)
        val diseasesText:TextView =itemView.findViewById(R.id.diseasesText)
        val therapyText:TextView = itemView.findViewById(R.id.therapyText)
        val addComment:ImageView =itemView.findViewById(R.id.addComment);
        val comment:EditText =itemView.findViewById(R.id.comment)
        val commentCount:TextView =itemView.findViewById(R.id.commentCount)
        val showComment:TextView = itemView.findViewById(R.id.showComment)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsAdapter.PostViewHolder {
        val viewHolder = PostViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.post_item,parent,false))
        viewHolder.likedButton.setOnClickListener{
            listener.OnClickLikeButton(snapshots.getSnapshot(viewHolder.adapterPosition).id)
        }
        viewHolder.addComment.setOnClickListener {
            listener.OnClickAddComment(snapshots.getSnapshot(viewHolder.adapterPosition).id,viewHolder.comment.editableText.toString())
            viewHolder.comment.setText("")
        }
        viewHolder.showComment.setOnClickListener {
            listener.OnClickShowComment(snapshots.getSnapshot(viewHolder.adapterPosition).id)
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: PostsAdapter.PostViewHolder, position: Int, model: Posts) {
        holder.postText.text = model.text
        holder.userText.text =model.createdBy.displayName
        holder.createdAt.text = Utils.getTimeAgo(model.createdAt)
        holder.diseasesText.text = model.disease
        holder.therapyText.text = model.therapy
        holder.likedBy.text = model.likedBy.size.toString()
        holder.commentCount.text=model.comments?.size.toString()
        Glide.with(holder.userImage.context).load(model.createdBy.imageUrl).circleCrop().into(holder.userImage)

        val currUserId = Firebase.auth.currentUser!!.uid
        val isLiked = model.likedBy.contains(currUserId)
        if(isLiked){
            holder.likedButton.setImageDrawable(ContextCompat.getDrawable(holder.likedButton.context,R.drawable.ic_baseline_favorite_24))
        }
        else{
            holder.likedButton.setImageDrawable(ContextCompat.getDrawable(holder.likedButton.context,R.drawable.ic_baseline_favorite_border_24))
        }
    }

    interface IPostAdapter{
        fun OnClickLikeButton(postId:String)
        fun OnClickAddComment(postId: String,comment:String)
        fun OnClickShowComment(postId: String)
    }
}



