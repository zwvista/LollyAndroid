package com.zwstudio.lolly.android

import android.graphics.Color
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.zwstudio.lolly.data.SettingsViewModel
import com.zwstudio.lolly.domain.Dictionary
import com.zwstudio.lolly.domain.Language
import com.zwstudio.lolly.domain.Textbook
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity
import org.androidannotations.annotations.ViewById

@EActivity(R.layout.activity_settings)
class SettingsActivity : DrawerActivity() {
    
    @Bean
    internal lateinit var vm: SettingsViewModel

    @ViewById(R.id.spnLanguage)
    internal lateinit var spnLanguage: Spinner
    @ViewById(R.id.spnDictionary)
    internal lateinit var spnDictionary: Spinner
    @ViewById(R.id.spnTextbook)
    internal lateinit var spnTextbook: Spinner
    @ViewById(R.id.spnUnitFrom)
    internal lateinit var spnUnitFrom: Spinner
    @ViewById(R.id.spnUnitTo)
    internal lateinit var spnUnitTo: Spinner
    @ViewById(R.id.spnPartFrom)
    internal lateinit var spnPartFrom: Spinner
    @ViewById(R.id.spnPartTo)
    internal lateinit var spnPartTo: Spinner
    @ViewById(R.id.chkUnitTo)
    internal lateinit var chkUnitTo: CheckBox

    @AfterViews
    override fun afterViews() {
        super.afterViews()
        chkUnitTo.setOnCheckedChangeListener { compoundButton, b -> chkUnitTo_onCheckedChanged(b) }
        initSpnLanguage()
    }

    private fun chkUnitTo_onCheckedChanged(b: Boolean) {
        spnPartTo.isEnabled = b
        spnUnitTo.isEnabled = b
        if (chkUnitTo.isChecked)
            updateUnitPartTo()
    }

    private fun updateUnitPartFrom() {
//        val m = vm.selectedTextbook
//        m.usunitfrom = m.usunitto
//        spnUnitFrom.setSelection(spnUnitTo.selectedItemPosition)
//        m.uspartfrom = m.uspartto
//        spnPartFrom.setSelection(spnPartTo.selectedItemPosition)
    }

    private fun updateUnitPartTo() {
//        val m = vm.selectedTextbook
//        m.usunitto = m.usunitfrom
//        spnUnitTo.setSelection(spnUnitFrom.selectedItemPosition)
//        m.uspartto = m.uspartfrom
//        spnPartTo.setSelection(spnPartFrom.selectedItemPosition)
    }

