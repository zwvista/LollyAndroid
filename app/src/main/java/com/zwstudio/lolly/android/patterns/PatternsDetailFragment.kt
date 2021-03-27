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
import com.zwstudio.lolly.android.databinding.FragmentPatternsDetailBinding
import com.zwstudio.lolly.android.vmSettings
import com.zwstudio.lolly.data.patterns.PatternsDetailViewModel
import com.zwstudio.lolly.data.patterns.PatternsViewModel
import com.zwstudio.lolly.domain.wpp.MPattern
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.androidannotations.annotations.*

class PatternsDetailFragment : Fragment() {

    val vm by lazy { vita.with(VitaOwner.Multiple(this)).getViewModel<PatternsViewModel>() }
    val vmDetail by lazy { vita.with(VitaOwner.Single(this)).getViewModel { PatternsDetailViewModel(item) } }
    lateinit var binding: FragmentPatternsDetailBinding
    lateinit var item: MPattern

    val compositeDisposable = CompositeDisposable()

    @AfterViews
    fun afterViews() {
        item = intent.getSerializableExtra("pattern") as MPattern
        binding = DataBindingUtil.inflate<FragmentPatternsDetailBinding>(LayoutInflater.from(this), R.layout.fragment_patterns_detail,
                findViewById(android.R.id.content), true).apply {
            lifecycleOwner = this@PatternsDetailFragment
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
                item.pattern = vmSettings.autoCorrectInput(item.pattern)
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
