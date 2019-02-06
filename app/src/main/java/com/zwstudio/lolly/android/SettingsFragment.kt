package com.zwstudio.lolly.android

import android.graphics.Color
import android.support.v4.app.Fragment
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.zwstudio.lolly.data.SettingsViewModel
import com.zwstudio.lolly.domain.*
import io.reactivex.disposables.CompositeDisposable
import org.androidannotations.annotations.*

@EFragment(R.layout.content_settings)
class SettingsFragment : Fragment() {

    @Bean
    lateinit var vm: SettingsViewModel

    @ViewById
    lateinit var spnLanguage: Spinner
    @ViewById
    lateinit var spnDictPicker: Spinner
    @ViewById
    lateinit var spnDictNote: Spinner
    @ViewById
    lateinit var spnTextbook: Spinner
    @ViewById
    lateinit var spnUnitFrom: Spinner
    @ViewById
    lateinit var spnUnitTo: Spinner
    @ViewById
    lateinit var spnPartFrom: Spinner
    @ViewById
    lateinit var spnPartTo: Spinner
    @ViewById
    lateinit var chkUnitTo: CheckBox

    val compositeDisposable = CompositeDisposable();

    @AfterViews
    fun afterViews() {
        activity?.title = "Settings"
        compositeDisposable.add(vm.getData().subscribe {
            initSpnLanguage()
        })
    }

    private fun updateUnitPartFrom() {
        if (vm.usunitfrom != vm.usunitto) {
            vm.usunitfrom = vm.usunitto
            compositeDisposable.add(vm.updateUnitFrom().subscribe {
                spnUnitFrom.setSelection(spnUnitTo.selectedItemPosition)
            })
        }
        if (vm.uspartfrom != vm.uspartto) {
            vm.uspartfrom = vm.uspartto
            compositeDisposable.add(vm.updatePartFrom().subscribe {
                spnPartFrom.setSelection(spnPartTo.selectedItemPosition)
            })
        }
    }

    private fun updateUnitPartTo() {
        if (vm.usunitto != vm.usunitfrom) {
            vm.usunitto = vm.usunitfrom
            compositeDisposable.add(vm.updateUnitTo().subscribe {
                spnUnitTo.setSelection(spnUnitFrom.selectedItemPosition)
            })
        }
        if (vm.uspartto != vm.uspartfrom) {
            vm.uspartto = vm.uspartfrom
            compositeDisposable.add(vm.updatePartTo().subscribe {
                spnPartTo.setSelection(spnPartFrom.selectedItemPosition)
            })
        }
    }

    private fun initSpnLanguage() {
        val lst = vm.lstLanguages
        val adapter = object : ArrayAdapter<Language>(activity!!,
            android.R.layout.simple_spinner_item, lst) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val v = super.getView(position, convertView, parent)
                val tv = v.findViewById<TextView>(android.R.id.text1)
                tv.text = lst[position].langname
                tv.setTextColor(Color.BLUE)
                return v
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val v = super.getDropDownView(position, convertView, parent)
                val ctv = v.findViewById<CheckedTextView>(android.R.id.text1)
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
        compositeDisposable.add(vm.setSelectedLangIndex(position).concatMap {
            vm.updateLang()
        }.subscribe {
            updateLang()
        })
    }

    private fun updateLang() {
        run {
            val lst = vm.lstDictsPicker
            val adapter = object : ArrayAdapter<DictPicker>(activity!!, R.layout.spinner_item_2, android.R.id.text1, lst) {
                fun convert(v: View, position: Int): View {
                    val m = getItem(position)!!
                    var tv = v.findViewById<TextView>(android.R.id.text1)
                    tv.text = m.dictname
                    (tv as? CheckedTextView)?.isChecked = spnDictPicker.selectedItemPosition == position
                    tv = v.findViewById<TextView>(android.R.id.text2)
                    val item2 = vm.lstDictsWord.firstOrNull { it.dictname == m.dictname }
                    tv.text = item2?.url ?: ""
                    return v
                }
                override fun getView(position: Int, convertView: View?, parent: ViewGroup) =
                    convert(super.getView(position, convertView, parent), position)
                override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup) =
                    convert(super.getDropDownView(position, convertView, parent), position)
            }
            adapter.setDropDownViewResource(R.layout.list_item_2)
            spnDictPicker.adapter = adapter

            spnDictPicker.setSelection(vm.selectedDictPickerIndex)
        }
        run {
            val lst = vm.lstDictsNote
            val adapter = object : ArrayAdapter<DictNote>(activity!!, R.layout.spinner_item_2, android.R.id.text1, lst) {
                fun convert(v: View, position: Int): View {
                    val m = getItem(position)!!
                    var tv = v.findViewById<TextView>(android.R.id.text1)
                    tv.text = m.dictname
                    (tv as? CheckedTextView)?.isChecked = spnDictNote.selectedItemPosition == position
                    tv = v.findViewById<TextView>(android.R.id.text2)
                    tv.text = m.url
                    return v
                }
                override fun getView(position: Int, convertView: View?, parent: ViewGroup) =
                        convert(super.getView(position, convertView, parent), position)
                override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup) =
                        convert(super.getDropDownView(position, convertView, parent), position)
            }
            adapter.setDropDownViewResource(R.layout.list_item_2)
            spnDictNote.adapter = adapter

