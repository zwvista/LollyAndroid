package com.zwstudio.lolly.android.words

import android.app.Activity
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.androidisland.vita.VitaOwner
import com.androidisland.vita.vita
import com.zwstudio.lolly.android.R
import com.zwstudio.lolly.android.databinding.FragmentWordsLangDetailBinding
import com.zwstudio.lolly.data.words.WordsLangDetailViewModel
import com.zwstudio.lolly.data.words.WordsLangViewModel
import com.zwstudio.lolly.domain.wpp.MLangWord
import org.androidannotations.annotations.*

@EActivity(R.layout.fragment_words_lang_detail)
class WordsLangDetailFragment : AppCompatActivity() {

    val vm by lazy { vita.with(VitaOwner.Multiple(this)).getViewModel<WordsLangViewModel>() }
    val vmDetail by lazy { vita.with(VitaOwner.Single(this)).getViewModel { WordsLangDetailViewModel(item) } }
    lateinit var binding: FragmentWordsLangDetailBinding
    lateinit var item: MLangWord

    @AfterViews
    fun afterViews() {
        item = intent.getSerializableExtra("word") as MLangWord
        binding = DataBindingUtil.inflate<FragmentWordsLangDetailBinding>(LayoutInflater.from(this), R.layout.fragment_words_lang_detail,
                findViewById(android.R.id.content), true).apply {
            lifecycleOwner = this@WordsLangDetailFragment
            model = vmDetail
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_save, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean =
        when (menuItem.itemId) {
            R.id.menuSave -> {
                vmDetail.save(item)
                item.word = vm.vmSettings.autoCorrectInput(item.word)
                if (item.id == 0)
                    vm.create(item)
                else
                    vm.update(item)
                setResult(Activity.RESULT_OK)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(menuItem)
        }
}
