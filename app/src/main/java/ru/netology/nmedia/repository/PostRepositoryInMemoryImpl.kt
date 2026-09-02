package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post

class PostRepositoryInMemoryImpl : PostRepository {

    private var posts: List<Post> = emptyList()
        set(value) {
            field = value
            data.value = value
        }

    private var nextId = 1L

    private val data = MutableLiveData<List<Post>>(posts)

    override fun get(): LiveData<List<Post>> = data

    override fun getAll(): LiveData<List<Post>> = data

    override fun likeById(id: Long) {
        posts = posts.map { post ->
            if (post.id == id) {
                post.copy(
                    likedByMe = !post.likedByMe,
                    likes = if (post.likedByMe) {
                        post.likes - 1
                    } else {
                        post.likes + 1
                    }
                )
            } else {
                post
            }
        }
    }

    override fun shareById(id: Long) {
        posts = posts.map { post ->
            if (post.id == id) {
                post.copy(
                    shares = post.shares + 1
                )
            } else {
                post
            }
        }
    }

    override fun viewsById(id: Long) {
        posts = posts.map { post ->
            if (post.id == id) {
                post.copy(
                    views = post.views + 1
                )
            } else {
                post
            }
        }
    }

    override fun removeById(id: Long) {
        posts = posts.filterNot { it.id == id }
    }

    override fun save(post: Post) {
        if (post.id == 0L) {
            val newPost = post.copy(
                id = nextId++,
                author = "Me",
                published = "Now",
                likes = 0,
                shares = 0,
                views = 0,
                likedByMe = false,
                sharesByMe = false
            )

            posts = listOf(newPost) + posts
        } else {
            posts = posts.map {
                if (it.id == post.id) post else it
            }
        }
    }
}

