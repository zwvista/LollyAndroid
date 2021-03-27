package com.zwstudio.lolly.android.patterns

import android.app.Activity
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.androidisland.vita.VitaOwner
import com.androidisland.vita.vita
import com.zwstudio.lolly.android.R
import com.zwstudio.lolly.android.databinding.FragmentPatternsWebpagesDetailBinding
import com.zwstudio.lolly.data.patterns.PatternsWebPageDetailViewModel
import com.zwstudio.lolly.data.patterns.PatternsWebPagesViewModel
import com.zwstudio.lolly.domain.wpp.MPatternWebPage
import org.androidannotations.annotations.*

class PatternsWebPagesDetailFragment : Fragment() {

    val vm by lazy { vita.with(VitaOwner.Multiple(this)).getViewModel<PatternsWebPagesViewModel>() }
    val vmDetail by lazy { vita.with(VitaOwner.Single(this)).getViewModel { PatternsWebPageDetailViewModel(item) } }
    lateinit var binding: FragmentPatternsWebpagesDetailBinding
    lateinit var item: MPatternWebPage

    @AfterViews
    fun afterViews() {
        item = intent.getSerializableExtra("webpage") as MPatternWebPage
        binding = DataBindingUtil.inflate<FragmentPatternsWebpagesDetailBinding>(LayoutInflater.from(this), R.layout.fragment_patterns_webpages_detail,
                findViewById(android.R.id.content), true).apply {
            lifecycleOwner = this@PatternsWebPagesDetailFragment
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
                if (item.id == 0)
                    vm.createPatternWebPage(item)
                else
                    vm.updatePatternWebPage(item)
                setResult(Activity.RESULT_OK)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(menuItem)
        }
}
