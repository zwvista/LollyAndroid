package com.zwstudio.lolly.android

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.zwstudio.lolly.data.PhrasesLangViewModel
import com.zwstudio.lolly.domain.LangPhrase
import io.reactivex.disposables.CompositeDisposable
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EFragment

@EFragment(R.layout.content_phrases_lang)
class PhrasesLangFragment : DrawerListFragment() {

    @Bean
    lateinit var vm: PhrasesLangViewModel

    val compositeDisposable = CompositeDisposable()

    @AfterViews
    fun afterViews() {
        activity?.title = resources.getString(R.string.phrases_lang)
        compositeDisposable.add(vm.getData().subscribe {
            val lst = it.lst!!
            val adapter = object : ArrayAdapter<LangPhrase>(activity!!,
                android.R.layout.simple_list_item_2, android.R.id.text1, lst) {
                override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                    val v = super.getView(position, convertView, parent)
                    var tv = v.findViewById<TextView>(android.R.id.text1)
                    tv.text = lst[position].phrase
                    tv.setTextColor(Color.rgb(255, 165, 0))
                    tv = v.findViewById<TextView>(android.R.id.text2)
                    tv.text = lst[position].translation
                    tv.setTextColor(Color.BLUE)
                    return v
                }
            }
            listView.adapter = adapter
            progressBar1.visibility = View.GONE
        })
    }

}
