package ru.netology.nmedia.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post

typealias LikeListenner = (Post) -> Unit
typealias ShareListenner = (Post) -> Unit
typealias ViewsListenner = (Post) -> Unit
typealias PostClickListener = (Post) -> Unit
typealias OnRemoveListenner = (Post) -> Unit
typealias OnEditListener = (Post) -> Unit

class PostAdapter(
    private val lileClickListener: LikeListenner,
    private val shareClickListener: ShareListenner,
    private val viewsClickListener: ViewsListenner,
    private val postClickListener: PostClickListener,
    private val onEditListener: OnEditListener,
    private val onRemoveListenner: OnRemoveListenner
) : ListAdapter<Post, PostViewHolder>(
    PostDiffItemColbek()
) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PostViewHolder = PostViewHolder(
        CardPostBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ),
        lileClickListener,
        shareClickListener,
        viewsClickListener,
        postClickListener,
        onRemoveListenner,
        onEditListener
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
    private val postClickListener: PostClickListener,
    private val onRemoveListenner: OnRemoveListenner,
    private val onEditListener: OnEditListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post) {

        with(binding) {

            author.text = post.author
            published.text = post.published
            content.text = post.content

            videoContainer.isVisible = !post.video.isNullOrBlank()

            if (!post.video.isNullOrBlank()) {

                val videoIntent = Intent(
                    Intent.ACTION_VIEW,
                    post.video.toUri()
                )

                videoContainer.setOnClickListener {
                    itemView.context.startActivity(videoIntent)
                }

                videoPlay.setOnClickListener {
                    itemView.context.startActivity(videoIntent)
                }

            } else {
                videoContainer.setOnClickListener(null)
                videoPlay.setOnClickListener(null)
            }

            shareIcon.text = post.shares.toString()
            viewsCount.text = post.views.toString()

            likeIcon.isChecked = post.likedByMe
            likeIcon.text = post.likes.toString()

            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {

                    inflate(R.menu.post_menu)

                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {

                            R.id.remove -> {
                                onRemoveListenner(post)
                                true
                            }

                            R.id.edit -> {
                                onEditListener(post)
                                true
                            }

                            else -> false
                        }
                    }

                    show()
                }
            }

            likeIcon.setOnClickListener {
                lileClickListener(post)
            }

            shareIcon.setOnClickListener {
                shareClickListener(post)
            }

            viewsIcon.setOnClickListener {
                viewsClickListener(post)
            }

            // Клик по самой карточке
            itemView.setOnClickListener {
                postClickListener(post)
            }
        }
    }
}

class PostDiffItemColbek : DiffUtil.ItemCallback<Post>() {

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
