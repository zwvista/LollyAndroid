package com.zwstudio.lolly.ui.onlinetextbooks

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.woxthebox.draglistview.DragItemAdapter
import com.woxthebox.draglistview.DragListView
import com.zwstudio.lolly.MainActivity
import com.zwstudio.lolly.R
import com.zwstudio.lolly.common.getPreferredRangeFromArray
import com.zwstudio.lolly.common.speak
import com.zwstudio.lolly.common.vmSettings
import com.zwstudio.lolly.databinding.FragmentOnlineTextbooksBinding
import com.zwstudio.lolly.models.misc.MOnlineTextbook
import com.zwstudio.lolly.ui.common.LollyListFragment
import com.zwstudio.lolly.ui.common.autoCleared
import com.zwstudio.lolly.ui.common.makeCustomAdapter
import com.zwstudio.lolly.viewmodels.LollyListViewModel
import com.zwstudio.lolly.viewmodels.onlinetextbooks.OnlineTextbooksViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class OnlineTextbooksFragment : LollyListFragment() {

    val vm by viewModel<OnlineTextbooksViewModel>()
    override val vmList: LollyListViewModel get() = vm
    var binding by autoCleared<FragmentOnlineTextbooksBinding>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentOnlineTextbooksBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            model = vm
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.spnOnlineTextbookFilter.adapter = makeCustomAdapter(requireContext(), vmSettings.lstOnlineTextbookFilters) { it.label }
        setupListView()

        vm.lstOnlineTextbooks_.onEach {
            val listAdapter = OnlineTextbooksItemAdapter(vm, mDragListView)
            mDragListView.setAdapter(listAdapter, true)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override suspend fun onRefresh() = vm.getData()

    private class OnlineTextbooksItemAdapter(val vm: OnlineTextbooksViewModel, val mDragListView: DragListView) : DragItemAdapter<MOnlineTextbook, OnlineTextbooksItemAdapter.ViewHolder>() {

        init {
            itemList = vm.lstOnlineTextbooks
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_online_textbooks_edit, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            val item = mItemList[position]
            holder.mText1.text = item.textbookname
            holder.mText2.text = item.title
            holder.itemView.tag = item
        }

        override fun getUniqueItemId(position: Int): Long {
            return mItemList[position].id.toLong()
        }

        @SuppressLint("ClickableViewAccessibility")
        inner class ViewHolder(itemView: View) : DragItemAdapter.ViewHolder(itemView, R.id.image_hamburger, false) {
            var mText1: TextView = itemView.findViewById(R.id.text1)
            var mText2: TextView = itemView.findViewById(R.id.text2)
            var mForward: ImageView = itemView.findViewById(R.id.image_forward)
            val navController get() = (itemView.context as MainActivity).getNavController()

            init {
                mForward.setOnTouchListener { _, event ->
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        val item = itemView.tag as MOnlineTextbook
                        browseWebPage(item)
                    }
                    true
                }
            }

            override fun onItemClicked(view: View?) {
                val item = itemView.tag as MOnlineTextbook
                speak(item.textbookname)
            }

            override fun onItemLongClicked(view: View?): Boolean {
                val item = itemView.tag as MOnlineTextbook
                // https://stackoverflow.com/questions/16389581/android-create-a-popup-that-has-multiple-selection-options
                AlertDialog.Builder(itemView.context)
                    .setTitle(item.title)
                    .setItems(arrayOf(
                        itemView.context.getString(R.string.action_edit),
                        itemView.context.getString(R.string.action_browse_web_page),
                        itemView.context.getString(R.string.action_cancel),
                    )) { _, which ->
                        when (which) {
                            0 -> navController.navigate(OnlineTextbooksFragmentDirections.actionOnlineTextbooksFragmentToOnlineTextbooksDetailFragment(item))
                            1 -> browseWebPage(item)
                            else -> {}
                        }
                    }.show()
                return true
            }

            private fun browseWebPage(item: MOnlineTextbook) {
                val index = vm.lstOnlineTextbooks.indexOf(item)
                val (start, end) = getPreferredRangeFromArray(index, vm.lstOnlineTextbooks.size, 50)
                navController.navigate(
                    OnlineTextbooksFragmentDirections.actionOnlineTextbooksFragmentToOnlineTextbooksWebPageFragment(
                    vm.lstOnlineTextbooks.subList(start, end).toTypedArray(), index
                ))
            }
        }
    }
}
