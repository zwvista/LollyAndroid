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
import org.androidannotations.annotations.*

@EActivity(R.layout.activity_settings)
class SettingsActivity : DrawerActivity() {

    @App
    lateinit var app: LollyApplication
    val vm: SettingsViewModel
        get() = app.vm

    @ViewById(R.id.spnLanguage)
    lateinit var spnLanguage: Spinner
    @ViewById(R.id.spnDictionary)
    lateinit var spnDictionary: Spinner
    @ViewById(R.id.spnTextbook)
    lateinit var spnTextbook: Spinner
    @ViewById(R.id.spnUnitFrom)
    lateinit var spnUnitFrom: Spinner
    @ViewById(R.id.spnUnitTo)
    lateinit var spnUnitTo: Spinner
    @ViewById(R.id.spnPartFrom)
    lateinit var spnPartFrom: Spinner
    @ViewById(R.id.spnPartTo)
    lateinit var spnPartTo: Spinner
    @ViewById(R.id.chkUnitTo)
    lateinit var chkUnitTo: CheckBox

    @AfterViews
    override fun afterViews() {
        super.afterViews()
        initSpnLanguage()
    }

    @CheckedChange
    fun chkUnitToCheckedChanged(selected: Boolean) {
        spnPartTo.isEnabled = selected
        spnUnitTo.isEnabled = selected
        if (chkUnitTo.isChecked)
            updateUnitPartTo()
    }

    private fun updateUnitPartFrom() {
        vm.usunitfrom = vm.usunitto
        vm.updateUnitFrom {
            spnUnitFrom.setSelection(spnUnitTo.selectedItemPosition)
        }
        vm.uspartfrom = vm.uspartto
        vm.updatePartFrom {
            spnPartFrom.setSelection(spnPartTo.selectedItemPosition)
        }
    }

    private fun updateUnitPartTo() {
        vm.usunitto = vm.usunitfrom
        vm.updateUnitTo {
            spnUnitTo.setSelection(spnUnitFrom.selectedItemPosition)
        }
        vm.uspartto = vm.uspartfrom
        vm.updatePartTo {
            spnPartTo.setSelection(spnPartFrom.selectedItemPosition)
        }
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
    }

    @ItemSelect
    fun spnLanguageItemSelected(selected: Boolean, position: Int) {
        Log.d("", String.format("Checked position:%d", position))
        vm.setSelectedLangIndex(position) {
            vm.updateLang {
                initSpnDictionary()
                initSpnTextbook()
            }
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
    }

    @ItemSelect
    fun spnDictionaryItemSelected(selected: Boolean, position: Int) {
        vm.selectedDictIndex = position
        Log.d("", String.format("Checked position:%d", position))
        (spnDictionary.adapter as ArrayAdapter<Dictionary>).notifyDataSetChanged()
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
    }

    @ItemSelect
    fun spnTextbookItemSelected(selected: Boolean, position: Int) {
        vm.selectedTextbookIndex = position
        Log.d("", String.format("Checked position:%d", position))
        (spnTextbook.adapter as ArrayAdapter<Textbook>).notifyDataSetChanged()
        initUnitsAndParts()
    }

    private fun initUnitsAndParts() {
        val b = vm.usunitfrom != vm.usunitto
        chkUnitTo.isChecked = b
        chkUnitToCheckedChanged(b)

        run {
            val lst = vm.lstUnits
            val adapter = object : ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, lst) {
                override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                    val v = super.getView(position, convertView, parent)
                    val s = lst[position]
                    val tv = v.findViewById<View>(android.R.id.text1) as TextView
                    tv.text = s
                    return v
                }

                override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                    val v = super.getDropDownView(position, convertView, parent)
                    val s = lst[position]
                    val ctv = v.findViewById<View>(android.R.id.text1) as CheckedTextView
                    ctv.text = s
                    return v
                }
            }
            adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice)
            spnUnitFrom.adapter = adapter
            spnUnitTo.adapter = adapter

            spnUnitFrom.setSelection(vm.usunitfrom - 1)
            spnUnitTo.setSelection(vm.usunitto - 1)

        }

        run {
            val lst = vm.lstParts
            val adapter = object : ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, lst) {
                override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                    val v = super.getView(position, convertView, parent)
                    val s = lst[position]
                    val tv = v.findViewById<View>(android.R.id.text1) as TextView
                    tv.setText(s)
                    return v
                }

                override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                    val v = super.getDropDownView(position, convertView, parent)
                    val s = lst[position]
                    val ctv = v.findViewById<View>(android.R.id.text1) as CheckedTextView
                    ctv.setText(s)
                    return v
                }
            }
            adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice)
            spnPartFrom.adapter = adapter
            spnPartTo.adapter = adapter

            spnPartFrom.setSelection(vm.uspartfrom - 1)
            spnPartTo.setSelection(vm.uspartto - 1)

        }
    }

    private fun isInvalidUnitPart() = vm.usunitpartfrom > vm.usunitpartto

    @ItemSelect
    fun spnUnitFromItemSelected(selected: Boolean, position: Int) {
        vm.usunitfrom = position + 1
        vm.updateUnitFrom {
            if (!chkUnitTo.isChecked || isInvalidUnitPart())
                updateUnitPartTo()
        }
    }

    @ItemSelect
    fun spnPartFromItemSelected(selected: Boolean, position: Int) {
        vm.uspartfrom = position + 1
        vm.updatePartFrom {
            if (!chkUnitTo.isChecked || isInvalidUnitPart())
                updateUnitPartTo()
        }
    }

    @ItemSelect
    fun spnUnitToItemSelected(selected: Boolean, position: Int) {
        vm.usunitto = position + 1
        vm.updateUnitTo {
            if (isInvalidUnitPart())
                updateUnitPartFrom()
        }
    }

    @ItemSelect
    fun spnPartToItemSelected(selected: Boolean, position: Int) {
        vm.uspartto = position + 1
        vm.updatePartTo {
            if (isInvalidUnitPart())
                updateUnitPartFrom()
        }
    }

}
