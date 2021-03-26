package com.zwstudio.lolly.android.phrases

import android.app.Activity
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.androidisland.vita.VitaOwner
import com.androidisland.vita.vita
import com.zwstudio.lolly.android.R
import com.zwstudio.lolly.android.databinding.FragmentPhrasesLangDetailBinding
import com.zwstudio.lolly.data.phrases.PhrasesLangDetailViewModel
import com.zwstudio.lolly.data.phrases.PhrasesLangViewModel
import com.zwstudio.lolly.domain.wpp.MLangPhrase
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.androidannotations.annotations.*

@EActivity(R.layout.fragment_phrases_lang_detail)
class PhrasesLangDetailFragment : AppCompatActivity() {

    val vm by lazy { vita.with(VitaOwner.Multiple(this)).getViewModel<PhrasesLangViewModel>() }
    val vmDetail by lazy { vita.with(VitaOwner.Single(this)).getViewModel { PhrasesLangDetailViewModel(item) } }
    lateinit var binding: FragmentPhrasesLangDetailBinding
    lateinit var item: MLangPhrase

    val compositeDisposable = CompositeDisposable()

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
                item.phrase = vm.vmSettings.autoCorrectInput(item.phrase)
                if (item.id == 0)
                    compositeDisposable.add(vm.create(item).subscribe())
                else
                    compositeDisposable.add(vm.update(item).subscribe())
                setResult(Activity.RESULT_OK)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(menuItem)
        }
}
