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
import com.zwstudio.lolly.android.databinding.FragmentPhrasesTextbookDetailBinding
import com.zwstudio.lolly.data.misc.makeCustomAdapter
import com.zwstudio.lolly.data.phrases.PhrasesUnitDetailViewModel
import com.zwstudio.lolly.data.phrases.PhrasesUnitViewModel
import com.zwstudio.lolly.domain.wpp.MUnitPhrase
import org.androidannotations.annotations.*

@EActivity(R.layout.fragment_phrases_textbook_detail)
class PhrasesTextbookDetailFragment : AppCompatActivity() {

    val vm by lazy { vita.with(VitaOwner.Multiple(this)).getViewModel<PhrasesUnitViewModel>() }
    val vmDetail by lazy { vita.with(VitaOwner.Single(this)).getViewModel { PhrasesUnitDetailViewModel(item) } }
    lateinit var binding: FragmentPhrasesTextbookDetailBinding
    lateinit var item: MUnitPhrase

    @AfterViews
    fun afterViews() {
        item = intent.getSerializableExtra("phrase") as MUnitPhrase
        binding = DataBindingUtil.inflate<FragmentPhrasesTextbookDetailBinding>(LayoutInflater.from(this), R.layout.fragment_phrases_textbook_detail,
            findViewById(android.R.id.content), true).apply {
            lifecycleOwner = this@PhrasesTextbookDetailFragment
            model = vmDetail
        }
        binding.spnUnit.adapter = makeCustomAdapter(this, vm.vmSettings.lstUnits) { it.label }
        binding.spnPart.adapter = makeCustomAdapter(this, vm.vmSettings.lstParts) { it.label }
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
                vm.update(item)
                setResult(Activity.RESULT_OK)
                true
            }
            else -> super.onOptionsItemSelected(menuItem)
        }
}
