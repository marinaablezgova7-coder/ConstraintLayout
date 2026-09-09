package ru.netology.nmedia.entity

import androidx.room3.Entity
import androidx.room3.PrimaryKey
import ru.netology.nmedia.dto.Post

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String,
    val published: String,
    val content: String,
    val likes: Int = 0,
    val shares: Int = 0,
    val views: Int = 0,
    val sharesByMe: Boolean = false,
    val viewsByMe: Boolean = false,
    val likedByMe: Boolean = false,
    val video: String? = null
) {

    fun toPost() = Post(
        id = id,
        author = author,
        published = published,
        content = content,
        likes = likes,
        shares = shares,
        views = views,
        sharesByMe = sharesByMe,
        viewsByMe = viewsByMe,
        likedByMe = likedByMe,
        video = video
    )

    companion object {
        fun fromDto(post: Post) = PostEntity(
            id = post.id,
            author = post.author,
            published = post.published,
            content = post.content,
            likes = post.likes,
            shares = post.shares,
            views = post.views,
            sharesByMe = post.sharesByMe,
            viewsByMe = post.viewsByMe,
            likedByMe = post.likedByMe,
            video = post.video
        )
    }
}
