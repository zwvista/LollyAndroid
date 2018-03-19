package com.zwstudio.lolly.android

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.zwstudio.lolly.data.WordsTextbookViewModel
import com.zwstudio.lolly.domain.TextbookWord
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EFragment
import org.androidannotations.annotations.ItemClick

@EFragment(R.layout.content_words_textbook)
class WordsTextbookFragment : DrawerListFragment() {

    @Bean
    lateinit var vm: WordsTextbookViewModel

    @AfterViews
    fun afterViews() {
        vm.getData {
            val lst = it.lst!!
            val adapter = object : ArrayAdapter<TextbookWord>(activity,
                android.R.layout.simple_list_item_1, lst) {
                override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                    val v = super.getView(position, convertView, parent)
                    val tv = v.findViewById<View>(android.R.id.text1) as TextView
                    tv.text = lst[position].word
                    tv.setTextColor(Color.BLUE)
                    return v
                }
            }
            listView.adapter = adapter
        }
    }

    @ItemClick
    fun listViewItemClicked(item: TextbookWord) {
        WordsDictActivity_.intent(activity).extra("word", item.word).start()
    }

}
