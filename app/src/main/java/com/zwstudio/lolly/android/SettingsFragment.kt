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
    lateinit var spnDictItem: Spinner
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
    lateinit var spnToType: Spinner
    @ViewById
    lateinit var btnPrevious: Button
    @ViewById
    lateinit var btnNext: Button

    val compositeDisposable = CompositeDisposable()

    @AfterViews
    fun afterViews() {
        activity?.title = "Settings"
        compositeDisposable.add(vm.getData().subscribe {
            initSpnLanguage()
        })
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
        compositeDisposable.add(vm.setSelectedLang(vm.lstLanguages[position]).concatMap {
            vm.updateLang()
        }.subscribe {
            updateLang()
        })
    }

    private fun updateLang() {
        run {
            val lst = vm.lstDictItems
            val adapter = object : ArrayAdapter<DictItem>(activity!!, R.layout.spinner_item_2, android.R.id.text1, lst) {
                fun convert(v: View, position: Int): View {
                    val m = getItem(position)!!
                    var tv = v.findViewById<TextView>(android.R.id.text1)
                    tv.text = m.dictname
                    (tv as? CheckedTextView)?.isChecked = spnDictItem.selectedItemPosition == position
                    tv = v.findViewById<TextView>(android.R.id.text2)
                    val item2 = vm.lstDictsMean.firstOrNull { it.dictname == m.dictname }
                    tv.text = item2?.url ?: ""
                    return v
                }
                override fun getView(position: Int, convertView: View?, parent: ViewGroup) =
                    convert(super.getView(position, convertView, parent), position)
                override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup) =
                    convert(super.getDropDownView(position, convertView, parent), position)
            }
            adapter.setDropDownViewResource(R.layout.list_item_2)
            spnDictItem.adapter = adapter

            spnDictItem.setSelection(vm.selectedDictItemIndex)
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
                    tv.text = "${vm.unitCount} units"
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
    fun spnDictItemItemSelected(selected: Boolean, position: Int) {
        if (vm.selectedDictItemIndex == position) return
        vm.selectedDictItem = vm.lstDictItems[position]
        Log.d("", String.format("Checked position:%d", position))
        (spnDictItem.adapter as ArrayAdapter<DictItem>).notifyDataSetChanged()
        compositeDisposable.add(vm.updateDictItem().subscribe())
    }

    @ItemSelect
    fun spnDictNoteItemSelected(selected: Boolean, position: Int) {
        if (vm.selectedDictNoteIndex == position) return
        vm.selectedDictNote = vm.lstDictsNote[position]
        Log.d("", String.format("Checked position:%d", position))
        (spnDictNote.adapter as ArrayAdapter<DictNote>).notifyDataSetChanged()
        compositeDisposable.add(vm.updateDictNote().subscribe())
    }

    @ItemSelect
    fun spnTextbookItemSelected(selected: Boolean, position: Int) {
        if (vm.selectedTextbookIndex == position) return
        vm.selectedTextbook = vm.lstTextbooks[position]
        Log.d("", String.format("Checked position:%d", position))
        (spnTextbook.adapter as ArrayAdapter<Textbook>).notifyDataSetChanged()
        compositeDisposable.add(vm.updateTextbook().subscribe {
            updateTextbook()
        })
    }

    private fun updateTextbook() {

        fun makeAdapter(lst: List<SelectItem>): ArrayAdapter<SelectItem> {
            val adapter = object : ArrayAdapter<SelectItem>(activity!!, android.R.layout.simple_spinner_item, lst) {
                fun convert(v: View, position: Int): View {
                    val tv = v.findViewById<TextView>(android.R.id.text1)
                    tv.text = getItem(position).label
                    return v
                }
                override fun getView(position: Int, convertView: View?, parent: ViewGroup) =
                    convert(super.getView(position, convertView, parent), position)
                override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup) =
                    convert(super.getDropDownView(position, convertView, parent), position)
            }
            adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice)
            return adapter
        }

        run {
            val adapter = makeAdapter(vm.lstUnits)
            spnUnitFrom.adapter = adapter
            spnUnitTo.adapter = adapter

            spnUnitFrom.setSelection(vm.usunitfrom - 1)
            spnUnitTo.setSelection(vm.usunitto - 1)
        }

        run {
            val adapter = makeAdapter(vm.lstParts)
            spnPartFrom.adapter = adapter
            spnPartTo.adapter = adapter

            spnPartFrom.setSelection(vm.uspartfrom - 1)
            spnPartTo.setSelection(vm.uspartto - 1)
        }

        run {
            val lst = listOf("Unit", "Part", "To").mapIndexed { index, s -> SelectItem(index, s) }
            val adapter = makeAdapter(lst)
            spnToType.adapter = adapter
            val toType =
                if (vm.isSingleUnit) 0
                else if (vm.isSingleUnitPart) 1
                else 2
            spnToType.setSelection(toType);
        }
    }

    @ItemSelect
    fun spnUnitFromItemSelected(selected: Boolean, position: Int) {
        if (!updateUnitFrom(vm.lstUnits[position].value)) return
        if (spnToType.selectedItemPosition == 0)
            updateSingleUnit()
        else if (spnToType.selectedItemPosition == 1 || vm.isInvalidUnitPart)
            updateUnitPartTo()
    }

    @ItemSelect
    fun spnPartFromItemSelected(selected: Boolean, position: Int) {
        if (!updatePartFrom(vm.lstParts[position].value)) return
        if (spnToType.selectedItemPosition == 1 || vm.isInvalidUnitPart)
            updateUnitPartTo()
    }

    @ItemSelect
    fun spnToTypeItemSelected(selected: Boolean, position: Int) {
        val b = position == 2
        spnUnitTo.isEnabled = b
        spnPartTo.isEnabled = b && !vm.isSinglePart
        btnPrevious.isEnabled = !b
        btnNext.isEnabled = !b
        spnPartFrom.isEnabled = position != 0 && !vm.isSinglePart
        if (position == 0)
            updateSingleUnit()
        else if (position == 1)
            updateUnitPartTo()
    }

    @Click
    fun btnPrevious() {
        if (spnToType.selectedItemPosition == 0) {
            if (vm.usunitfrom > 1) {
                updateUnitFrom(vm.usunitfrom - 1)
                updateUnitTo(vm.usunitfrom)
            }
        } else if (vm.uspartfrom > 1) {
            updatePartFrom(vm.uspartfrom - 1)
            updateUnitPartTo()
        } else if (vm.usunitfrom > 1) {
            updateUnitFrom(vm.usunitfrom - 1)
            updatePartFrom(vm.partCount)
            updateUnitPartTo()
        }
    }

    @Click
    fun btnNext() {
        if (spnToType.selectedItemPosition == 0) {
            if (vm.usunitfrom < vm.unitCount) {
                updateUnitFrom(vm.usunitfrom + 1)
                updateUnitTo(vm.usunitfrom)
            }
        } else if (vm.uspartfrom < vm.partCount) {
            updatePartFrom(vm.uspartfrom + 1)
            updateUnitPartTo()
        } else if (vm.usunitfrom < vm.unitCount) {
            updateUnitFrom(vm.usunitfrom + 1)
            updatePartFrom(1)
            updateUnitPartTo()
        }
    }

    @ItemSelect
    fun spnUnitToItemSelected(selected: Boolean, position: Int) {
        if (!updateUnitTo(vm.lstUnits[position].value)) return
        if (vm.isInvalidUnitPart)
            updateUnitPartFrom()
    }

    @ItemSelect
    fun spnPartToItemSelected(selected: Boolean, position: Int) {
        if (!updatePartTo(vm.lstParts[position].value)) return
        if (vm.isInvalidUnitPart)
            updateUnitPartFrom()
    }

    fun updateUnitPartFrom() {
        updateUnitFrom(vm.usunitto)
        updatePartFrom(vm.uspartto)
    }

    fun updateUnitPartTo() {
        updateUnitTo(vm.usunitfrom)
        updatePartTo(vm.uspartfrom)
    }

    fun updateSingleUnit() {
        updateUnitTo(vm.usunitfrom)
        updatePartFrom(1)
        updatePartTo(vm.partCount)
    }

    fun updateUnitFrom(v: Int): Boolean {
        if (vm.usunitfrom == v) return false
        vm.usunitfrom = v
        spnUnitFrom.setSelection(vm.lstUnits.indexOfFirst { it.value == v })
        compositeDisposable.add(vm.updateUnitFrom().subscribe())
        return true
    }

    fun updatePartFrom(v: Int): Boolean {
        if (vm.uspartfrom == v) return false
        vm.uspartfrom = v
        spnPartFrom.setSelection(vm.lstParts.indexOfFirst { it.value == v })
        compositeDisposable.add(vm.updatePartFrom().subscribe())
        return true
    }

    fun updateUnitTo(v: Int): Boolean {
        if (vm.usunitto == v) return false
        vm.usunitto = v
        spnUnitTo.setSelection(vm.lstUnits.indexOfFirst { it.value == v })
        compositeDisposable.add(vm.updateUnitTo().subscribe())
        return true
    }

    fun updatePartTo(v: Int): Boolean {
        if (vm.uspartto == v) return false
        vm.uspartto = v
        spnPartTo.setSelection(vm.lstParts.indexOfFirst { it.value == v })
        compositeDisposable.add(vm.updatePartTo().subscribe())
        return true
    }
}
