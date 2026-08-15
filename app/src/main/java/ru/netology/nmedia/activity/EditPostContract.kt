package ru.netology.nmedia.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import ru.netology.nmedia.dto.Post

class EditPostContract : ActivityResultContract<Post, Post?>() {

    override fun createIntent(
        context: Context,
        input: Post
    ): Intent {
        return Intent(context, EditPostActivity::class.java).apply {
            putExtra("post", input)
        }
    }

    override fun parseResult(
        resultCode: Int,
        intent: Intent?
    ): Post? {

        if (resultCode != Activity.RESULT_OK) {
            return null
        }

        return intent
            ?.getSerializableExtra("post") as? Post
    }
}