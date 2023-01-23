package com.zwstudio.lolly.ui.patterns

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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.woxthebox.draglistview.DragItem
import com.woxthebox.draglistview.DragItemAdapter
import com.woxthebox.draglistview.DragListView
import com.zwstudio.lolly.MainActivity
import com.zwstudio.lolly.R
import com.zwstudio.lolly.common.speak
import com.zwstudio.lolly.databinding.FragmentPatternsWebpagesListBinding
import com.zwstudio.lolly.models.wpp.MPatternWebPage
import com.zwstudio.lolly.ui.*
import com.zwstudio.lolly.ui.common.DrawerListFragment
import com.zwstudio.lolly.ui.common.autoCleared
import com.zwstudio.lolly.ui.common.yesNoDialog
import com.zwstudio.lolly.viewmodels.DrawerListViewModel
import com.zwstudio.lolly.viewmodels.patterns.PatternsWebPagesViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.getViewModel
import java.nio.file.Files.delete

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
        vm.compositeDisposable = compositeDisposable
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListView(PatternsWebPagesDragItem(requireContext(), R.layout.list_item_patterns_webpages_edit))

        combine(vm.lstWebPages_, vm.isEditMode_, ::Pair).onEach {
            val listAdapter = PatternsWebPagesItemAdapter(vm, mDragListView, compositeDisposable)
            mDragListView.setAdapter(listAdapter, true)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        compositeDisposable.add(vm.getWebPages(item.id).subscribe())
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_patterns_webpages_list, menu)
        setEditMode(menu.findItem(if (vm.isEditMode) R.id.menuEditMode else R.id.menuNormalMode), vm.isEditMode)
    }

    private fun setEditMode(menuItem: MenuItem, isEditMode: Boolean) {
        vm.isEditMode = isEditMode
        menuItem.isChecked = true
    }

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
                findNavController().navigate(PatternsWebPagesListFragmentDirections.actionPatternsWebPagesListFragmentToPatternsWebPagesDetailFragment(vm.newPatternWebPage(item.id, item.pattern)))
                true
            }
            else -> false
        }

    private class PatternsWebPagesDragItem(context: Context, layoutId: Int) : DragItem(context, layoutId) {

        override fun onBindDragView(clickedView: View, dragView: View) {
            dragView.findViewById<TextView>(R.id.text1).text = clickedView.findViewById<TextView>(R.id.text1).text
            dragView.findViewById<TextView>(R.id.text2).text = clickedView.findViewById<TextView>(R.id.text2).text
            dragView.findViewById<TextView>(R.id.text3).text = clickedView.findViewById<TextView>(R.id.text3).text
            dragView.findViewById<View>(R.id.item_swipe).setBackgroundColor(dragView.resources.getColor(R.color.list_item_background, null))
        }
    }

    private class PatternsWebPagesItemAdapter(val vm: PatternsWebPagesViewModel, val mDragListView: DragListView, val compositeDisposable: CompositeDisposable) : DragItemAdapter<MPatternWebPage, PatternsWebPagesItemAdapter.ViewHolder>() {

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
            var mHamburger: ImageView = itemView.findViewById(R.id.image_hamburger)
            val navController get() = (itemView.context as MainActivity).getNavController()

            init {
                if (!vm.isEditMode)
                    mHamburger.visibility = View.GONE
            }

            fun edit(item: MPatternWebPage) =
                navController.navigate(PatternsWebPagesListFragmentDirections.actionPatternsWebPagesListFragmentToPatternsWebPagesDetailFragment(item))

            override fun onItemClicked(view: View?) {
                val item = itemView.tag as MPatternWebPage
                if (vm.isEditMode)
                    edit(item)
                else
                    speak(item.title)
            }

            override fun onItemLongClicked(view: View?): Boolean {
                val item = itemView.tag as MPatternWebPage
                // https://stackoverflow.com/questions/16389581/android-create-a-popup-that-has-multiple-selection-options
                AlertDialog.Builder(itemView.context)
                    .setTitle(item.title)
                    .setItems(arrayOf(
                        itemView.context.getString(R.string.action_delete),
                        itemView.context.getString(R.string.action_edit),
                        itemView.context.getString(R.string.action_cancel),
                    )) { _, which ->
                        when (which) {
                            0 ->
                                yesNoDialog(itemView.context, "Are you sure you want to delete the web page \"${item.title}\"?", {
                                    val pos = mDragListView.adapter.getPositionForItem(item)
                                    mDragListView.adapter.removeItem(pos)
                                    compositeDisposable.add(vm.deletePatternWebPage(item.id).subscribe())
                                }, {})
                            1 -> edit(item)
                            else -> {}
                        }
                    }.show()
                return true
            }
        }
    }
}
