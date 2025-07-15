package com.zwstudio.lolly.ui.blogs

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.woxthebox.draglistview.DragItemAdapter
import com.woxthebox.draglistview.DragListView
import com.zwstudio.lolly.MainActivity
import com.zwstudio.lolly.R
import com.zwstudio.lolly.common.getPreferredRangeFromArray
import com.zwstudio.lolly.common.speak
import com.zwstudio.lolly.databinding.FragmentLangBlogPostsListBinding
import com.zwstudio.lolly.models.blogs.MLangBlogPost
import com.zwstudio.lolly.ui.common.DrawerListFragment
import com.zwstudio.lolly.ui.common.autoCleared
import com.zwstudio.lolly.viewmodels.DrawerListViewModel
import com.zwstudio.lolly.viewmodels.blogs.LangBlogGroupsViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class LangBlogPostsListFragment : DrawerListFragment() {

    val vm by activityViewModel<LangBlogGroupsViewModel>()
    override val vmDrawerList: DrawerListViewModel get() = vm
    var binding by autoCleared<FragmentLangBlogPostsListBinding>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentLangBlogPostsListBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            model = vm
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.svPostFilter.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
                vm.postFilter = newText
                return false
            }
        })
        setupListView()

        vm.lstLangBlogPosts_.onEach {
            val listAdapter = LangBlogPostsItemAdapter(vm, mDragListView)
            mDragListView.setAdapter(listAdapter, true)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        vm.isBusy_.onEach {
            progressBar1.visibility = if (it) View.VISIBLE else View.GONE
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private class LangBlogPostsItemAdapter(val vm: LangBlogGroupsViewModel, val mDragListView: DragListView) : DragItemAdapter<MLangBlogPost, LangBlogPostsItemAdapter.ViewHolder>() {

        init {
            itemList = vm.lstLangBlogPosts
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_online_textbooks_edit, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            val item = mItemList[position]
            holder.mText1.text = item.title
            holder.itemView.tag = item
        }

        override fun getUniqueItemId(position: Int): Long {
            return mItemList[position].id.toLong()
        }

        @SuppressLint("ClickableViewAccessibility")
        inner class ViewHolder(itemView: View) : DragItemAdapter.ViewHolder(itemView, R.id.image_hamburger, false) {
            var mText1: TextView = itemView.findViewById(R.id.text1)
            var mForward: ImageView = itemView.findViewById(R.id.image_forward)
            val navController get() = (itemView.context as MainActivity).getNavController()

            init {
                mForward.setOnTouchListener { _, event ->
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        val item = itemView.tag as MLangBlogPost
                        showContent(item)
                    }
                    true
                }
            }

            override fun onItemClicked(view: View?) {
                val item = itemView.tag as MLangBlogPost
                speak(item.title)
            }

            override fun onItemLongClicked(view: View?): Boolean {
                val item = itemView.tag as MLangBlogPost
                // https://stackoverflow.com/questions/16389581/android-create-a-popup-that-has-multiple-selection-options
                AlertDialog.Builder(itemView.context)
                    .setTitle(item.title)
                    .setItems(arrayOf(
                        itemView.context.getString(R.string.action_edit),
                        itemView.context.getString(R.string.action_show_content),
                        itemView.context.getString(R.string.action_cancel),
                    )) { _, which ->
                        when (which) {
                            0 -> navController.navigate(LangBlogPostsListFragmentDirections.actionLangBlogPostsListFragmentToLangBlogPostsDetailFragment(item))
                            1 -> showContent(item)
                            else -> {}
                        }
                    }.show()
                return true
            }

            private fun showContent(item: MLangBlogPost) {
                vm.selectedPost = item
                val index = vm.lstLangBlogPosts.indexOf(item)
                val (start, end) = getPreferredRangeFromArray(index, vm.lstLangBlogPosts.size, 50)
                navController.navigate(LangBlogPostsListFragmentDirections.actionLangBlogPostsListFragmentToLangBlogPostsContentFragment(
                    vm.lstLangBlogPosts.subList(start, end).toTypedArray(), index
                ))
            }
        }
    }
}
