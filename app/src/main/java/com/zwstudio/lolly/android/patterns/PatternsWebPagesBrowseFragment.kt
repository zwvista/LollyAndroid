package com.zwstudio.lolly.android.patterns

import android.webkit.WebView
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.androidisland.vita.VitaOwner
import com.androidisland.vita.vita
import com.zwstudio.lolly.android.R
import com.zwstudio.lolly.data.misc.makeAdapter
import com.zwstudio.lolly.data.patterns.PatternsWebPagesViewModel
import com.zwstudio.lolly.domain.wpp.MPattern
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.EActivity
import org.androidannotations.annotations.ItemSelect
import org.androidannotations.annotations.ViewById

class PatternsWebPagesBrowseFragment : Fragment() {

    val vm by lazy { vita.with(VitaOwner.Multiple(this)).getViewModel<PatternsWebPagesViewModel>() }
    lateinit var item: MPattern

    val compositeDisposable = CompositeDisposable()

    @ViewById
    lateinit var spnWebPages: Spinner
    @ViewById
    lateinit var webView: WebView

    @AfterViews
    fun afterViews() {
        item = intent.getSerializableExtra("pattern") as MPattern
        vm.getWebPages(item.id)
        val lst = vm.lstWebPages
        val adapter = makeAdapter(this, android.R.layout.simple_spinner_item, lst) { v, position ->
            val tv = v.findViewById<TextView>(android.R.id.text1)
            tv.text = getItem(position)!!.title
            v
        }
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1)
        spnWebPages.adapter = adapter
        spnWebPages.setSelection(0)
    }

    @ItemSelect
    fun spnWebPagesItemSelected(selected: Boolean, position: Int) {
        webView.loadUrl(vm.lstWebPages[position].url)
    }
}
