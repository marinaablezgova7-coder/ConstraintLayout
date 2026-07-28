package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ActionMenuView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post

typealias  LikeListenner = (Post) -> Unit
typealias ShareListenner = (Post) -> Unit
typealias ViewsListenner = (Post) -> Unit

class PostAdapter(
    private val lileClickListener: LikeListenner,
    private val shareClickListener: ShareListenner,
    private val viewsClickListener: ViewsListenner,
    ): ListAdapter<Post, PostViewHolder>(
    PostDiffItemColbek()
    ) {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PostViewHolder = PostViewHolder(
        CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        lileClickListener,
        shareClickListener,
        viewsClickListener,
        )

    override fun onBindViewHolder(
        holder: PostViewHolder,
        position: Int
    ) {
       holder.bind(getItem(position))
    }

}

class PostViewHolder(
    private val binding: CardPostBinding,
    private val lileClickListener: LikeListenner,
    private val shareClickListener: ShareListenner,
    private val viewsClickListener: ViewsListenner,
) : RecyclerView.ViewHolder(binding.root){
    fun bind(post: Post) {
        with(binding) {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            likeCount.text = post.likes.toString()
            shareCount.text = post.shares.toString()
            viewsCount.text = post.views.toString()

            likeIcon?.setImageResource(
                if (post.likedByMe)
                    R.drawable.ic_liked_24
                else
                    R.drawable.ic_like_24
            )

            likeIcon?.setOnClickListener {
                lileClickListener(post)
            }
            shareIcon?.setOnClickListener {
                shareClickListener(post)
            }
            viewsIcon?.setOnClickListener {
                viewsClickListener(post)
            }
        }
    }
}

class PostDiffItemColbek: DiffUtil.ItemCallback<Post>(){
    override fun areItemsTheSame(
        oldItem: Post,
        newItem: Post
    ): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: Post,
        newItem: Post
    ): Boolean = oldItem == newItem

    override fun getChangePayload(
        oldItem: Post,
        newItem: Post
    ): Any? = Unit

}