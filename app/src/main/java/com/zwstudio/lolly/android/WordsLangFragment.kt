package com.zwstudio.lolly.android

import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.zwstudio.lolly.data.WordsLangViewModel
import com.zwstudio.lolly.domain.LangWord
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EFragment
import org.androidannotations.annotations.ItemClick

@EFragment(R.layout.content_words_lang)
class WordsLangFragment : DrawerListFragment() {

    @Bean
    lateinit var vm: WordsLangViewModel

    @AfterViews
    fun afterViews() {
        activity?.title = resources.getString(R.string.phrases_lang)
        vm.getData {
            val lst = it.lst!!
            val adapter = object : ArrayAdapter<LangWord>(activity,
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
        }
    }

    @ItemClick
    fun listViewItemClicked(item: LangWord) {
        WordDictActivity_.intent(activity).extra("word", item.word).start()
    }

}
