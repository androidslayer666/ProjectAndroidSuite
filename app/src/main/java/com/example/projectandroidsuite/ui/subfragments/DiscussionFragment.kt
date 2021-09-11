package com.example.projectandroidsuite.ui.subfragments

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.database.entities.CommentEntity
import com.example.database.entities.MessageEntity
import com.example.domain.SessionManager
import com.example.network.dto.UserDto
import com.example.projectandroidsuite.R
import com.example.projectandroidsuite.adapter.CommentAdapter
import com.example.projectandroidsuite.adapter.UniversalAdapter
import com.example.projectandroidsuite.databinding.*
import com.example.projectandroidsuite.ui.dialogs.DialogConfirmation
import com.example.projectandroidsuite.ui.dialogs.ProjectCreateEditDialog
import com.example.projectandroidsuite.ui.dialogs.TaskCreateEditDialog
import com.example.projectandroidsuite.ui.loadImageGlide
import com.example.projectandroidsuite.viewmodel.ProjectDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class DiscussionFragment : Fragment(), CommentAdapter.OnCommentReplyClicker {

    val viewModel: ProjectDetailViewModel by activityViewModels()

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {

        val binding = FragmentDiscussionBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = this

        viewModel.listDiscussions.observe(viewLifecycleOwner, {

            lifecycleScope.launch {
                binding.linearLayoutDiscussion.removeAllViews()
                for (message in it) {
                    //Log.d("DiscussionFragment", "ProjectDetailFragment " + message.listMessages.toString())
                    displayComments(
                        inflater,
                        message,
                        message.listMessages,
                        binding.linearLayoutDiscussion
                    )
                }
            }
        })

        return binding.root
    }

    private fun displayComments(
        inflater: LayoutInflater,
        message: MessageEntity,
        listComments: List<CommentEntity>?,
        layout: LinearLayout
    ) {
        val binding = MessageCommentItemBinding.inflate(inflater, layout, false)
        val adapter = CommentAdapter(this)
        binding.messageTitle.text = message.title
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.messageText.text = Html.fromHtml(message.text, Html.FROM_HTML_MODE_COMPACT);
        } else {
            binding.messageText.text = Html.fromHtml(message.text);
        }
        binding.commentRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.commentRecycler.adapter = adapter
        adapter.submitList(listComments)
        layout.addView(binding.root)
    }

    override fun onCommentReplyClick(comment: CommentEntity) {
        TODO("Not yet implemented")
    }


}