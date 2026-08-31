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
import ru.netology.nmedia.adapter.PostAdapter
import ru.netology.nmedia.databinding.FragmentFeedBinding
import ru.netology.nmedia.viewmodel.PostViewModel

class FeedFragment : Fragment() {

    private val viewModel: PostViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentFeedBinding.inflate(
            inflater,
            container,
            false
        )

        val adapter = PostAdapter(
            lileClickListener = { post ->
                viewModel.likesById(post.id)
            },

            shareClickListener = { post ->
                viewModel.shareById(post.id)

                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, post.content)
                    type = "text/plain"
                }

                startActivity(
                    Intent.createChooser(
                        intent,
                        getString(R.string.chooser_share_post)
                    )
                )
            },

            viewsClickListener = { post ->
                viewModel.viewsById(post.id)
            },

            postClickListener = { post ->
                findNavController().navigate(
                    R.id.action_feedFragment_to_postFragment,
                    Bundle().apply {
                        putSerializable("post", post)
                    }
                )
            },

            onEditListener = { post ->
                viewModel.edit(post)

                findNavController().navigate(
                    R.id.action_feedFragment_to_editPostFragment,
                    Bundle().apply {
                        putSerializable("post", post)
                    }
                )
            },

            onRemoveListenner = { post ->
                viewModel.removeById(post.id)
            }
        )

        binding.list.adapter = adapter

        viewModel.data.observe(viewLifecycleOwner) { posts ->
            adapter.submitList(posts)
        }

        binding.fab?.setOnClickListener {
            findNavController().navigate(
                R.id.action_feedFragment_to_newPostFragment
            )
        }

        return binding.root
    }
}
