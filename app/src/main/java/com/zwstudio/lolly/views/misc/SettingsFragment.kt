package com.zwstudio.lolly.views.misc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.distinctUntilChanged
import com.zwstudio.lolly.viewmodels.misc.SettingsListener
import com.zwstudio.lolly.views.databinding.FragmentSettingsBinding
import io.reactivex.rxjava3.disposables.CompositeDisposable
import com.zwstudio.lolly.vmSettings

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

        vm.selectedLangIndex_.distinctUntilChanged().observe(viewLifecycleOwner) {
            if (!vm.busy)
                compositeDisposable.add(vm.updateLang().subscribe())
        }
        vm.selectedVoiceIndex_.distinctUntilChanged().observe(viewLifecycleOwner) {
            if (!vm.busy)
                compositeDisposable.add(vm.updateVoice().subscribe())
        }
        vm.selectedDictReferenceIndex_.distinctUntilChanged().observe(viewLifecycleOwner) {
            if (!vm.busy)
                compositeDisposable.add(vm.updateDictReference().subscribe())
        }
        vm.selectedDictNoteIndex_.distinctUntilChanged().observe(viewLifecycleOwner) {
            if (!vm.busy)
                compositeDisposable.add(vm.updateDictNote().subscribe())
        }
        vm.selectedDictTranslationIndex_.distinctUntilChanged().observe(viewLifecycleOwner) {
            if (!vm.busy)
                compositeDisposable.add(vm.updateDictTranslation().subscribe())
        }
        vm.selectedTextbookIndex_.distinctUntilChanged().observe(viewLifecycleOwner) {
            if (!vm.busy)
                compositeDisposable.add(vm.updateTextbook().subscribe())
        }
        vm.selectedUnitFromIndex_.distinctUntilChanged().observe(viewLifecycleOwner) {
            if (!vm.busy)
                compositeDisposable.add(vm.updateUnitFrom(vm.lstUnits[it].value).subscribe())
        }
        vm.selectedPartFromIndex_.distinctUntilChanged().observe(viewLifecycleOwner) {
            if (!vm.busy)
                compositeDisposable.add(vm.updatePartFrom(it).subscribe())
        }
        vm.toTypeIndex_.distinctUntilChanged().observe(viewLifecycleOwner) {
            val b = it == 2
            binding.spnUnitTo.isEnabled = b
            binding.spnPartTo.isEnabled = b && !vm.isSinglePart
            binding.btnPrevious.isEnabled = !b
            binding.btnNext.isEnabled = !b
            binding.spnPartFrom.isEnabled = it != 0 && !vm.isSinglePart
            if (!vm.busy)
                compositeDisposable.add(vm.updateToType(it).subscribe())
        }
        vm.selectedUnitToIndex_.distinctUntilChanged().observe(viewLifecycleOwner) {
            if (!vm.busy)
                compositeDisposable.add(vm.updateUnitTo(vm.lstUnits[it].value).subscribe())
        }
        vm.selectedPartToIndex_.distinctUntilChanged().observe(viewLifecycleOwner) {
            if (!vm.busy)
                compositeDisposable.add(vm.updatePartTo(vm.lstParts[it].value).subscribe())
        }

        binding.btnPrevious.setOnClickListener {
            compositeDisposable.add(vm.previousUnitPart().subscribe())
        }

        binding.btnNext.setOnClickListener {
            compositeDisposable.add(vm.nextUnitPart().subscribe())
        }

        vm.settingsListener = this
        compositeDisposable.add(vm.getData().subscribe())
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
