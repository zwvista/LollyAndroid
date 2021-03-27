package com.zwstudio.lolly.android.phrases

import android.app.Activity
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.androidisland.vita.VitaOwner
import com.androidisland.vita.vita
import com.zwstudio.lolly.android.R
import com.zwstudio.lolly.android.databinding.FragmentPhrasesLangDetailBinding
import com.zwstudio.lolly.android.vmSettings
import com.zwstudio.lolly.data.phrases.PhrasesLangDetailViewModel
import com.zwstudio.lolly.data.phrases.PhrasesLangViewModel
import com.zwstudio.lolly.domain.wpp.MLangPhrase
import org.androidannotations.annotations.*

@EActivity(R.layout.fragment_phrases_lang_detail)
class PhrasesLangDetailFragment : Fragment() {

    val vm by lazy { vita.with(VitaOwner.Multiple(this)).getViewModel<PhrasesLangViewModel>() }
    val vmDetail by lazy { vita.with(VitaOwner.Single(this)).getViewModel { PhrasesLangDetailViewModel(item) } }
    lateinit var binding: FragmentPhrasesLangDetailBinding
    lateinit var item: MLangPhrase

    @AfterViews
    fun afterViews() {
        item = intent.getSerializableExtra("phrase") as MLangPhrase
        binding = DataBindingUtil.inflate<FragmentPhrasesLangDetailBinding>(LayoutInflater.from(this), R.layout.fragment_phrases_lang_detail,
                findViewById(android.R.id.content), true).apply {
            lifecycleOwner = this@PhrasesLangDetailFragment
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
                item.phrase = vmSettings.autoCorrectInput(item.phrase)
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
