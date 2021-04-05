package com.zwstudio.lolly.android.misc

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.distinctUntilChanged
import com.zwstudio.lolly.android.databinding.FragmentSettingsBinding
import com.zwstudio.lolly.android.vmSettings
import com.zwstudio.lolly.data.misc.SettingsListener
import com.zwstudio.lolly.data.misc.makeCustomAdapter
import com.zwstudio.lolly.data.misc.makeCustomAdapter2

class SettingsFragment : Fragment(), SettingsListener {

    var vm = vmSettings
    var binding by autoCleared<FragmentSettingsBinding>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            model = vm
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vmSettings.selectedLangIndex_.distinctUntilChanged().observe(viewLifecycleOwner) {
            vmSettings.updateLang()
        }

        binding.spnVoice.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (vm.selectedVoice == vm.lstVoices[position]) return
                vm.selectedVoice = vm.lstVoices[position]
                (binding.spnVoice.adapter as ArrayAdapter<*>).notifyDataSetChanged()
                vm.updateVoice()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        binding.spnDictReference.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (vm.selectedDictReference == vm.lstDictsReference[position]) return
                vm.selectedDictReference = vm.lstDictsReference[position]
                (binding.spnDictReference.adapter as ArrayAdapter<*>).notifyDataSetChanged()
                vm.updateDictReference()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        binding.spnDictNote.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (vm.selectedDictNote == vm.lstDictsNote[position]) return
                vm.selectedDictNote = vm.lstDictsNote[position]
                (binding.spnDictNote.adapter as ArrayAdapter<*>).notifyDataSetChanged()
                vm.updateDictNote()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        binding.spnDictTranslation.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (vm.selectedDictTranslation == vm.lstDictsTranslation[position]) return
                vm.selectedDictTranslation = vm.lstDictsTranslation[position]
                (binding.spnDictTranslation.adapter as ArrayAdapter<*>).notifyDataSetChanged()
                vm.updateDictTranslation()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        binding.spnTextbook.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (vm.selectedTextbook == vm.lstTextbooks[position]) return
                vm.selectedTextbook = vm.lstTextbooks[position]
                (binding.spnTextbook.adapter as ArrayAdapter<*>).notifyDataSetChanged()
                vm.updateTextbook()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        binding.spnUnitFrom.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (vm.usunitfrom == vm.lstUnits[position].value) return
                vm.updateUnitFrom(vm.lstUnits[position].value)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        binding.spnPartFrom.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (vm.uspartfrom == vm.lstParts[position].value) return
                vm.updatePartFrom(vm.lstParts[position].value)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        binding.spnToType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val b = position == 2
                binding.spnUnitTo.isEnabled = b
                binding.spnPartTo.isEnabled = b && !vm.isSinglePart
                binding.btnPrevious.isEnabled = !b
                binding.btnNext.isEnabled = !b
                binding.spnPartFrom.isEnabled = position != 0 && !vm.isSinglePart
                vm.updateToType(position)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        binding.btnPrevious.setOnClickListener {
            vm.previousUnitPart()
        }

        binding.btnNext.setOnClickListener {
            vm.nextUnitPart()
        }

        binding.spnUnitTo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (vm.usunitto == vm.lstUnits[position].value) return
                vm.updateUnitTo(vm.lstUnits[position].value)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        binding.spnPartTo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (vm.uspartto == vm.lstParts[position].value) return
                vm.updatePartTo(vm.lstParts[position].value)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        vm.handler = Handler(Looper.getMainLooper())
        vm.settingsListener = this
        vm.getData()
    }

    override fun onDestroyView() {
        if (vm.settingsListener == this)
            vm.settingsListener = null
        super.onDestroyView()
    }

    override fun onGetData() {
        binding.spnLanguage.adapter = makeCustomAdapter(requireContext(), vm.lstLanguages) { it.langname }
        binding.spnLanguage.setSelection(vm.selectedLangIndex)
    }

    override fun onUpdateLang() {
        binding.spnVoice.makeCustomAdapter2(requireActivity(), vm.lstVoices, { it.voicelang }, { it.voicename })
        binding.spnVoice.setSelection(vm.selectedVoiceIndex)
        onUpdateVoice()

        binding.spnDictReference.makeCustomAdapter2(requireActivity(), vm.lstDictsReference, { it.dictname }, { it.url })
        binding.spnDictReference.setSelection(vm.selectedDictReferenceIndex)
        onUpdateDictReference()

        binding.spnDictNote.makeCustomAdapter2(requireActivity(), vm.lstDictsNote, { it.dictname }, { it.url })
        binding.spnDictNote.setSelection(vm.selectedDictNoteIndex)
        onUpdateDictNote()

        binding.spnDictTranslation.makeCustomAdapter2(requireActivity(), vm.lstDictsTranslation, { it.dictname }, { it.url })
        binding.spnDictTranslation.setSelection(vm.selectedDictTranslationIndex)
        onUpdateDictTranslation()

        binding.spnTextbook.makeCustomAdapter2(requireActivity(), vm.lstTextbooks, { it.textbookname }, { "${vm.unitCount} units" })
        binding.spnTextbook.setSelection(vm.selectedTextbookIndex)
        onUpdateTextbook()
    }

    override fun onUpdateTextbook() {
        run {
            val adapter = makeCustomAdapter(requireContext(), vm.lstUnits) { it.label }
            binding.spnUnitFrom.adapter = adapter
            binding.spnUnitTo.adapter = adapter

            onUpdateUnitFrom()
            onUpdateUnitTo()
        }
        run {
            val adapter = makeCustomAdapter(requireContext(), vm.lstParts) { it.label }
            binding.spnPartFrom.adapter = adapter
            binding.spnPartTo.adapter = adapter

            onUpdatePartFrom()
            onUpdatePartTo()
        }
        run {
            val adapter = makeCustomAdapter(requireContext(), vm.lstToTypes) { it.label }
            binding.spnToType.adapter = adapter
            binding.spnToType.setSelection(vm.toType.ordinal)
        }
    }

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