            spnDictNote.setSelection(vm.selectedDictNoteIndex)
        }
        run {
            val lst = vm.lstTextbooks
            val adapter = object : ArrayAdapter<Textbook>(activity!!, R.layout.spinner_item_2, android.R.id.text1, lst) {
                fun convert(v: View, position: Int): View {
                    val m = getItem(position)!!
                    var tv = v.findViewById<TextView>(android.R.id.text1)
                    tv.text = m.textbookname
                    (tv as? CheckedTextView)?.isChecked = spnTextbook.selectedItemPosition == position
                    tv = v.findViewById<TextView>(android.R.id.text2)
                    tv.text = m.units.toString() + " units"
                    return v
                }
                override fun getView(position: Int, convertView: View?, parent: ViewGroup) =
                    convert(super.getView(position, convertView, parent), position)
                override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup) =
                    convert(super.getDropDownView(position, convertView, parent), position)
            }
            adapter.setDropDownViewResource(R.layout.list_item_2)
            spnTextbook.adapter = adapter

            spnTextbook.setSelection(vm.selectedTextbookIndex)
            updateTextbook()
        }
    }

    @ItemSelect
    fun spnDictPickerItemSelected(selected: Boolean, position: Int) {
        if (vm.selectedDictPickerIndex == position) return
        vm.selectedDictPickerIndex = position
        Log.d("", String.format("Checked position:%d", position))
        (spnDictPicker.adapter as ArrayAdapter<DictPicker>).notifyDataSetChanged()
        compositeDisposable.add(vm.updateDictPicker().subscribe())
    }

    @ItemSelect
    fun spnDictNoteItemSelected(selected: Boolean, position: Int) {
        if (vm.selectedDictNoteIndex == position) return
        vm.selectedDictNoteIndex = position
        Log.d("", String.format("Checked position:%d", position))
        (spnDictNote.adapter as ArrayAdapter<DictNote>).notifyDataSetChanged()
        compositeDisposable.add(vm.updateDictNote().subscribe())
    }

    @ItemSelect
    fun spnTextbookItemSelected(selected: Boolean, position: Int) {
        if (vm.selectedTextbookIndex == position) return
        vm.selectedTextbookIndex = position
        Log.d("", String.format("Checked position:%d", position))
        (spnTextbook.adapter as ArrayAdapter<Textbook>).notifyDataSetChanged()
        compositeDisposable.add(vm.updateTextbook().subscribe {
            updateTextbook()
        })
    }

    private fun updateTextbook() {
        run {
            val lst = vm.lstUnits
            val adapter = object : ArrayAdapter<String>(activity!!, android.R.layout.simple_spinner_item, lst) {
                fun convert(v: View, position: Int): View {
                    val tv = v.findViewById<TextView>(android.R.id.text1)
                    tv.text = getItem(position)
                    return v
                }
                override fun getView(position: Int, convertView: View?, parent: ViewGroup) =
                    convert(super.getView(position, convertView, parent), position)
                override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup) =
                    convert(super.getDropDownView(position, convertView, parent), position)
            }
            adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice)
            spnUnitFrom.adapter = adapter
            spnUnitTo.adapter = adapter

            spnUnitFrom.setSelection(vm.usunitfrom - 1)
            spnUnitTo.setSelection(vm.usunitto - 1)
        }

        run {
            val lst = vm.lstParts
            val adapter = object : ArrayAdapter<String>(activity!!, android.R.layout.simple_spinner_item, lst) {
                fun convert(v: View, position: Int): View {
                    val tv = v.findViewById<TextView>(android.R.id.text1)
                    tv.text = getItem(position)
                    return v
                }
                override fun getView(position: Int, convertView: View?, parent: ViewGroup) =
                    convert(super.getView(position, convertView, parent), position)
                override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup) =
                    convert(super.getDropDownView(position, convertView, parent), position)
            }
            adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice)
            spnPartFrom.adapter = adapter
            spnPartTo.adapter = adapter

            spnPartFrom.setSelection(vm.uspartfrom - 1)
            spnPartTo.setSelection(vm.uspartto - 1)
        }

        val b = !vm.isSingleUnitPart
        chkUnitTo.isChecked = b
        chkUnitToCheckedChanged(b)
    }

    @CheckedChange
    fun chkUnitToCheckedChanged(isChecked: Boolean) {
        spnPartTo.isEnabled = isChecked
        spnUnitTo.isEnabled = isChecked
        if (!isChecked)
            updateUnitPartTo()
    }

    @ItemSelect
    fun spnUnitFromItemSelected(selected: Boolean, position: Int) {
        if (vm.usunitfrom == position + 1) return
        vm.usunitfrom = position + 1
        compositeDisposable.add(vm.updateUnitFrom().subscribe {
            if (!chkUnitTo.isChecked || vm.isInvalidUnitPart)
                updateUnitPartTo()
        })
    }

    @ItemSelect
    fun spnPartFromItemSelected(selected: Boolean, position: Int) {
        if (vm.uspartfrom == position + 1) return
        vm.uspartfrom = position + 1
        compositeDisposable.add(vm.updatePartFrom().subscribe {
            if (!chkUnitTo.isChecked || vm.isInvalidUnitPart)
                updateUnitPartTo()
        })
    }

    @ItemSelect
    fun spnUnitToItemSelected(selected: Boolean, position: Int) {
        if (vm.usunitto == position + 1) return
        vm.usunitto = position + 1
        compositeDisposable.add(vm.updateUnitTo().subscribe {
            if (vm.isInvalidUnitPart)
                updateUnitPartFrom()
        })
    }

    @ItemSelect
    fun spnPartToItemSelected(selected: Boolean, position: Int) {
        if (vm.uspartto == position + 1) return
        vm.uspartto = position + 1
        compositeDisposable.add(vm.updatePartTo().subscribe {
            if (vm.isInvalidUnitPart)
                updateUnitPartFrom()
        })
    }

}
