package ru.netology.nmedia.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.databinding.FragmentNewPostBinding
import ru.netology.nmedia.util.AndroidUtils
import ru.netology.nmedia.util.StringArg
import ru.netology.nmedia.viewmodel.PostViewModel
import kotlin.getValue

class NewPostFragment : Fragment() {

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

        binding.edit.setText(arguments?.contentArg)

        AndroidUtils.showKeyboard(binding.edit)

        binding.ok.setOnClickListener {

            val content = binding.edit.text.toString()

            if (content.isNotBlank()) {
                viewModel.save(content)
            }

            findNavController().navigateUp()
        }

        return binding.root
    }

    companion object {
        var Bundle.contentArg by StringArg
    }
}