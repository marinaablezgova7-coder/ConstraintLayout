package ru.netology.nmedia.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
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

        with(binding.post) {

            author.text = post.author
            published.text = post.published
            content.text = post.content

            likeIcon.isChecked = post.likedByMe
            likeIcon.text = post.likes.toString()

            shareIcon.text = post.shares.toString()
            viewsCount.text = post.views.toString()

            // Меню
            menu.setOnClickListener {
                PopupMenu(requireContext(), menu).apply {
                    inflate(R.menu.post_menu)

                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {

                            R.id.edit -> {
                                findNavController().navigate(
                                    R.id.action_postFragment_to_editPostFragment,
                                    Bundle().apply {
                                        putSerializable("post", post)
                                    }
                                )
                                true
                            }

                            R.id.remove -> {
                                viewModel.removeById(post.id)
                                findNavController().navigateUp()
                                true
                            }

                            else -> false
                        }
                    }

                    show()
                }
            }

            // Like
            likeIcon.setOnClickListener {
                viewModel.likesById(post.id)
            }

            // Share
            shareIcon.setOnClickListener {
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
            }

            // Views
            viewsIcon.setOnClickListener {
                viewModel.viewsById(post.id)
            }

            // Video
            if (!post.video.isNullOrBlank()) {

                videoContainer.visibility = View.VISIBLE

                val videoIntent = Intent(
                    Intent.ACTION_VIEW,
                    android.net.Uri.parse(post.video)
                )

                videoContainer.setOnClickListener {
                    startActivity(videoIntent)
                }

                videoPlay.setOnClickListener {
                    startActivity(videoIntent)
                }

            } else {
                videoContainer.visibility = View.GONE
            }
        }

        return binding.root
    }
}

