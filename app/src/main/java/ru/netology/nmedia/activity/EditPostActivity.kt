package ru.netology.nmedia.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.netology.nmedia.databinding.ActivityEditPostBinding
import ru.netology.nmedia.dto.Post

class EditPostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val binding = ActivityEditPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )

            insets
        }

        val post = intent.getSerializableExtra("post") as? Post

        if (post == null) {
            finish()
            return
        }

        // Показываем старый текст
        binding.edit.setText(post.content)

        binding.ok.setOnClickListener {

            val newContent = binding.edit.text.toString().trim()

            if (newContent.isBlank()) {
                return@setOnClickListener
            }

            // Создаем измененный пост
            val newPost = post.copy(
                content = newContent
            )

            // Возвращаем его в MainActivity
            val resultIntent = Intent().apply {
                putExtra("post", newPost)
            }

            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}