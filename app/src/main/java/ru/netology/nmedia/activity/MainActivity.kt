package ru.netology.nmedia.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.viewmodel.PostViewModel

private fun formatCount(count: Int): String {

    if (count < 1000) {
        return count.toString()
    }

    if (count < 10000) {
        return "${count / 1000}.${count % 1000 / 100}К"
    }

    if (count < 1000000) {
        return "${count / 1000}К"
    }

    if (count < 10000000) {
        return "${count / 1000000}.${count % 1000000 / 100000}М"
    }

    return "${count / 1000000}М"
}

class MainActivity : AppCompatActivity() {

    private val viewModel: PostViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left + v.paddingLeft, systemBars.top + v.paddingTop, systemBars.right + v.paddingRight, systemBars.bottom + v.paddingBottom)
            insets
        }

        viewModel.data.observe(this){ post ->



        with(binding) {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            likeCount.text = formatCount(post.likes)
            shareCount.text = formatCount(post.shares)
            viewsCount.text = formatCount(post.views)
            likeIcon?.setImageResource(if (post.likedByMe) R.drawable.ic_liked_24 else R.drawable.ic_like_24)

            likeIcon?.setOnClickListener {
                if (post.likedByMe) post.likes-- else post.likes++
                post.likedByMe = !post.likedByMe
                likeIcon?.setImageResource(if (post.likedByMe) R.drawable.ic_liked_24 else R.drawable.ic_like_24)
                likeCount.text = formatCount(post.likes)
            }

            shareIcon?.setOnClickListener {
                if (post.sharesByMe) post.shares-- else post.shares++
                post.sharesByMe = !post.sharesByMe
                shareCount.text = formatCount(post.shares)
            }

            viewsIcon?.setOnClickListener {
                if (post.viewsByMe) post.views-- else post.views++
                post.viewsByMe = !post.viewsByMe
                viewsCount.text = formatCount(post.views)
            }

        }
        }

    }
}


