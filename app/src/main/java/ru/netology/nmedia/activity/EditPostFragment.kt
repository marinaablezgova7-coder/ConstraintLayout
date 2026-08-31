package ru.netology.nmedia.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.databinding.FragmentNewPostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewmodel.PostViewModel

class EditPostFragment : Fragment() {

    private val viewModel: PostViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentNewPostBinding.inflate(
            inflater,
            container,
            false
        )

        val post = arguments?.getSerializable("post") as? Post
            ?: return binding.root

        binding.edit.setText(post.content)

        binding.ok.setOnClickListener {

            val content = binding.edit.text.toString().trim()

            if (content.isBlank()) {
                return@setOnClickListener
            }

            viewModel.saveEdited(
                post.copy(content = content)
            )

            findNavController().navigateUp()
        }

        return binding.root
    }
}
