package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryJsonImpl
private val emptyPost = Post()

class PostViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PostRepository =
        PostRepositoryJsonImpl(application)

    val data: LiveData<List<Post>> = repository.get()

    fun likesById(id: Long) = repository.likeById(id)

    fun shareById(id: Long) {
        repository.shareById(id)
    }

    fun viewsById(id: Long) = repository.viewsById(id)

    fun removeById(id: Long) = repository.removeById(id)

    val edited = MutableLiveData(emptyPost)

    fun edit(post: Post) {
        edited.value = post
    }

    fun cancelEditing() {
        edited.value = emptyPost
    }

    fun save(content: String) {
        val text = content.trim()
        if (text.isEmpty()) return

        edited.value?.let { post ->
            repository.save(
                post.copy(content = text)
            )
        }

        edited.value = emptyPost
    }

    fun saveEdited(post: Post) {
        repository.save(post)
        edited.value = emptyPost
    }
}


