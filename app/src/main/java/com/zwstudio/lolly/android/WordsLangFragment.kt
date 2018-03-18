package com.zwstudio.lolly.android

import android.content.Intent
import android.graphics.Color
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
        vm.getData {
            val lst = it.lst!!
            val adapter = object : ArrayAdapter<LangWord>(activity,
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
    fun listViewItemClicked(item: LangWord) {
        val intent = Intent(activity, WordsDictActivity_::class.java)
        intent.putExtra("word", item.word)
        startActivity(intent)
    }

}
