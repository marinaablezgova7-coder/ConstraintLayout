package ru.netology.nmedia.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.nmedia.dto.Post

class PostRepositoryJsonImpl(
    private val context: Context
) : PostRepository {

    private var posts: List<Post> = getPosts()
        set(value) {
            field = value
            sync()
        }

    private var nextId = getId()

    private val data = MutableLiveData<List<Post>>(posts)

    override fun get(): LiveData<List<Post>> = data

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
        data.value = posts
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
        data.value = posts
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
        data.value = posts
    }

    override fun removeById(id: Long) {
        posts = posts.filterNot { it.id == id }
        data.value = posts
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
                if (it.id == post.id) {
                    post
                } else {
                    it
                }
            }
        }

        data.value = posts
    }

    private fun getPosts(): List<Post> {
        return context.filesDir
            .resolve(FILE_NAME)
            .takeIf { it.exists() }
            ?.inputStream()
            ?.bufferedReader()
            ?.use {
                gson.fromJson(it, postsType)
            }
            ?: emptyList()
    }

    private fun getId(): Long {
        return (posts.maxByOrNull { it.id }?.id ?: 0L) + 1L
    }

    private fun sync() {
        context.filesDir
            .resolve(FILE_NAME)
            .outputStream()
            .bufferedWriter()
            .use {
                it.write(gson.toJson(posts))
            }
    }

    private companion object {
        const val FILE_NAME = "posts.json"

        val gson = Gson()

        val postsType = object : TypeToken<List<Post>>() {}.type
    }
}