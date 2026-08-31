package ru.netology.nmedia.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.PostViewHolder
import ru.netology.nmedia.databinding.FragmentPostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewmodel.PostViewModel

class PostFragment : Fragment() {

    private val viewModel: PostViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentPostBinding.inflate(
            inflater,
            container,
            false
        )

        val post = arguments?.getSerializable("post") as? Post
            ?: return binding.root

        val holder = PostViewHolder(
            binding = binding.post,

            lileClickListener = { currentPost ->
                viewModel.likesById(currentPost.id)
            },

            shareClickListener = { currentPost ->
                viewModel.shareById(currentPost.id)

                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, currentPost.content)
                    type = "text/plain"
                }

                startActivity(
                    Intent.createChooser(
                        intent,
                        getString(R.string.chooser_share_post)
                    )
                )
            },

            viewsClickListener = { currentPost ->
                viewModel.viewsById(currentPost.id)
            },

            postClickListener = { },

            onEditListener = { currentPost ->
                viewModel.edit(currentPost)

                findNavController().navigate(
                    R.id.action_postFragment_to_editPostFragment,
                    Bundle().apply {
                        putSerializable("post", currentPost)
                    }
                )
            },

            onRemoveListenner = { currentPost ->
                viewModel.removeById(currentPost.id)
                findNavController().navigateUp()
            }
        )

        viewModel.data.observe(viewLifecycleOwner) { posts ->
            posts.find { it.id == post.id }?.let { currentPost ->
                holder.bind(currentPost)
            }
        }

        return binding.root
    }
}

