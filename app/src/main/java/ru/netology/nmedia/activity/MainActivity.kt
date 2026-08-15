package ru.netology.nmedia.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.launch
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

        val postContract = registerForActivityResult(NewPostContract){ result ->
           result ?: return@registerForActivityResult
            viewModel.save((result))
        }
        val editPostContract = registerForActivityResult(EditPostContract()) { result ->
            result ?: return@registerForActivityResult
            viewModel.edit(result)
        }


        val adapter = PostAdapter(
            lileClickListener = {
                viewModel.likesById(it.id)
            },

            shareClickListener = { post ->
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, post.content)
                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(
                    intent,
                    getString(R.string.chooser_share_post)
                )

                startActivity(shareIntent)
            },

            viewsClickListener = {
                viewModel.viewsById(it.id)
            },

            onRemoveListenner = {
                viewModel.removeById(it.id)
            },

            onEditListener = {
                editPostContract.launch(it)
            }
        )

        binding.list?.adapter = adapter
        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
        }

        binding.save?.setOnClickListener {
            val content =  binding.content?.text?.toString()
            if (content.isNullOrBlank()){
                Toast.makeText(this, (R.string.error_emple_text), Toast.LENGTH_SHORT).show()
            return@setOnClickListener
            }

            viewModel.save(content)
            binding.content.clearFocus()
            binding.content.setText("")
        }

        binding.add?.setOnClickListener{
            postContract.launch()
            
        }

        viewModel.edited.observe(this) { post ->
            if (post.id == 0L) {
                binding.editGroup?.visibility = View.GONE
                binding.content?.setText("")
            } else {
                binding.editGroup?.visibility = View.VISIBLE
                binding.content?.setText(post.content)
                binding.content?.requestFocus()
            }
        }
        binding.cancelEdit?.setOnClickListener {
            viewModel.cancelEditing()
        }





    }
}


