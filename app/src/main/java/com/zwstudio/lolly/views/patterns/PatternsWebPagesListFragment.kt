package com.zwstudio.lolly.views.patterns

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.woxthebox.draglistview.DragItem
import com.woxthebox.draglistview.DragItemAdapter
import com.woxthebox.draglistview.DragListView
import com.zwstudio.lolly.MainActivity
import com.zwstudio.lolly.common.speak
import com.zwstudio.lolly.models.wpp.MPatternWebPage
import com.zwstudio.lolly.viewmodels.DrawerListViewModel
import com.zwstudio.lolly.viewmodels.patterns.PatternsWebPagesViewModel
import com.zwstudio.lolly.views.*
import com.zwstudio.lolly.views.databinding.FragmentPatternsWebpagesListBinding
import com.zwstudio.lolly.views.misc.autoCleared
import com.zwstudio.lolly.views.misc.yesNoDialog
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.getViewModel

private const val REQUEST_CODE = 1

class PatternsWebPagesListFragment : DrawerListFragment(), MenuProvider {

    val vm by lazy { requireParentFragment().getViewModel<PatternsWebPagesViewModel>() }
    override val vmDrawerList: DrawerListViewModel get() = vm
    var binding by autoCleared<FragmentPatternsWebpagesListBinding>()
    val args: PatternsWebPagesListFragmentArgs by navArgs()
    val item get() = args.item

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        requireActivity().addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        binding = FragmentPatternsWebpagesListBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            model = vm
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListView(PatternsWebPagesDragItem(requireContext(), R.layout.list_item_patterns_webpages_edit))
        vm.viewModelScope.launch {
            vm.getWebPages(item.id)
            refreshListView()
            progressBar1.visibility = View.GONE
        }
    }

    private fun refreshListView() {
        val listAdapter = PatternsWebPagesItemAdapter(vm, mDragListView)
        mDragListView.setAdapter(listAdapter, true)
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_patterns_webpages_list, menu)
        setEditMode(menu.findItem(if (vm.isEditMode) R.id.menuEditMode else R.id.menuNormalMode), vm.isEditMode)
    }

    private fun setEditMode(menuItem: MenuItem, isEditMode: Boolean) {
        vm.isEditMode = isEditMode
        menuItem.isChecked = true
        refreshListView()
    }

    fun menuAdd() =
        findNavController().navigate(PatternsWebPagesListFragmentDirections.actionPatternsWebPagesListFragmentToPatternsWebPagesDetailFragment(vm.newPatternWebPage(item.id, item.pattern)))

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean =
        when (menuItem.itemId) {
            R.id.menuNormalMode -> {
                setEditMode(menuItem, false)
                true
            }
            R.id.menuEditMode -> {
                setEditMode(menuItem, true)
                true
            }
            R.id.menuAdd -> {
                menuAdd()
                true
            }
            else -> false
        }

//    @OnActivityResult(REQUEST_CODE)
//    fun onResult(resultCode: Int) {
//        if (resultCode == RESULT_OK)
//            menuAdd()
//    }

    private class PatternsWebPagesDragItem(context: Context, layoutId: Int) : DragItem(context, layoutId) {

        override fun onBindDragView(clickedView: View, dragView: View) {
            dragView.findViewById<TextView>(R.id.text1).text = clickedView.findViewById<TextView>(R.id.text1).text
            dragView.findViewById<TextView>(R.id.text2).text = clickedView.findViewById<TextView>(R.id.text2).text
            dragView.findViewById<TextView>(R.id.text3).text = clickedView.findViewById<TextView>(R.id.text3).text
            dragView.findViewById<View>(R.id.item_swipe).setBackgroundColor(dragView.resources.getColor(R.color.list_item_background, null))
        }
    }

    private class PatternsWebPagesItemAdapter(val vm: PatternsWebPagesViewModel, val mDragListView: DragListView) : DragItemAdapter<MPatternWebPage, PatternsWebPagesItemAdapter.ViewHolder>() {

        init {
            itemList = vm.lstWebPages
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_patterns_webpages_edit, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            val item = mItemList[position]
            holder.mText1.text = item.title
            holder.mText2.text = item.seqnum.toString()
            holder.mText3.text = item.url
            holder.itemView.tag = item
        }

        override fun getUniqueItemId(position: Int): Long {
            return mItemList[position].id.toLong()
        }

        inner class ViewHolder(itemView: View) : DragItemAdapter.ViewHolder(itemView, R.id.image_hamburger, false) {
            var mText1: TextView = itemView.findViewById(R.id.text1)
            var mText2: TextView = itemView.findViewById(R.id.text2)
            var mText3: TextView = itemView.findViewById(R.id.text3)
            var mEdit: TextView = itemView.findViewById(R.id.item_edit)
            var mDelete: TextView = itemView.findViewById(R.id.item_delete)
            var mMore: TextView = itemView.findViewById(R.id.item_more)
            var mHamburger: ImageView = itemView.findViewById(R.id.image_hamburger)
            val navController get() = (itemView.context as MainActivity).getNavController()

            init {
                initButtons()
            }

            fun edit(item: MPatternWebPage) =
                navController.navigate(PatternsWebPagesListFragmentDirections.actionPatternsWebPagesListFragmentToPatternsWebPagesDetailFragment(item))

            @SuppressLint("ClickableViewAccessibility")
            private fun initButtons() {
                fun delete(item: MPatternWebPage) {
                    yesNoDialog(itemView.context, "Are you sure you want to delete the web page \"${item.title}\"?", {
                        val pos = mDragListView.adapter.getPositionForItem(item)
                        mDragListView.adapter.removeItem(pos)
                        vm.deletePatternWebPage(item.id)
                        vm.isSwipeStarted = false
                    }, {
                        mDragListView.resetSwipedViews(null)
                        vm.isSwipeStarted = false
                    })
                }
                mEdit.setOnTouchListener { _, event ->
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        val item = itemView.tag as MPatternWebPage
                        edit(item)
                    }
                    true
                }
                mDelete.setOnTouchListener { _, event ->
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        val item = itemView.tag as MPatternWebPage
                        delete(item)
                    }
                    true
                }
                mMore.setOnTouchListener { _, event ->
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        mDragListView.resetSwipedViews(null)
                        vm.isSwipeStarted = false

                        val item = itemView.tag as MPatternWebPage
                        // https://stackoverflow.com/questions/16389581/android-create-a-popup-that-has-multiple-selection-options
                        AlertDialog.Builder(itemView.context)
                            .setTitle(item.title)
                            .setItems(arrayOf("Delete", "Edit", "Cancel")) { _, which ->
                                when (which) {
                                    0 -> delete(item)
                                    1 -> edit(item)
                                    else -> {}
                                }
                            }.show()
                    }
                    true
                }
                if (!vm.isEditMode)
                    mHamburger.visibility = View.GONE
            }

            override fun onItemClicked(view: View?) {
                if (vm.isSwipeStarted) {
                    mDragListView.resetSwipedViews(null)
                    vm.isSwipeStarted = false
                } else {
                    val item = view!!.tag as MPatternWebPage
                    if (vm.isEditMode)
                        edit(item)
                    else
                        speak(item.title)
                }
            }

            override fun onItemLongClicked(view: View?): Boolean {
                Toast.makeText(view!!.context, "Item long clicked", Toast.LENGTH_SHORT).show()
                return true
            }
        }
    }
}
