package com.zwstudio.lolly.ui.misc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.zwstudio.lolly.common.vmSettings
import com.zwstudio.lolly.databinding.FragmentSettingsBinding
import com.zwstudio.lolly.ui.common.autoCleared
import com.zwstudio.lolly.ui.common.makeCustomAdapter
import com.zwstudio.lolly.ui.common.makeCustomAdapter2
import com.zwstudio.lolly.viewmodels.misc.SettingsListener

class SettingsFragment : Fragment(), SettingsListener {

    var vm = vmSettings
    var binding by autoCleared<FragmentSettingsBinding>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            model = vm
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm.addObservers(viewLifecycleOwner)

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
