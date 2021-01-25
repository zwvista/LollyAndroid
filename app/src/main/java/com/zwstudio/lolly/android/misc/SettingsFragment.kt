package com.zwstudio.lolly.android.misc

import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.widget.*
import androidx.fragment.app.Fragment
import com.zwstudio.lolly.android.R
import com.zwstudio.lolly.data.misc.SettingsListener
import com.zwstudio.lolly.data.misc.SettingsViewModel
import com.zwstudio.lolly.data.misc.makeAdapter
import com.zwstudio.lolly.domain.misc.*
import io.reactivex.rxjava3.disposables.CompositeDisposable
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
    lateinit var spnDictReference: Spinner
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
        vm.handler = Handler(Looper.getMainLooper())
        vm.settingsListener = this
        compositeDisposable.add(vm.getData().subscribe())
    }

    override fun onGetData() {
        val lst = vm.lstLanguages
        val adapter = makeAdapter(activity!!, android.R.layout.simple_spinner_item, lst) { v, position ->
            val ctv = v.findViewById<TextView>(android.R.id.text1)
            ctv.text = lst[position].langname
            ctv.setTextColor(Color.BLUE)
            v
        }
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice)
        spnLanguage.adapter = adapter

        spnLanguage.setSelection(vm.selectedLangIndex)
    }

    @ItemSelect
    fun spnLanguageItemSelected(selected: Boolean, selectedItem: MLanguage) {
        if (vm.selectedLang == selectedItem) return
        compositeDisposable.add(vm.setSelectedLang(selectedItem).subscribe())
    }

    override fun onUpdateLang() {
        run {
            val lst = vm.lstVoices
            val adapter = makeAdapter(activity!!, R.layout.spinner_item_2, android.R.id.text1, lst) { v, position ->
                val m = getItem(position)!!
                var tv = v.findViewById<TextView>(android.R.id.text1)
                tv.text = m.voicelang
                (tv as? CheckedTextView)?.isChecked = spnVoice.selectedItemPosition == position
                tv = v.findViewById<TextView>(android.R.id.text2)
                val item = vm.lstVoices.firstOrNull { it.voicelang == m.voicelang }
                tv.text = item?.voicename ?: ""
                v
            }
            adapter.setDropDownViewResource(R.layout.list_item_2)
            spnVoice.adapter = adapter

            spnVoice.setSelection(vm.selectedVoiceIndex)
            onUpdateVoice()
        }
        run {
            val lst = vm.lstDictsReference
            val adapter = makeAdapter(activity!!, R.layout.spinner_item_2, android.R.id.text1, lst) { v, position ->
                val m = getItem(position)!!
                var tv = v.findViewById<TextView>(android.R.id.text1)
                tv.text = m.dictname
                (tv as? CheckedTextView)?.isChecked = spnDictReference.selectedItemPosition == position
                tv = v.findViewById<TextView>(android.R.id.text2)
                val item = vm.lstDictsReference.firstOrNull { it.dictname == m.dictname }
                tv.text = item?.url ?: ""
                v
            }
            adapter.setDropDownViewResource(R.layout.list_item_2)
            spnDictReference.adapter = adapter

            spnDictReference.setSelection(vm.selectedDictReferenceIndex)
            onUpdateDictReference()
        }
        run {
            val lst = vm.lstDictsNote
            val adapter = makeAdapter(activity!!, R.layout.spinner_item_2, android.R.id.text1, lst) { v, position ->
                val m = getItem(position)!!
                var tv = v.findViewById<TextView>(android.R.id.text1)
                tv.text = m.dictname
                (tv as? CheckedTextView)?.isChecked = spnDictNote.selectedItemPosition == position
                tv = v.findViewById<TextView>(android.R.id.text2)
                tv.text = m.url
                v
            }
            adapter.setDropDownViewResource(R.layout.list_item_2)
            spnDictNote.adapter = adapter

            spnDictNote.setSelection(vm.selectedDictNoteIndex)
            onUpdateDictNote()
        }
        run {
            val lst = vm.lstDictsTranslation
            val adapter = makeAdapter(activity!!, R.layout.spinner_item_2, android.R.id.text1, lst) { v, position ->
                val m = getItem(position)!!
                var tv = v.findViewById<TextView>(android.R.id.text1)
                tv.text = m.dictname
                (tv as? CheckedTextView)?.isChecked = spnDictTranslation.selectedItemPosition == position
                tv = v.findViewById<TextView>(android.R.id.text2)
                tv.text = m.url
                v
            }
            adapter.setDropDownViewResource(R.layout.list_item_2)
            spnDictTranslation.adapter = adapter

            spnDictTranslation.setSelection(vm.selectedDictTranslationIndex)
            onUpdateDictTranslation()
        }
        run {
            val lst = vm.lstTextbooks
            val adapter = makeAdapter(activity!!, R.layout.spinner_item_2, android.R.id.text1, lst) { v, position ->
                val m = getItem(position)!!
                var tv = v.findViewById<TextView>(android.R.id.text1)
                tv.text = m.textbookname
                (tv as? CheckedTextView)?.isChecked = spnTextbook.selectedItemPosition == position
                tv = v.findViewById<TextView>(android.R.id.text2)
                tv.text = "${vm.unitCount} units"
                v
            }
            adapter.setDropDownViewResource(R.layout.list_item_2)
            spnTextbook.adapter = adapter

            spnTextbook.setSelection(vm.selectedTextbookIndex)
            onUpdateTextbook()
        }
    }

    @ItemSelect
    fun spnVoiceItemSelected(selected: Boolean, selectedItem: MVoice) {
        if (vm.selectedVoice == selectedItem) return
        vm.selectedVoice = selectedItem
        (spnVoice.adapter as ArrayAdapter<*>).notifyDataSetChanged()
        compositeDisposable.add(vm.updateVoice().subscribe())
    }

    @ItemSelect
    fun spnDictReferenceItemSelected(selected: Boolean, selectedItem: MDictionary) {
        if (vm.selectedDictReference == selectedItem) return
        vm.selectedDictReference = selectedItem
        (spnDictReference.adapter as ArrayAdapter<*>).notifyDataSetChanged()
        compositeDisposable.add(vm.updateDictReference().subscribe())
    }

    @ItemSelect
    fun spnDictNoteItemSelected(selected: Boolean, selectedItem: MDictionary) {
        if (vm.selectedDictNote == selectedItem) return
        vm.selectedDictNote = selectedItem
        (spnDictNote.adapter as ArrayAdapter<*>).notifyDataSetChanged()
        compositeDisposable.add(vm.updateDictNote().subscribe())
    }

    @ItemSelect
    fun spnDictTranslationItemSelected(selected: Boolean, selectedItem: MDictionary) {
        if (vm.selectedDictTranslation == selectedItem) return
        vm.selectedDictTranslation = selectedItem
        (spnDictTranslation.adapter as ArrayAdapter<*>).notifyDataSetChanged()
        compositeDisposable.add(vm.updateDictTranslation().subscribe())
    }

    @ItemSelect
    fun spnTextbookItemSelected(selected: Boolean, selectedItem: MTextbook) {
        if (vm.selectedTextbook == selectedItem) return
        vm.selectedTextbook = selectedItem
        (spnTextbook.adapter as ArrayAdapter<*>).notifyDataSetChanged()
        compositeDisposable.add(vm.updateTextbook().subscribe())
    }

    override fun onUpdateTextbook() {

        fun makeAdapter(lst: List<MSelectItem>): ArrayAdapter<MSelectItem> {
            val adapter = makeAdapter(activity!!, android.R.layout.simple_spinner_item, lst) { v, position ->
                val tv = v.findViewById<TextView>(android.R.id.text1)
                tv.text = getItem(position)!!.label
                v
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
            spnToType.setSelection(vm.toType.ordinal)
        }
    }

    @ItemSelect
    fun spnUnitFromItemSelected(selected: Boolean, selectedItem: MSelectItem) {
        if (vm.usunitfrom == selectedItem.value) return
        compositeDisposable.add(vm.updateUnitFrom(selectedItem.value).subscribe())
    }

    @ItemSelect
    fun spnPartFromItemSelected(selected: Boolean, selectedItem: MSelectItem) {
        if (vm.uspartfrom == selectedItem.value) return
        compositeDisposable.add(vm.updatePartFrom(selectedItem.value).subscribe())
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
    fun spnUnitToItemSelected(selected: Boolean, selectedItem: MSelectItem) {
        if (vm.usunitto == selectedItem.value) return
        compositeDisposable.add(vm.updateUnitTo(selectedItem.value).subscribe())
    }

    @ItemSelect
    fun spnPartToItemSelected(selected: Boolean, selectedItem: MSelectItem) {
        if (vm.uspartto == selectedItem.value) return
        compositeDisposable.add(vm.updatePartTo(selectedItem.value).subscribe())
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
}
