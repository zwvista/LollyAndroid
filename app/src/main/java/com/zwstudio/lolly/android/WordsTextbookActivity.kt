package com.zwstudio.lolly.android

import android.content.Intent
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import com.zwstudio.lolly.data.WordsTextbookViewModel
import com.zwstudio.lolly.domain.TextbookWord
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_words_textbook)
class WordsTextbookActivity : DrawerListActivity() {

    @Bean
    internal lateinit var vm: WordsTextbookViewModel

    @AfterViews
    override fun afterViews() {
        super.afterViews()
        vm.getData {
            val lst = it.lst!!
            val adapter = object : ArrayAdapter<TextbookWord>(this,
                    android.R.layout.simple_list_item_1, lst) {
                override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                    val v = super.getView(position, convertView, parent)
                    val tv = v.findViewById<View>(android.R.id.text1) as TextView
                    tv.text = lst[position].word
                    tv.setTextColor(Color.BLUE)
                    return v
                }
            }
            lv.adapter = adapter

            lv.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
                val intent = Intent(applicationContext, WordsDictActivity::class.java)
                intent.putExtra("word", lst[position].word)
                startActivity(intent)
            }
        }
    }

}
