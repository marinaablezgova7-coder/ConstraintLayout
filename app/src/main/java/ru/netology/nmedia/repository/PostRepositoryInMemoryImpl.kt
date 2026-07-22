package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post

class PostRepositoryInMemoryImpl: PostRepository {

    var post = Post(
        id =  1,
        author = "Нетология. Университет интернет-профессий будущего",
        published = "21 мая 18:36",
        content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
        likes = 1999,
        shares = 1999,
        views = 1999,
        likedByMe = false,
        sharesByMe = false
    )

    private val data = MutableLiveData<Post>(post)

    override fun getData(): LiveData<Post> = data

    override fun like() {
        post = post.copy(
            likedByMe = !post.likedByMe, likes = (if (post.likedByMe) {
                post.likes = 1
            } else {
                post.likes + 1
            }) as Int
        )
        data.value = post
    }

    override fun shares() {
        post = post.copy(
            shares = post.shares + 1
        )

        data.value = post
    }

    override fun views() {
        post = post.copy(
            views = post.views + 1
        )

        data.value = post
    }

}