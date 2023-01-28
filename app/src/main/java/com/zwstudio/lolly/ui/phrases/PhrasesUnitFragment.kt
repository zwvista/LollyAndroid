package com.zwstudio.lolly.ui.phrases

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.view.MenuProvider
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.woxthebox.draglistview.DragItem
import com.woxthebox.draglistview.DragItemAdapter
import com.woxthebox.draglistview.DragListView
import com.zwstudio.lolly.MainActivity
import com.zwstudio.lolly.R
import com.zwstudio.lolly.common.copyText
import com.zwstudio.lolly.common.googleString
import com.zwstudio.lolly.common.speak
import com.zwstudio.lolly.common.vmSettings
import com.zwstudio.lolly.databinding.FragmentPhrasesUnitBinding
import com.zwstudio.lolly.models.wpp.MUnitPhrase
import com.zwstudio.lolly.ui.common.DrawerListFragment
import com.zwstudio.lolly.ui.common.autoCleared
import com.zwstudio.lolly.ui.common.makeCustomAdapter
import com.zwstudio.lolly.ui.common.yesNoDialog
import com.zwstudio.lolly.viewmodels.DrawerListViewModel
import com.zwstudio.lolly.viewmodels.misc.SettingsViewModel
import com.zwstudio.lolly.viewmodels.phrases.PhrasesUnitViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PhrasesUnitFragment : DrawerListFragment(), MenuProvider {

    val vm by viewModel<PhrasesUnitViewModel>()
    override val vmDrawerList: DrawerListViewModel get() = vm
    var binding by autoCleared<FragmentPhrasesUnitBinding>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        requireActivity().addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        binding = FragmentPhrasesUnitBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            model = vm
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.svTextFilter.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
                vm.textFilter = newText
                return false
            }
        })
        binding.spnScopeFilter.adapter = makeCustomAdapter(requireContext(), SettingsViewModel.lstScopePhraseFilters) { it.label }
        setupListView(PhrasesUnitDragItem(requireContext(), R.layout.list_item_phrases_unit_edit))

        setFragmentResultListener("PhrasesUnitDetailFragment") { _, bundle ->
            if (bundle.getBoolean("isAdd"))
                menuAdd()
        }

        combine(vm.lstPhrases_, vm.isEditMode_, ::Pair).onEach {
            val listAdapter = PhrasesUnitItemAdapter(vm, mDragListView)
            mDragListView.setAdapter(listAdapter, true)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        vm.isBusy_.onEach {
            progressBar1.visibility = if (it) View.VISIBLE else View.GONE
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewLifecycleOwner.lifecycleScope.launch {
            vm.getDataInTextbook()
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_phrases_unit, menu)
        setEditMode(menu.findItem(if (vm.isEditMode) R.id.menuEditMode else R.id.menuNormalMode), vm.isEditMode)
    }

    private fun setEditMode(menuItem: MenuItem, isEditMode: Boolean) {
        vm.isEditMode = isEditMode
        menuItem.isChecked = true
    }

    fun menuAdd() =
        findNavController().navigate(PhrasesUnitFragmentDirections.actionPhrasesUnitFragmentToPhrasesUnitDetailFragment(vm.newUnitPhrase()))

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
            R.id.menuBatch -> {
                findNavController().navigate(PhrasesUnitFragmentDirections.actionPhrasesUnitFragmentToPhrasesUnitBatchEditFragment(vm.lstPhrases.toTypedArray()))
                true
            }
            else -> false
        }

    private class PhrasesUnitDragItem(context: Context, layoutId: Int) : DragItem(context, layoutId) {

        override fun onBindDragView(clickedView: View, dragView: View) {
            dragView.findViewById<TextView>(R.id.text1).text = clickedView.findViewById<TextView>(R.id.text1).text
            dragView.findViewById<TextView>(R.id.text2).text = clickedView.findViewById<TextView>(R.id.text2).text
            dragView.findViewById<TextView>(R.id.text3).text = clickedView.findViewById<TextView>(R.id.text3).text
            dragView.findViewById<View>(R.id.item_swipe).setBackgroundColor(dragView.resources.getColor(R.color.list_item_background, null))
        }
    }

    private class PhrasesUnitItemAdapter(val vm: PhrasesUnitViewModel, val mDragListView: DragListView) : DragItemAdapter<MUnitPhrase, PhrasesUnitItemAdapter.ViewHolder>() {

        init {
            itemList = vm.lstPhrases
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_phrases_unit_edit, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            val item = mItemList[position]
            holder.mText1.text = item.phrase
            holder.mText2.text = item.unitpartseqnum
            holder.mText3.text = item.translation
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
                if (!(vm.isEditMode && vmSettings.isSingleUnitPart && vm.noFilter))
                    mHamburger.visibility = View.GONE
            }

            fun edit(item: MUnitPhrase) =
                navController.navigate(PhrasesUnitFragmentDirections.actionPhrasesUnitFragmentToPhrasesUnitDetailFragment(item))

            override fun onItemClicked(view: View?) {
                val item = itemView.tag as MUnitPhrase
                if (vm.isEditMode)
                    edit(item)
                else
                    speak(item.phrase)
            }

            override fun onItemLongClicked(view: View?): Boolean {
                val item = itemView.tag as MUnitPhrase
                // https://stackoverflow.com/questions/16389581/android-create-a-popup-that-has-multiple-selection-options
                AlertDialog.Builder(itemView.context)
                    .setTitle(item.phrase)
                    .setItems(arrayOf(
                        itemView.context.getString(R.string.action_delete),
                        itemView.context.getString(R.string.action_edit),
                        itemView.context.getString(R.string.action_copy_phrase),
                        itemView.context.getString(R.string.action_google_phrase),
                        itemView.context.getString(R.string.action_cancel),
                    )) { _, which ->
                        when (which) {
                            0 ->
                                yesNoDialog(itemView.context, "Are you sure you want to delete the phrase \"${item.phrase}\"?", {
                                    val pos = mDragListView.adapter.getPositionForItem(item)
                                    mDragListView.adapter.removeItem(pos)
                                    vm.delete(item)
                                }, {})
                            1 -> edit(item)
                            2 -> copyText(itemView.context, item.phrase)
                            3 -> googleString(itemView.context, item.phrase)
                            else -> {}
                        }
                    }.show()
                return true
            }
        }
    }
}
