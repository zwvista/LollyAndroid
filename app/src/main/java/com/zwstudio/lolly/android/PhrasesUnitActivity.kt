package com.zwstudio.lolly.android

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.zwstudio.lolly.data.PhrasesUnitViewModel
import com.zwstudio.lolly.domain.UnitPhrase
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity


@EActivity(R.layout.activity_phrases_unit)
class PhrasesUnitActivity : DrawerListActivity() {

    @Bean
    lateinit var vm: PhrasesUnitViewModel

    @AfterViews
    override fun afterViews() {
        super.afterViews()
        vm.getData {
            val lst = it.lst!!
            val adapter = object : ArrayAdapter<UnitPhrase>(this,
                android.R.layout.simple_list_item_2, android.R.id.text1, lst) {
                override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                    val v = super.getView(position, convertView, parent)
                    var tv = v.findViewById<View>(android.R.id.text1) as TextView
                    tv.text = lst[position].phrase
                    tv.setTextColor(Color.rgb(255, 165, 0))
                    tv = v.findViewById<View>(android.R.id.text2) as TextView
                    tv.text = lst[position].translation
                    tv.setTextColor(Color.BLUE)
                    return v
                }
            }
            listView.adapter = adapter
        }
    }

}
