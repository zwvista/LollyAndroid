package com.zwstudio.lolly.android.patterns

import android.app.Activity
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.androidisland.vita.VitaOwner
import com.androidisland.vita.vita
import com.zwstudio.lolly.android.R
import com.zwstudio.lolly.android.databinding.FragmentPatternsWebpagesDetailBinding
import com.zwstudio.lolly.data.patterns.PatternsWebPageDetailViewModel
import com.zwstudio.lolly.data.patterns.PatternsWebPagesViewModel
import com.zwstudio.lolly.domain.wpp.MPatternWebPage
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.androidannotations.annotations.*

@EActivity(R.layout.fragment_patterns_webpages_detail)
class PatternsWebPagesDetailFragment : AppCompatActivity() {

    val vm by lazy { vita.with(VitaOwner.Multiple(this)).getViewModel<PatternsWebPagesViewModel>() }
    val vmDetail by lazy { vita.with(VitaOwner.Single(this)).getViewModel { PatternsWebPageDetailViewModel(item) } }
    lateinit var binding: FragmentPatternsWebpagesDetailBinding
    lateinit var item: MPatternWebPage

    val compositeDisposable = CompositeDisposable()

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
                    compositeDisposable.add(vm.createPatternWebPage(item).subscribe())
                else
                    compositeDisposable.add(vm.updatePatternWebPage(item).subscribe())
                setResult(Activity.RESULT_OK)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(menuItem)
        }
}
