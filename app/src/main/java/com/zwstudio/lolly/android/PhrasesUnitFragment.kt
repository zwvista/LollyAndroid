package com.zwstudio.lolly.android

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.zwstudio.lolly.data.PhrasesUnitViewModel
import com.zwstudio.lolly.domain.UnitPhrase
import org.androidannotations.annotations.*


@EFragment(R.layout.content_phrases_unit)
@OptionsMenu(R.menu.menu_words_phrases_edit)
class PhrasesUnitFragment : DrawerListFragment() {

    @Bean
    lateinit var vm: PhrasesUnitViewModel
    lateinit var lst: List<UnitPhrase>

    @AfterViews
    fun afterViews() {
        activity?.title = "Phrases in Unit"
        vm.getData {
            lst = it.lst!!
            val adapter = object : ArrayAdapter<UnitPhrase>(activity,
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
        }
    }

    @OptionsItem
    fun menuEdit() {
        PhrasesUnitEditActivity_.intent(activity).extra("lst", lst.toTypedArray()).start()
    }

}
