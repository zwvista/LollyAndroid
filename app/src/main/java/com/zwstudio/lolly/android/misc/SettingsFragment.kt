package com.zwstudio.lolly.android.misc

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckedTextView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.zwstudio.lolly.android.R
import com.zwstudio.lolly.android.databinding.FragmentSettingsBinding
import com.zwstudio.lolly.android.vmSettings
import com.zwstudio.lolly.data.misc.SettingsListener
import com.zwstudio.lolly.data.misc.makeAdapter
import com.zwstudio.lolly.domain.misc.MSelectItem
import io.reactivex.rxjava3.disposables.CompositeDisposable

class SettingsFragment : Fragment(), SettingsListener {

    var vm = vmSettings
    var binding by autoCleared<FragmentSettingsBinding>()

    val compositeDisposable = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            model = vm
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm.handler = Handler(Looper.getMainLooper())
        vm.settingsListener = this
        compositeDisposable.add(vm.getData().subscribe())
    }

    override fun onGetData() {
        val lst = vm.lstLanguages
        val adapter = makeAdapter(requireActivity(), android.R.layout.simple_spinner_item, lst) { v, position ->
            val ctv = v.findViewById<TextView>(android.R.id.text1)
            ctv.text = lst[position].langname
            ctv.setTextColor(Color.BLUE)
            v
        }
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice)
        binding.spnLanguage.adapter = adapter

        binding.spnLanguage.setSelection(vm.selectedLangIndex)
    }

//    fun spnLanguageItemSelected(selected: Boolean, selectedItem: MLanguage) {
//        if (vm.selectedLang == selectedItem) return
//        compositeDisposable.add(vm.setSelectedLang(selectedItem).subscribe())
//    }

    override fun onUpdateLang() {
        run {
            val lst = vm.lstVoices
            val adapter = makeAdapter(requireActivity(), R.layout.spinner_item_2, android.R.id.text1, lst) { v, position ->
                val m = getItem(position)!!
                var tv = v.findViewById<TextView>(android.R.id.text1)
                tv.text = m.voicelang
                (tv as? CheckedTextView)?.isChecked = binding.spnVoice.selectedItemPosition == position
                tv = v.findViewById<TextView>(android.R.id.text2)
                val item = vm.lstVoices.firstOrNull { it.voicelang == m.voicelang }
                tv.text = item?.voicename ?: ""
                v
            }
            adapter.setDropDownViewResource(R.layout.list_item_2)
            binding.spnVoice.adapter = adapter

            binding.spnVoice.setSelection(vm.selectedVoiceIndex)
            onUpdateVoice()
        }
        run {
            val lst = vm.lstDictsReference
            val adapter = makeAdapter(requireActivity(), R.layout.spinner_item_2, android.R.id.text1, lst) { v, position ->
                val m = getItem(position)!!
                var tv = v.findViewById<TextView>(android.R.id.text1)
                tv.text = m.dictname
                (tv as? CheckedTextView)?.isChecked = binding.spnDictReference.selectedItemPosition == position
                tv = v.findViewById<TextView>(android.R.id.text2)
                val item = vm.lstDictsReference.firstOrNull { it.dictname == m.dictname }
                tv.text = item?.url ?: ""
                v
            }
            adapter.setDropDownViewResource(R.layout.list_item_2)
            binding.spnDictReference.adapter = adapter

            binding.spnDictReference.setSelection(vm.selectedDictReferenceIndex)
            onUpdateDictReference()
        }
        run {
            val lst = vm.lstDictsNote
            val adapter = makeAdapter(requireActivity(), R.layout.spinner_item_2, android.R.id.text1, lst) { v, position ->
                val m = getItem(position)!!
                var tv = v.findViewById<TextView>(android.R.id.text1)
                tv.text = m.dictname
                (tv as? CheckedTextView)?.isChecked = binding.spnDictNote.selectedItemPosition == position
                tv = v.findViewById<TextView>(android.R.id.text2)
                tv.text = m.url
                v
            }
            adapter.setDropDownViewResource(R.layout.list_item_2)
            binding.spnDictNote.adapter = adapter

            binding.spnDictNote.setSelection(vm.selectedDictNoteIndex)
            onUpdateDictNote()
        }
        run {
            val lst = vm.lstDictsTranslation
            val adapter = makeAdapter(requireActivity(), R.layout.spinner_item_2, android.R.id.text1, lst) { v, position ->
                val m = getItem(position)!!
                var tv = v.findViewById<TextView>(android.R.id.text1)
                tv.text = m.dictname
                (tv as? CheckedTextView)?.isChecked = binding.spnDictTranslation.selectedItemPosition == position
                tv = v.findViewById<TextView>(android.R.id.text2)
                tv.text = m.url
                v
            }
            adapter.setDropDownViewResource(R.layout.list_item_2)
            binding.spnDictTranslation.adapter = adapter

            binding.spnDictTranslation.setSelection(vm.selectedDictTranslationIndex)
            onUpdateDictTranslation()
        }
        run {
            val lst = vm.lstTextbooks
            val adapter = makeAdapter(requireActivity(), R.layout.spinner_item_2, android.R.id.text1, lst) { v, position ->
                val m = getItem(position)!!
                var tv = v.findViewById<TextView>(android.R.id.text1)
                tv.text = m.textbookname
                (tv as? CheckedTextView)?.isChecked = binding.spnTextbook.selectedItemPosition == position
                tv = v.findViewById<TextView>(android.R.id.text2)
                tv.text = "${vm.unitCount} units"
                v
            }
            adapter.setDropDownViewResource(R.layout.list_item_2)
            binding.spnTextbook.adapter = adapter

            binding.spnTextbook.setSelection(vm.selectedTextbookIndex)
            onUpdateTextbook()
        }
    }

