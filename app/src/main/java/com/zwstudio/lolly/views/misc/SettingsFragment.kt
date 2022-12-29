package com.zwstudio.lolly.views.misc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.distinctUntilChanged
import com.zwstudio.lolly.viewmodels.misc.SettingsListener
import com.zwstudio.lolly.views.databinding.FragmentSettingsBinding
import com.zwstudio.lolly.vmSettings

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

        vm.selectedLangIndex_.distinctUntilChanged().observe(viewLifecycleOwner) {
            if (!vm.busy)
                vm.updateLang()
        }
        vm.selectedVoiceIndex_.distinctUntilChanged().observe(viewLifecycleOwner) {
            if (!vm.busy)
                vm.updateVoice()
        }
        vm.selectedDictReferenceIndex_.distinctUntilChanged().observe(viewLifecycleOwner) {
            if (!vm.busy)
                vm.updateDictReference()
        }
        vm.selectedDictNoteIndex_.distinctUntilChanged().observe(viewLifecycleOwner) {
            if (!vm.busy)
                vm.updateDictNote()
        }
        vm.selectedDictTranslationIndex_.distinctUntilChanged().observe(viewLifecycleOwner) {
            if (!vm.busy)
                vm.updateDictTranslation()
        }
        vm.selectedTextbookIndex_.distinctUntilChanged().observe(viewLifecycleOwner) {
            if (!vm.busy)
                vm.updateTextbook()
        }
        vm.selectedUnitFromIndex_.distinctUntilChanged().observe(viewLifecycleOwner) {
            if (!vm.busy)
                vm.updateUnitFrom(vm.lstUnits[it].value)
        }
        vm.selectedPartFromIndex_.distinctUntilChanged().observe(viewLifecycleOwner) {
            if (!vm.busy)
                vm.updatePartFrom(it)
        }
        vm.toTypeIndex_.distinctUntilChanged().observe(viewLifecycleOwner) {
            val b = it == 2
            binding.spnUnitTo.isEnabled = b
            binding.spnPartTo.isEnabled = b && !vm.isSinglePart
            binding.btnPrevious.isEnabled = !b
            binding.btnNext.isEnabled = !b
            binding.spnPartFrom.isEnabled = it != 0 && !vm.isSinglePart
            if (!vm.busy)
                vm.updateToType(it)
        }
        vm.selectedUnitToIndex_.distinctUntilChanged().observe(viewLifecycleOwner) {
            if (!vm.busy)
                vm.updateUnitTo(vm.lstUnits[it].value)
        }
        vm.selectedPartToIndex_.distinctUntilChanged().observe(viewLifecycleOwner) {
            if (!vm.busy)
                vm.updatePartTo(vm.lstParts[it].value)
        }

        binding.btnPrevious.setOnClickListener {
            vm.previousUnitPart()
        }

        binding.btnNext.setOnClickListener {
            vm.nextUnitPart()
        }

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
    }

    override fun onUpdateLang() {
        binding.spnVoice.makeCustomAdapter2(requireActivity(), vm.lstVoices, { it.voicelang }, { it.voicename })
        binding.spnDictReference.makeCustomAdapter2(requireActivity(), vm.lstDictsReference, { it.dictname }, { it.url })
        binding.spnDictNote.makeCustomAdapter2(requireActivity(), vm.lstDictsNote, { it.dictname }, { it.url })
        binding.spnDictTranslation.makeCustomAdapter2(requireActivity(), vm.lstDictsTranslation, { it.dictname }, { it.url })
        binding.spnTextbook.makeCustomAdapter2(requireActivity(), vm.lstTextbooks, { it.textbookname }, { "${vm.unitCount} units" })
    }

    override fun onUpdateTextbook() {
        run {
            val adapter = makeCustomAdapter(requireContext(), vm.lstUnits) { it.label }
            binding.spnUnitFrom.adapter = adapter
            binding.spnUnitTo.adapter = adapter
        }
        run {
            val adapter = makeCustomAdapter(requireContext(), vm.lstParts) { it.label }
            binding.spnPartFrom.adapter = adapter
            binding.spnPartTo.adapter = adapter
        }
        run {
            val adapter = makeCustomAdapter(requireContext(), vm.lstToTypes) { it.label }
            binding.spnToType.adapter = adapter
        }
    }
}