    private fun initSpnLanguage() {
        val lst = vm.lstLanguages
        val adapter = object : ArrayAdapter<Language>(this,
                android.R.layout.simple_spinner_item, lst) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val v = super.getView(position, convertView, parent)
                val tv = v.findViewById<View>(android.R.id.text1) as TextView
                tv.text = lst[position].langname
                tv.setTextColor(Color.BLUE)
                return v
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val v = super.getDropDownView(position, convertView, parent)
                val ctv = v.findViewById<View>(android.R.id.text1) as CheckedTextView
                ctv.text = lst[position].langname
                ctv.setTextColor(Color.BLUE)
                return v
            }
        }
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice)
        spnLanguage.adapter = adapter

        spnLanguage.setSelection(vm.selectedLangIndex)
        spnLanguage.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                vm.selectedLangIndex = position
                Log.d("", String.format("Checked position:%d", position))
                initSpnDictionary()
                initSpnTextbook()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun initSpnDictionary() {
        val lst = vm.lstDictionaries
        val adapter = object : ArrayAdapter<Dictionary>(this,
                R.layout.spinner_item_2, android.R.id.text1, lst) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val v = super.getView(position, convertView, parent)
                val m = lst[position]
                var tv = v.findViewById<View>(android.R.id.text1) as TextView
                tv.text = m.dictname
                tv = v.findViewById<View>(android.R.id.text2) as TextView
                tv.text = m.url
                return v
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val v = super.getDropDownView(position, convertView, parent)
                val m = lst[position]
                val ctv = v.findViewById<View>(android.R.id.text1) as CheckedTextView
                ctv.text = m.dictname
                ctv.isChecked = spnDictionary.selectedItemPosition == position
                val tv = v.findViewById<View>(android.R.id.text2) as TextView
                tv.text = m.url
                return v
            }
        }
        adapter.setDropDownViewResource(R.layout.list_item_2)
        spnDictionary.adapter = adapter

        spnDictionary.setSelection(vm.selectedDictIndex)
        spnDictionary.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                vm.selectedDictIndex = position
                Log.d("", String.format("Checked position:%d", position))
                adapter.notifyDataSetChanged()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun initSpnTextbook() {
        val lst = vm.lstTextbooks
        val adapter = object : ArrayAdapter<Textbook>(this,
                R.layout.spinner_item_2, android.R.id.text1, lst) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val v = super.getView(position, convertView, parent)
                val m = lst[position]
                var tv = v.findViewById<View>(android.R.id.text1) as TextView
                tv.text = m.textbookname
                tv = v.findViewById<View>(android.R.id.text2) as TextView
                tv.text = m.units.toString() + " units"
                return v
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val v = super.getDropDownView(position, convertView, parent)
                val m = lst[position]
                val ctv = v.findViewById<View>(android.R.id.text1) as CheckedTextView
                ctv.text = m.textbookname
                ctv.isChecked = spnTextbook.selectedItemPosition == position
                val tv = v.findViewById<View>(android.R.id.text2) as TextView
                tv.text = m.units.toString() + " units"
                return v
            }
        }
        adapter.setDropDownViewResource(R.layout.list_item_2)
        spnTextbook.adapter = adapter

        spnTextbook.setSelection(vm.selectedTextbookIndex)
        spnTextbook.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                vm.selectedTextbookIndex = position
                Log.d("", String.format("Checked position:%d", position))
                adapter.notifyDataSetChanged()
                initUnitsAndParts()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun initUnitsAndParts() {
//        val m = vm.selectedTextbook
//        val isInvalidUnitPart = { m.usunitfrom * 10 + m.uspartfrom > m.usunitto * 10 + m.uspartto }
//        val b = m.usunitfrom != m.usunitto
//        chkUnitTo.isChecked = b
//        chkUnitTo_onCheckedChanged(b)
//
//        run {
//            val lst = (1..m.units).map { it.toString() }
//            val adapter = object : ArrayAdapter<String>(this,
//                    android.R.layout.simple_spinner_item, lst) {
//                override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//                    val v = super.getView(position, convertView, parent)
//                    val s = lst[position]
//                    val tv = v.findViewById<View>(android.R.id.text1) as TextView
//                    tv.text = s
//                    return v
//                }
//
//                override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
//                    val v = super.getDropDownView(position, convertView, parent)
//                    val s = lst[position]
//                    val ctv = v.findViewById<View>(android.R.id.text1) as CheckedTextView
//                    ctv.text = s
//                    return v
//                }
//            }
//            adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice)
//            spnUnitFrom.adapter = adapter
//            spnUnitTo.adapter = adapter
//
//            spnUnitFrom.setSelection(m.usunitfrom - 1)
//            spnUnitTo.setSelection(m.usunitto - 1)
//
//            spnUnitFrom.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//                override fun onItemSelected(adapterView: AdapterView<*>, view: View, position: Int, id: Long) {
//                    m.usunitfrom = position + 1
//                    if (!chkUnitTo.isChecked || isInvalidUnitPart())
//                        updateUnitPartTo()
//                }
//
//                override fun onNothingSelected(adapterView: AdapterView<*>) {}
//            }
//            spnUnitTo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//                override fun onItemSelected(adapterView: AdapterView<*>, view: View, position: Int, id: Long) {
//                    m.usunitto = position + 1
//                    if (isInvalidUnitPart())
//                        updateUnitPartFrom()
//                }
//
//                override fun onNothingSelected(adapterView: AdapterView<*>) {}
//            }
//        }
//
//        run {
//            val lst = m.parts?.split(' ')!!
//            val adapter = object : ArrayAdapter<String>(this,
//                    android.R.layout.simple_spinner_item, lst) {
//                override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//                    val v = super.getView(position, convertView, parent)
//                    val s = lst[position]
//                    val tv = v.findViewById<View>(android.R.id.text1) as TextView
//                    tv.setText(s)
//                    return v
//                }
//
//                override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
//                    val v = super.getDropDownView(position, convertView, parent)
//                    val s = lst[position]
//                    val ctv = v.findViewById<View>(android.R.id.text1) as CheckedTextView
//                    ctv.setText(s)
//                    return v
//                }
//            }
//            adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice)
//            spnPartFrom.adapter = adapter
//            spnPartTo.adapter = adapter
//
//            spnPartFrom.setSelection(m.uspartfrom - 1)
//            spnPartTo.setSelection(m.uspartto - 1)
//
//            spnPartFrom.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//                override fun onItemSelected(adapterView: AdapterView<*>, view: View, position: Int, id: Long) {
//                    m.uspartfrom = position + 1
//                    if (!chkUnitTo.isChecked || isInvalidUnitPart())
//                        updateUnitPartTo()
//                }
//
//                override fun onNothingSelected(adapterView: AdapterView<*>) {}
//            }
//            spnPartTo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//                override fun onItemSelected(adapterView: AdapterView<*>, view: View, position: Int, id: Long) {
//                    m.uspartto = position + 1
//                    if (isInvalidUnitPart())
//                        updateUnitPartFrom()
//                }
//
//                override fun onNothingSelected(adapterView: AdapterView<*>) {}
//            }
//        }
    }

}
