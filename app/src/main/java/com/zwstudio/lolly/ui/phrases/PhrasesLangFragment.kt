package com.zwstudio.lolly.ui.phrases

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.woxthebox.draglistview.DragItemAdapter
import com.woxthebox.draglistview.DragListView
import com.zwstudio.lolly.MainActivity
import com.zwstudio.lolly.R
import com.zwstudio.lolly.common.copyText
import com.zwstudio.lolly.common.googleString
import com.zwstudio.lolly.common.speak
import com.zwstudio.lolly.databinding.FragmentPhrasesLangBinding
import com.zwstudio.lolly.models.wpp.MLangPhrase
import com.zwstudio.lolly.ui.common.DrawerListFragment
import com.zwstudio.lolly.ui.common.autoCleared
import com.zwstudio.lolly.ui.common.makeCustomAdapter
import com.zwstudio.lolly.ui.common.yesNoDialog
import com.zwstudio.lolly.viewmodels.DrawerListViewModel
import com.zwstudio.lolly.viewmodels.misc.SettingsViewModel
import com.zwstudio.lolly.viewmodels.phrases.PhrasesLangViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class PhrasesLangFragment : DrawerListFragment(), MenuProvider {

    val vm by viewModel<PhrasesLangViewModel>()
    override val vmDrawerList: DrawerListViewModel get() = vm
    var binding by autoCleared<FragmentPhrasesLangBinding>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        requireActivity().addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        binding = FragmentPhrasesLangBinding.inflate(inflater, container, false).apply {
            model = vm
            lifecycleOwner = viewLifecycleOwner
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
        setupListView()

        vm.lstPhrases_.onEach {
            val listAdapter = PhrasesLangItemAdapter(vm, mDragListView, compositeDisposable)
            mDragListView.setAdapter(listAdapter, true)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        vm.isBusy_.onEach {
            progressBar1.visibility = if (it) View.VISIBLE else View.GONE
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        compositeDisposable.add(vm.getData().subscribe())
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_add, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean =
        when (menuItem.itemId) {
            R.id.menuAdd -> {
                findNavController().navigate(PhrasesLangFragmentDirections.actionPhrasesLangFragmentToPhrasesLangDetailFragment(vm.newLangPhrase()))
                true
            }
            else -> false
        }

    private class PhrasesLangItemAdapter(val vm: PhrasesLangViewModel, val mDragListView: DragListView, val compositeDisposable: CompositeDisposable) : DragItemAdapter<MLangPhrase, PhrasesLangItemAdapter.ViewHolder>() {

        init {
            itemList = vm.lstPhrases
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_phrases_lang_edit, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            val item = mItemList[position]
            holder.mText1.text = item.phrase
            holder.mText2.text = item.translation
            holder.itemView.tag = item
        }

        override fun getUniqueItemId(position: Int): Long {
            return mItemList[position].id.toLong()
        }

        inner class ViewHolder(itemView: View) : DragItemAdapter.ViewHolder(itemView, R.id.image_hamburger, false) {
            var mText1: TextView = itemView.findViewById(R.id.text1)
            var mText2: TextView = itemView.findViewById(R.id.text2)
            val navController get() = (itemView.context as MainActivity).getNavController()

            override fun onItemClicked(view: View?) {
                val item = itemView.tag as MLangPhrase
                speak(item.phrase)
            }

            override fun onItemLongClicked(view: View?): Boolean {
                val item = itemView.tag as MLangPhrase
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
                                    compositeDisposable.add(vm.delete(item).subscribe())
                                }, {})
                            1 -> navController.navigate(PhrasesLangFragmentDirections.actionPhrasesLangFragmentToPhrasesLangDetailFragment(item))
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
