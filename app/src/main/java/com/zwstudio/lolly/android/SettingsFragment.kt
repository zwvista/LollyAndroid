package com.zwstudio.lolly.android

import android.graphics.Color
import android.os.Handler
import android.support.v4.app.Fragment
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.zwstudio.lolly.data.SettingsListener
import com.zwstudio.lolly.data.SettingsViewModel
import com.zwstudio.lolly.domain.*
import io.reactivex.disposables.CompositeDisposable
import org.androidannotations.annotations.*

@EFragment(R.layout.content_settings)
class SettingsFragment : Fragment(), SettingsListener {

    @Bean
    lateinit var vm: SettingsViewModel

    @ViewById
    lateinit var spnLanguage: Spinner
    @ViewById
    lateinit var spnVoice: Spinner
    @ViewById
    lateinit var spnDictItem: Spinner
    @ViewById
    lateinit var spnDictNote: Spinner
    @ViewById
    lateinit var spnDictTranslation: Spinner
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
        vm.handler = Handler()
        vm.settingsListener = this
        compositeDisposable.add(vm.getData().subscribe())
    }

    override fun onGetData() {
        val lst = vm.lstLanguages
        val adapter = object : ArrayAdapter<MLanguage>(activity!!,
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
    }

    @ItemSelect
    fun spnLanguageItemSelected(selected: Boolean, position: Int) {
        if (vm.selectedLangIndex == position) return
        Log.d("", String.format("Checked position:%d", position))
        compositeDisposable.add(vm.setSelectedLang(vm.lstLanguages[position]).subscribe())
    }

    override fun onUpdateLang() {
        run {
            val lst = vm.lstVoices
            val adapter = object : ArrayAdapter<MVoice>(activity!!, R.layout.spinner_item_2, android.R.id.text1, lst) {
                fun convert(v: View, position: Int): View {
                    val m = getItem(position)!!
                    var tv = v.findViewById<TextView>(android.R.id.text1)
                    tv.text = m.voicelang
                    (tv as? CheckedTextView)?.isChecked = spnVoice.selectedItemPosition == position
                    tv = v.findViewById<TextView>(android.R.id.text2)
                    val item2 = vm.lstVoices.firstOrNull { it.voicelang == m.voicelang }
                    tv.text = item2?.voicename ?: ""
                    return v
                }
                override fun getView(position: Int, convertView: View?, parent: ViewGroup) =
                    convert(super.getView(position, convertView, parent), position)
                override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup) =
                    convert(super.getDropDownView(position, convertView, parent), position)
            }
            adapter.setDropDownViewResource(R.layout.list_item_2)
            spnVoice.adapter = adapter

            spnVoice.setSelection(vm.selectedVoiceIndex)
            onUpdateVoice()
        }
        run {
            val lst = vm.lstDictItems
            val adapter = object : ArrayAdapter<MDictItem>(activity!!, R.layout.spinner_item_2, android.R.id.text1, lst) {
                fun convert(v: View, position: Int): View {
                    val m = getItem(position)!!
                    var tv = v.findViewById<TextView>(android.R.id.text1)
                    tv.text = m.dictname
                    (tv as? CheckedTextView)?.isChecked = spnDictItem.selectedItemPosition == position
                    tv = v.findViewById<TextView>(android.R.id.text2)
                    val item2 = vm.lstDictsReference.firstOrNull { it.dictname == m.dictname }
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
            onUpdateDictItem()
        }
        run {
            val lst = vm.lstDictsNote
            val adapter = object : ArrayAdapter<MDictNote>(activity!!, R.layout.spinner_item_2, android.R.id.text1, lst) {
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
            onUpdateDictNote()
        }
        run {
            val lst = vm.lstDictsTranslation
            val adapter = object : ArrayAdapter<MDictTranslation>(activity!!, R.layout.spinner_item_2, android.R.id.text1, lst) {
                fun convert(v: View, position: Int): View {
                    val m = getItem(position)!!
                    var tv = v.findViewById<TextView>(android.R.id.text1)
                    tv.text = m.dictname
                    (tv as? CheckedTextView)?.isChecked = spnDictTranslation.selectedItemPosition == position
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
            spnDictTranslation.adapter = adapter

            spnDictTranslation.setSelection(vm.selectedDictTranslationIndex)
            onUpdateDictTranslation()
        }
        run {
            val lst = vm.lstTextbooks
            val adapter = object : ArrayAdapter<MTextbook>(activity!!, R.layout.spinner_item_2, android.R.id.text1, lst) {
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
            onUpdateTextbook()
        }
    }

    @ItemSelect
    fun spnVoiceItemSelected(selected: Boolean, position: Int) {
        if (vm.selectedVoiceIndex == position) return
        vm.selectedVoice = vm.lstVoices[position]
        Log.d("", String.format("Checked position:%d", position))
        (spnVoice.adapter as ArrayAdapter<*>).notifyDataSetChanged()
        compositeDisposable.add(vm.updateVoice().subscribe())
    }

    @ItemSelect
    fun spnDictItemItemSelected(selected: Boolean, position: Int) {
        if (vm.selectedDictItemIndex == position) return
        vm.selectedDictItem = vm.lstDictItems[position]
        Log.d("", String.format("Checked position:%d", position))
        (spnDictItem.adapter as ArrayAdapter<*>).notifyDataSetChanged()
        compositeDisposable.add(vm.updateDictItem().subscribe())
    }

    @ItemSelect
    fun spnDictNoteItemSelected(selected: Boolean, position: Int) {
        if (vm.selectedDictNoteIndex == position) return
        vm.selectedDictNote = vm.lstDictsNote[position]
        Log.d("", String.format("Checked position:%d", position))
        (spnDictNote.adapter as ArrayAdapter<*>).notifyDataSetChanged()
        compositeDisposable.add(vm.updateDictNote().subscribe())
    }

    @ItemSelect
    fun spnDictTranslationItemSelected(selected: Boolean, position: Int) {
        if (vm.selectedDictTranslationIndex == position) return
        vm.selectedDictTranslation = vm.lstDictsTranslation[position]
        Log.d("", String.format("Checked position:%d", position))
        (spnDictTranslation.adapter as ArrayAdapter<*>).notifyDataSetChanged()
        compositeDisposable.add(vm.updateDictTranslation().subscribe())
    }

    @ItemSelect
    fun spnTextbookItemSelected(selected: Boolean, position: Int) {
        if (vm.selectedTextbookIndex == position) return
        vm.selectedTextbook = vm.lstTextbooks[position]
        Log.d("", String.format("Checked position:%d", position))
        (spnTextbook.adapter as ArrayAdapter<*>).notifyDataSetChanged()
        compositeDisposable.add(vm.updateTextbook().subscribe())
    }

    override fun onUpdateTextbook() {

        fun makeAdapter(lst: List<MSelectItem>): ArrayAdapter<MSelectItem> {
            val adapter = object : ArrayAdapter<MSelectItem>(activity!!, android.R.layout.simple_spinner_item, lst) {
                fun convert(v: View, position: Int): View {
                    val tv = v.findViewById<TextView>(android.R.id.text1)
                    tv.text = getItem(position)!!.label
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

            onUpdateUnitFrom()
            onUpdateUnitTo()
        }

        run {
            val adapter = makeAdapter(vm.lstParts)
            spnPartFrom.adapter = adapter
            spnPartTo.adapter = adapter

            onUpdatePartFrom()
            onUpdatePartTo()
        }

        run {
            val adapter = makeAdapter(vm.lstToTypes)
            spnToType.adapter = adapter
            spnToType.setSelection(vm.toType)
        }
    }

    @ItemSelect
    fun spnUnitFromItemSelected(selected: Boolean, position: Int) {
        if (vm.lstUnits.indexOfFirst { it.value == vm.usunitfrom } == position) return
        compositeDisposable.add(vm.updateUnitFrom(vm.lstUnits[position].value).subscribe())
    }

    @ItemSelect
    fun spnPartFromItemSelected(selected: Boolean, position: Int) {
        if (vm.lstParts.indexOfFirst { it.value == vm.uspartfrom } == position) return
        compositeDisposable.add(vm.updatePartFrom(vm.lstParts[position].value).subscribe())
    }

    @ItemSelect
    fun spnToTypeItemSelected(selected: Boolean, position: Int) {
        val b = position == 2
        spnUnitTo.isEnabled = b
        spnPartTo.isEnabled = b && !vm.isSinglePart
        btnPrevious.isEnabled = !b
        btnNext.isEnabled = !b
        spnPartFrom.isEnabled = position != 0 && !vm.isSinglePart
        compositeDisposable.add(vm.updateToType(position).subscribe())
    }

    @Click
    fun btnPrevious() {
        compositeDisposable.add(vm.previousUnitPart().subscribe())
    }

    @Click
    fun btnNext() {
        compositeDisposable.add(vm.nextUnitPart().subscribe())
    }

    @ItemSelect
    fun spnUnitToItemSelected(selected: Boolean, position: Int) {
        if (vm.lstUnits.indexOfFirst { it.value == vm.usunitto } == position) return
        compositeDisposable.add(vm.updateUnitTo(vm.lstUnits[position].value).subscribe())
    }

    @ItemSelect
    fun spnPartToItemSelected(selected: Boolean, position: Int) {
        if (vm.lstParts.indexOfFirst { it.value == vm.uspartto } == position) return
        compositeDisposable.add(vm.updatePartTo(vm.lstParts[position].value).subscribe())
    }

    override fun onUpdateDictItem() {
    }

    override fun onUpdateDictNote() {
    }

    override fun onUpdateDictTranslation() {
    }

    override fun onUpdateUnitFrom() {
        spnUnitFrom.setSelection(vm.lstUnits.indexOfFirst { it.value == vm.usunitfrom })
    }

    override fun onUpdatePartFrom() {
        spnPartFrom.setSelection(vm.lstParts.indexOfFirst { it.value == vm.uspartfrom })
    }

    override fun onUpdateUnitTo() {
        spnUnitTo.setSelection(vm.lstUnits.indexOfFirst { it.value == vm.usunitto })
    }

    override fun onUpdatePartTo() {
        spnPartTo.setSelection(vm.lstParts.indexOfFirst { it.value == vm.uspartto })
    }

    override fun onUpdateVoice() {
    }
}
