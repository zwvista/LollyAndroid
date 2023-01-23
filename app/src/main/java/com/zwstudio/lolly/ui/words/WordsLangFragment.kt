package com.zwstudio.lolly.ui.words

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.woxthebox.draglistview.DragItemAdapter
import com.woxthebox.draglistview.DragListView
import com.zwstudio.lolly.MainActivity
import com.zwstudio.lolly.R
import com.zwstudio.lolly.common.*
import com.zwstudio.lolly.databinding.FragmentWordsLangBinding
import com.zwstudio.lolly.models.wpp.MLangWord
import com.zwstudio.lolly.ui.*
import com.zwstudio.lolly.ui.common.*
import com.zwstudio.lolly.ui.misc.*
import com.zwstudio.lolly.viewmodels.DrawerListViewModel
import com.zwstudio.lolly.viewmodels.misc.SettingsViewModel
import com.zwstudio.lolly.viewmodels.words.WordsLangViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.nio.file.Files.delete

class WordsLangFragment : DrawerListFragment(), MenuProvider {

    val vm by viewModel<WordsLangViewModel>()
    override val vmDrawerList: DrawerListViewModel get() = vm
    var binding by autoCleared<FragmentWordsLangBinding>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        requireActivity().addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        binding = FragmentWordsLangBinding.inflate(inflater, container, false).apply {
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
        binding.spnScopeFilter.adapter = makeCustomAdapter(requireContext(), SettingsViewModel.lstScopeWordFilters) { it.label }
        setupListView()

        vm.lstWords_.onEach {
            val listAdapter = WordsLangItemAdapter(vm, mDragListView, compositeDisposable)
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
                findNavController().navigate(WordsLangFragmentDirections.actionWordsLangFragmentToWordsLangDetailFragment(vm.newLangWord()))
                true
            }
            else -> false
        }

//    fun onResult(resultCode: Int) {
//        if (resultCode == Activity.RESULT_OK)
//            mDragListView.resetSwipedViews(null)
//    }

    private class WordsLangItemAdapter(val vm: WordsLangViewModel, val mDragListView: DragListView, val compositeDisposable: CompositeDisposable) : DragItemAdapter<MLangWord, WordsLangItemAdapter.ViewHolder>() {

        init {
            itemList = vm.lstWords
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_words_lang_edit, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            val item = mItemList[position]
            holder.mText1.text = item.word
            holder.mText2.text = item.note
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
                        val item = itemView.tag as MLangWord
                        navController.navigate(WordsLangFragmentDirections.actionWordsLangFragmentToWordsDictFragment(
                            vm.lstWords.map { it.word }.toTypedArray(), vm.lstWords.indexOf(item)
                        ))
                    }
                    true
                }
            }

            override fun onItemClicked(view: View?) {
                val item = itemView.tag as MLangWord
                speak(item.word)
            }

            override fun onItemLongClicked(view: View?): Boolean {
                val item = itemView.tag as MLangWord
                // https://stackoverflow.com/questions/16389581/android-create-a-popup-that-has-multiple-selection-options
                AlertDialog.Builder(itemView.context)
                    .setTitle(item.wordnote)
                    .setItems(arrayOf(
                        itemView.context.getString(R.string.action_delete),
                        itemView.context.getString(R.string.action_edit),
                        itemView.context.getString(R.string.action_retrieve_note),
                        itemView.context.getString(R.string.action_clear_note),
                        itemView.context.getString(R.string.action_copy_word),
                        itemView.context.getString(R.string.action_google_word),
                        itemView.context.getString(R.string.action_online_dict),
                        itemView.context.getString(R.string.action_cancel),
                    )) { _, which ->
                        when (which) {
                            0 ->
                                yesNoDialog(itemView.context, "Are you sure you want to delete the word \"${item.word}\"?", {
                                    val pos = mDragListView.adapter.getPositionForItem(item)
                                    mDragListView.adapter.removeItem(pos)
                                    compositeDisposable.add(vm.delete(item).subscribe())
                                }, {
                                    mDragListView.resetSwipedViews(null)
                                })
                            1 -> navController.navigate(WordsLangFragmentDirections.actionWordsLangFragmentToWordsLangDetailFragment(item))
                            2 -> {
                                val index = itemList.indexOf(item)
                                compositeDisposable.add(vm.getNote(item).subscribe {
                                    mDragListView.adapter.notifyItemChanged(index)
                                })
                            }
                            3 -> {
                                val index = itemList.indexOf(item)
                                compositeDisposable.add(vm.clearNote(item).subscribe {
                                    mDragListView.adapter.notifyItemChanged(index)
                                })
                            }
                            4 -> copyText(itemView.context, item.word)
                            5 -> googleString(itemView.context, item.word)
                            6 -> {
                                val url = vmSettings.selectedDictReference.urlString(item.word, vmSettings.lstAutoCorrect)
                                openPage(itemView.context, url)
                            }
                            else -> {}
                        }
                    }.show()
                return true
            }
        }
    }
}
