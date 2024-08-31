package com.zwstudio.lolly.ui.webtextbooks

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.woxthebox.draglistview.DragItemAdapter
import com.woxthebox.draglistview.DragListView
import com.zwstudio.lolly.MainActivity
import com.zwstudio.lolly.R
import com.zwstudio.lolly.common.speak
import com.zwstudio.lolly.common.vmSettings
import com.zwstudio.lolly.databinding.FragmentWebTextbooksBinding
import com.zwstudio.lolly.models.misc.MWebTextbook
import com.zwstudio.lolly.ui.*
import com.zwstudio.lolly.ui.common.*
import com.zwstudio.lolly.ui.misc.*
import com.zwstudio.lolly.viewmodels.DrawerListViewModel
import com.zwstudio.lolly.viewmodels.webtextbooks.WebTextbooksViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class WebTextbooksFragment : DrawerListFragment() {

    val vm by viewModel<WebTextbooksViewModel>()
    override val vmDrawerList: DrawerListViewModel get() = vm
    var binding by autoCleared<FragmentWebTextbooksBinding>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentWebTextbooksBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            model = vm
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.spnWebTextbookFilter.adapter = makeCustomAdapter(requireContext(), vmSettings.lstWebTextbookFilters) { it.label }
        setupListView()

        vm.lstWebTextbooks_.onEach {
            val listAdapter = WebTextbooksItemAdapter(vm, mDragListView)
            mDragListView.setAdapter(listAdapter, true)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        vm.isBusy_.onEach {
            progressBar1.visibility = if (it) View.VISIBLE else View.GONE
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewLifecycleOwner.lifecycleScope.launch {
            vm.getData()
        }
    }

    private class WebTextbooksItemAdapter(val vm: WebTextbooksViewModel, val mDragListView: DragListView) : DragItemAdapter<MWebTextbook, WebTextbooksItemAdapter.ViewHolder>() {

        init {
            itemList = vm.lstWebTextbooks
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_web_textbooks_edit, parent, false)
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
                        val item = itemView.tag as MWebTextbook
                        navController.navigate(WebTextbooksFragmentDirections.actionWebTextbooksFragmentToWebTextbooksWebPageFragment(item))
                    }
                    true
                }
            }

            override fun onItemClicked(view: View?) {
                val item = itemView.tag as MWebTextbook
                speak(item.textbookname)
            }

            override fun onItemLongClicked(view: View?): Boolean {
                val item = itemView.tag as MWebTextbook
                // https://stackoverflow.com/questions/16389581/android-create-a-popup-that-has-multiple-selection-options
                AlertDialog.Builder(itemView.context)
                    .setTitle(item.title)
                    .setItems(arrayOf(
                        itemView.context.getString(R.string.action_edit),
                        itemView.context.getString(R.string.action_browse_web_page),
                        itemView.context.getString(R.string.action_cancel),
                    )) { _, which ->
                        when (which) {
                            0 -> navController.navigate(WebTextbooksFragmentDirections.actionWebTextbooksFragmentToWebTextbooksDetailFragment(item))
                            1 -> navController.navigate(WebTextbooksFragmentDirections.actionWebTextbooksFragmentToWebTextbooksWebPageFragment(item))
                            else -> {}
                        }
                    }.show()
                return true
            }
        }
    }
}
