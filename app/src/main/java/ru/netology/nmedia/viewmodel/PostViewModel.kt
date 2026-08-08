package ru.netology.nmedia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryInMemoryImpl

private val emptyPost = Post()


class PostViewModel: ViewModel(){
   private val repository: PostRepository = PostRepositoryInMemoryImpl()

    val data: LiveData<List<Post>> = repository.get()

    fun likesById(id: Long) = repository.likeById(id)
    fun shareById(id: Long) { repository.shareById(id) }

    fun viewsById(id: Long) = repository.viewsById(id)

    fun removeById(id: Long) = repository.removeById(id)

    val  edited = MutableLiveData(emptyPost)

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
    }


