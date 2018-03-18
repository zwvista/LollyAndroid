package com.zwstudio.lolly.android

import android.graphics.Color
import android.support.v4.app.Fragment
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.zwstudio.lolly.data.SettingsViewModel
import com.zwstudio.lolly.domain.Dictionary
import com.zwstudio.lolly.domain.Language
import com.zwstudio.lolly.domain.Textbook
import org.androidannotations.annotations.*

@EFragment(R.layout.content_settings)
class SettingsFragment : Fragment() {

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
    fun afterViews() {
        vm.getData {
            initSpnLanguage()
        }
    }

    @CheckedChange
    fun chkUnitToCheckedChanged(selected: Boolean) {
        spnPartTo.isEnabled = selected
        spnUnitTo.isEnabled = selected
        if (!chkUnitTo.isChecked)
            updateUnitPartTo()
    }

    private fun updateUnitPartFrom() {
        if (vm.usunitfrom != vm.usunitto) {
            vm.usunitfrom = vm.usunitto
            vm.updateUnitFrom {
                spnUnitFrom.setSelection(spnUnitTo.selectedItemPosition)
            }
        }
        if (vm.uspartfrom != vm.uspartto) {
            vm.uspartfrom = vm.uspartto
            vm.updatePartFrom {
                spnPartFrom.setSelection(spnPartTo.selectedItemPosition)
            }
        }
    }

    private fun updateUnitPartTo() {
        if (vm.usunitto != vm.usunitfrom) {
            vm.usunitto = vm.usunitfrom
            vm.updateUnitTo {
                spnUnitTo.setSelection(spnUnitFrom.selectedItemPosition)
            }
        }
        if (vm.uspartto != vm.uspartfrom) {
            vm.uspartto = vm.uspartfrom
            vm.updatePartTo {
                spnPartTo.setSelection(spnPartFrom.selectedItemPosition)
            }
        }
    }

    private fun initSpnLanguage() {
        val lst = vm.lstLanguages
        val adapter = object : ArrayAdapter<Language>(activity,
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
        updateLang()
    }

    @ItemSelect
    fun spnLanguageItemSelected(selected: Boolean, position: Int) {
        if (vm.selectedLangIndex == position) return
        Log.d("", String.format("Checked position:%d", position))
        vm.setSelectedLangIndex(position) {
            vm.updateLang {
                updateLang()
            }
        }
    }

    private fun updateLang() {
        run {
            val lst = vm.lstDictionaries
            val adapter = object : ArrayAdapter<Dictionary>(activity,
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
        run {
            val lst = vm.lstTextbooks
            val adapter = object : ArrayAdapter<Textbook>(activity,
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
            updateTextbook()
        }
    }

    @ItemSelect
    fun spnDictionaryItemSelected(selected: Boolean, position: Int) {
        if (vm.selectedDictIndex == position) return
        vm.selectedDictIndex = position
        Log.d("", String.format("Checked position:%d", position))
        (spnDictionary.adapter as ArrayAdapter<Dictionary>).notifyDataSetChanged()
        vm.updateDict {  }
    }

    @ItemSelect
    fun spnTextbookItemSelected(selected: Boolean, position: Int) {
        if (vm.selectedTextbookIndex == position) return
        vm.selectedTextbookIndex = position
        Log.d("", String.format("Checked position:%d", position))
        (spnTextbook.adapter as ArrayAdapter<Textbook>).notifyDataSetChanged()
        vm.updateTextbook {
            updateTextbook()
        }
    }

    private fun updateTextbook() {
        run {
            val lst = vm.lstUnits
            val adapter = object : ArrayAdapter<String>(activity,
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
            val adapter = object : ArrayAdapter<String>(activity,
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

        val b = !vm.isSingleUnitPart
        chkUnitTo.isChecked = b
    }

    @ItemSelect
    fun spnUnitFromItemSelected(selected: Boolean, position: Int) {
        if (vm.usunitfrom == position + 1) return
        vm.usunitfrom = position + 1
        vm.updateUnitFrom {
            if (!chkUnitTo.isChecked || vm.isInvalidUnitPart)
                updateUnitPartTo()
        }
    }

    @ItemSelect
    fun spnPartFromItemSelected(selected: Boolean, position: Int) {
        if (vm.uspartfrom == position + 1) return
        vm.uspartfrom = position + 1
        vm.updatePartFrom {
            if (!chkUnitTo.isChecked || vm.isInvalidUnitPart)
                updateUnitPartTo()
        }
    }

    @ItemSelect
    fun spnUnitToItemSelected(selected: Boolean, position: Int) {
        if (vm.usunitto == position + 1) return
        vm.usunitto = position + 1
        vm.updateUnitTo {
            if (vm.isInvalidUnitPart)
                updateUnitPartFrom()
        }
    }

    @ItemSelect
    fun spnPartToItemSelected(selected: Boolean, position: Int) {
        if (vm.uspartto == position + 1) return
        vm.uspartto = position + 1
        vm.updatePartTo {
            if (vm.isInvalidUnitPart)
                updateUnitPartFrom()
        }
    }

}
