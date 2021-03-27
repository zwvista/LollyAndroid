package com.zwstudio.lolly.android.words

import android.app.Activity
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.androidisland.vita.VitaOwner
import com.androidisland.vita.vita
import com.zwstudio.lolly.android.R
import com.zwstudio.lolly.android.databinding.FragmentWordsTextbookDetailBinding
import com.zwstudio.lolly.android.vmSettings
import com.zwstudio.lolly.data.misc.makeCustomAdapter
import com.zwstudio.lolly.data.words.WordsUnitDetailViewModel
import com.zwstudio.lolly.data.words.WordsUnitViewModel
import com.zwstudio.lolly.domain.wpp.MUnitWord
import org.androidannotations.annotations.*

@EActivity(R.layout.fragment_words_textbook_detail)
class WordsTextbookDetailFragment : Fragment() {

    val vm by lazy { vita.with(VitaOwner.Multiple(this)).getViewModel<WordsUnitViewModel>() }
    val vmDetail by lazy { vita.with(VitaOwner.Single(this)).getViewModel { WordsUnitDetailViewModel(item) } }
    lateinit var binding: FragmentWordsTextbookDetailBinding
    lateinit var item: MUnitWord

    @AfterViews
    fun afterViews() {
        item = intent.getSerializableExtra("word") as MUnitWord
        binding = DataBindingUtil.inflate<FragmentWordsTextbookDetailBinding>(LayoutInflater.from(this), R.layout.fragment_words_textbook_detail,
            findViewById(android.R.id.content), true).apply {
            lifecycleOwner = this@WordsTextbookDetailFragment
            model = vmDetail
        }
        binding.spnUnit.adapter = makeCustomAdapter(this, vmSettings.lstUnits) { it.label }
        binding.spnPart.adapter = makeCustomAdapter(this, vmSettings.lstParts) { it.label }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_save, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean =
        when (menuItem.itemId) {
            R.id.menuSave -> {
                vmDetail.save(item)
                item.word = vmSettings.autoCorrectInput(item.word)
                vm.update(item)
                setResult(Activity.RESULT_OK)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(menuItem)
        }
}
