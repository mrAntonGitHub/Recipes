package com.worldwidemobiledevelopment.recipes.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.worldwidemobiledevelopment.recipes.R
import com.worldwidemobiledevelopment.recipes.data.Comment
import com.worldwidemobiledevelopment.recipes.data.CookingStep
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.item_step.view.*


class CommentsAdapter(private val comments: List<Comment>, private val commentAction: CommentAction) :
    RecyclerView.Adapter<CommentsAdapter.ViewHolder>() {

    interface CommentAction {
        fun onCommentClicked(step: CookingStep)
    }

    override fun getItemCount(): Int = comments.count()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            if (comments[position].userAvatar != null){
                avatar.setImageURI(Uri.parse(comments[position].userAvatar))
            }else{
                avatar.setImageResource(R.drawable.ic_man)
            }
            name.text = comments[position].userName
            status.text = comments[position].userStatus
            rating.numStars = comments[position].rating
            comment.text = comments[position].commentText
        }
    }

    inner class ViewHolder(val item: View) : RecyclerView.ViewHolder(item) {
        val avatar : CircleImageView = item.findViewById(R.id.avatar)
        val name : TextView = item.findViewById(R.id.name)
        val status : TextView = item.findViewById(R.id.status)
        val rating : RatingBar = item.findViewById(R.id.rating)
        val comment : TextView = item.findViewById(R.id.comment)
    }
}