package com.zwstudio.lolly.android

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.zwstudio.lolly.data.WordsUnitViewModel
import com.zwstudio.lolly.domain.UnitWord
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EFragment
import org.androidannotations.annotations.ItemClick

@EFragment(R.layout.content_words_unit)
class WordsUnitFragment : DrawerListFragment() {

    @Bean
    lateinit var vm: WordsUnitViewModel
    lateinit var lst: List<UnitWord>

    @AfterViews
    fun afterViews() {
        activity?.title = "Words in Unit"
        vm.getData {
            lst = it.lst!!
            val adapter = object : ArrayAdapter<UnitWord>(activity,
                android.R.layout.simple_list_item_1, lst) {
                override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                    val v = super.getView(position, convertView, parent)
                    val tv = v.findViewById<TextView>(android.R.id.text1)
                    tv.text = lst[position].word
                    tv.setTextColor(Color.BLUE)
                    return v
                }
            }
            listView.adapter = adapter
            progressBar1.visibility = View.GONE
        }
    }

    @ItemClick
    fun listViewItemClicked(item: UnitWord) {
//        WordsDictActivity_.intent(activity).extra("word", item.word).start()
        WordsEditActivity_.intent(activity).extra("lst", lst.toTypedArray()).start()
    }

}
