package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.entity.PostEntity

class PostRepositorySQLiteImpl(
    private val dao: PostDao
) : PostRepository {

    private val posts = MutableLiveData<List<Post>>()

    override fun getAll(): LiveData<List<Post>> {
        loadPosts()
        return posts
    }

    override fun get(): LiveData<List<Post>> {
        loadPosts()
        return posts
    }

    override fun save(post: Post) {
        dao.save(PostEntity.fromDto(post))
        loadPosts()
    }

    override fun likeById(id: Long) {
        dao.likeById(id)
        loadPosts()
    }

    override fun shareById(id: Long) {
        dao.shareById(id)
        loadPosts()
    }

    override fun viewsById(id: Long) {
        dao.viewsById(id)
        loadPosts()
    }

    override fun removeById(id: Long) {
        dao.removeById(id)
        loadPosts()
    }

    private fun loadPosts() {
        posts.value = dao.getAll().map { postEntity ->
            postEntity.toPost()
        }
    }
}