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
import org.androidannotations.annotations.EActivity
import org.androidannotations.annotations.ItemClick

@EActivity(R.layout.activity_words_lang)
class WordsLangActivity : DrawerListActivity() {

    @Bean
    lateinit var vm: WordsLangViewModel

    @AfterViews
    override fun afterViews() {
        super.afterViews()
        vm.getData {
            val lst = it.lst!!
            val adapter = object : ArrayAdapter<LangWord>(this,
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
        val intent = Intent(applicationContext, WordsDictActivity_::class.java)
        intent.putExtra("word", item.word)
        startActivity(intent)
    }

}