//    fun spnVoiceItemSelected(selected: Boolean, selectedItem: MVoice) {
//        if (vm.selectedVoice == selectedItem) return
//        vm.selectedVoice = selectedItem
//        (binding.spnVoice.adapter as ArrayAdapter<*>).notifyDataSetChanged()
//        compositeDisposable.add(vm.updateVoice().subscribe())
//    }

//    fun spnDictReferenceItemSelected(selected: Boolean, selectedItem: MDictionary) {
//        if (vm.selectedDictReference == selectedItem) return
//        vm.selectedDictReference = selectedItem
//        (binding.spnDictReference.adapter as ArrayAdapter<*>).notifyDataSetChanged()
//        compositeDisposable.add(vm.updateDictReference().subscribe())
//    }

//    fun spnDictNoteItemSelected(selected: Boolean, selectedItem: MDictionary) {
//        if (vm.selectedDictNote == selectedItem) return
//        vm.selectedDictNote = selectedItem
//        (binding.spnDictNote.adapter as ArrayAdapter<*>).notifyDataSetChanged()
//        compositeDisposable.add(vm.updateDictNote().subscribe())
//    }

//    fun spnDictTranslationItemSelected(selected: Boolean, selectedItem: MDictionary) {
//        if (vm.selectedDictTranslation == selectedItem) return
//        vm.selectedDictTranslation = selectedItem
//        (binding.spnDictTranslation.adapter as ArrayAdapter<*>).notifyDataSetChanged()
//        compositeDisposable.add(vm.updateDictTranslation().subscribe())
//    }

//    fun spnTextbookItemSelected(selected: Boolean, selectedItem: MTextbook) {
//        if (vm.selectedTextbook == selectedItem) return
//        vm.selectedTextbook = selectedItem
//        (binding.spnTextbook.adapter as ArrayAdapter<*>).notifyDataSetChanged()
//        compositeDisposable.add(vm.updateTextbook().subscribe())
//    }

    override fun onUpdateTextbook() {

        fun makeAdapter(lst: List<MSelectItem>): ArrayAdapter<MSelectItem> {
            val adapter = makeAdapter(requireActivity(), android.R.layout.simple_spinner_item, lst) { v, position ->
                val tv = v.findViewById<TextView>(android.R.id.text1)
                tv.text = getItem(position)!!.label
                v
            }
            adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice)
            return adapter
        }

        run {
            val adapter = makeAdapter(vm.lstUnits)
            binding.spnUnitFrom.adapter = adapter
            binding.spnUnitTo.adapter = adapter

            onUpdateUnitFrom()
            onUpdateUnitTo()
        }

        run {
            val adapter = makeAdapter(vm.lstParts)
            binding.spnPartFrom.adapter = adapter
            binding.spnPartTo.adapter = adapter

            onUpdatePartFrom()
            onUpdatePartTo()
        }

        run {
            val adapter = makeAdapter(vm.lstToTypes)
            binding.spnToType.adapter = adapter
            binding.spnToType.setSelection(vm.toType.ordinal)
        }
    }

//    fun spnUnitFromItemSelected(selected: Boolean, selectedItem: MSelectItem) {
//        if (vm.usunitfrom == selectedItem.value) return
//        compositeDisposable.add(vm.updateUnitFrom(selectedItem.value).subscribe())
//    }

//    fun spnPartFromItemSelected(selected: Boolean, selectedItem: MSelectItem) {
//        if (vm.uspartfrom == selectedItem.value) return
//        compositeDisposable.add(vm.updatePartFrom(selectedItem.value).subscribe())
//    }

//    fun spnToTypeItemSelected(selected: Boolean, position: Int) {
//        val b = position == 2
//        binding.spnUnitTo.isEnabled = b
//        binding.spnPartTo.isEnabled = b && !vm.isSinglePart
//        binding.btnPrevious.isEnabled = !b
//        binding.btnNext.isEnabled = !b
//        binding.spnPartFrom.isEnabled = position != 0 && !vm.isSinglePart
//        compositeDisposable.add(vm.updateToType(position).subscribe())
//    }

    fun btnPrevious() {
        compositeDisposable.add(vm.previousUnitPart().subscribe())
    }

    fun btnNext() {
        compositeDisposable.add(vm.nextUnitPart().subscribe())
    }

//    fun spnUnitToItemSelected(selected: Boolean, selectedItem: MSelectItem) {
//        if (vm.usunitto == selectedItem.value) return
//        compositeDisposable.add(vm.updateUnitTo(selectedItem.value).subscribe())
//    }

//    fun spnPartToItemSelected(selected: Boolean, selectedItem: MSelectItem) {
//        if (vm.uspartto == selectedItem.value) return
//        compositeDisposable.add(vm.updatePartTo(selectedItem.value).subscribe())
//    }

    override fun onUpdateUnitFrom() {
        binding.spnUnitFrom.setSelection(vm.lstUnits.indexOfFirst { it.value == vm.usunitfrom })
    }

    override fun onUpdatePartFrom() {
        binding.spnPartFrom.setSelection(vm.lstParts.indexOfFirst { it.value == vm.uspartfrom })
    }

    override fun onUpdateUnitTo() {
        binding.spnUnitTo.setSelection(vm.lstUnits.indexOfFirst { it.value == vm.usunitto })
    }

    override fun onUpdatePartTo() {
        binding.spnPartTo.setSelection(vm.lstParts.indexOfFirst { it.value == vm.uspartto })
    }
}
