package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.PostAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.viewmodel.PostViewModel
import androidx.recyclerview.widget.LinearLayoutManager



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
            v.setPadding(
                systemBars.left + v.paddingLeft,
                systemBars.top + v.paddingTop,
                systemBars.right + v.paddingRight,
                systemBars.bottom + v.paddingBottom
            )
            insets
        }




        val adapter = PostAdapter(
            lileClickListener = {
                viewModel.likesById(it.id)
            },
            shareClickListener = {
                viewModel.shareById(it.id)
            },
            viewsClickListener = {
                viewModel.viewsById(it.id)
            }
        )

        binding.main.adapter = adapter

        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
        }



    }
}


