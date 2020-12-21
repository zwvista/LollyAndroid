package com.zwstudio.lolly.android.patterns

import android.webkit.WebView
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.zwstudio.lolly.android.R
import com.zwstudio.lolly.data.misc.makeAdapter
import com.zwstudio.lolly.data.patterns.PatternsViewModel
import com.zwstudio.lolly.domain.wpp.MPattern
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.androidannotations.annotations.*

@EActivity(R.layout.activity_patterns_webpages_browse)
class PatternsWebPagesBrowseActivity : AppCompatActivity() {

    @Bean
    lateinit var vm: PatternsViewModel
    lateinit var item: MPattern

    val compositeDisposable = CompositeDisposable()

    @ViewById
    lateinit var spnWebPages: Spinner
    @ViewById
    lateinit var webView: WebView

    @AfterViews
    fun afterViews() {
        item = intent.getSerializableExtra("pattern") as MPattern
        compositeDisposable.add(vm.getWebPages(item.id).subscribe {
            val lst = vm.lstWebPages
            val adapter = makeAdapter(this, android.R.layout.simple_spinner_item, lst) { v, position ->
                val tv = v.findViewById<TextView>(android.R.id.text1)
                tv.text = getItem(position)!!.title
                v
            }
            adapter.setDropDownViewResource(android.R.layout.simple_list_item_1)
            spnWebPages.adapter = adapter
            spnWebPages.setSelection(0)
        })
    }

    @ItemSelect
    fun spnWebPagesItemSelected(selected: Boolean, position: Int) {
        webView.loadUrl(vm.lstWebPages[position].url)
    }
}
