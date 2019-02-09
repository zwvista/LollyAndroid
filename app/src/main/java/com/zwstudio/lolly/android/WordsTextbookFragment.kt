package com.zwstudio.lolly.android

import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.zwstudio.lolly.data.WordsTextbookViewModel
import com.zwstudio.lolly.domain.TextbookWord
import io.reactivex.disposables.CompositeDisposable
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EFragment
import org.androidannotations.annotations.ItemClick

@EFragment(R.layout.content_words_textbook)
class WordsTextbookFragment : DrawerListFragment() {

    @Bean
    lateinit var vm: WordsTextbookViewModel

    val compositeDisposable = CompositeDisposable()

    @AfterViews
    fun afterViews() {
        activity?.title = resources.getString(R.string.words_textbook)
        compositeDisposable.add(vm.getData().subscribe {
            val lst = it.lst!!
            val adapter = object : ArrayAdapter<TextbookWord>(activity!!,
                android.R.layout.simple_list_item_1, lst) {
                override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                    val v = super.getView(position, convertView, parent)
                    val tv = v.findViewById<TextView>(android.R.id.text1)
                    tv.text = lst[position].word
                    tv.setTextColor(resources.getColor(R.color.color_text1))
                    return v
                }
            }
            listView.adapter = adapter
            progressBar1.visibility = View.GONE
        })
    }

    @ItemClick
    fun listViewItemClicked(item: TextbookWord) {
        WordsDictActivity_.intent(activity).extra("word", item.word).start()
    }

}
