package com.zwstudio.lolly.ui.misc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.zwstudio.lolly.common.vmSettings
import com.zwstudio.lolly.databinding.FragmentSettingsBinding
import com.zwstudio.lolly.ui.common.autoCleared
import com.zwstudio.lolly.ui.common.makeCustomAdapter
import com.zwstudio.lolly.ui.common.makeCustomAdapter2
import com.zwstudio.lolly.viewmodels.misc.SettingsViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class SettingsFragment : Fragment() {

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

        binding.btnPrevious.setOnClickListener {
            vm.previousUnitPart()
        }

        binding.btnNext.setOnClickListener {
            vm.nextUnitPart()
        }

        binding.spnToType.adapter = makeCustomAdapter(requireContext(), SettingsViewModel.lstToTypes) { it.label }

        vm.lstLanguages_.onEach {
            binding.spnLanguage.adapter = makeCustomAdapter(requireContext(), vm.lstLanguages) { it.langname }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        vm.lstVoices_.onEach {
            binding.spnVoice.makeCustomAdapter2(requireActivity(), vm.lstVoices, { it.voicelang }, { it.voicename })
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        vm.lstDictsReference_.onEach {
            binding.spnDictReference.makeCustomAdapter2(requireActivity(), vm.lstDictsReference, { it.dictname }, { it.url })
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        vm.lstDictsNote_.onEach {
            binding.spnDictNote.makeCustomAdapter2(requireActivity(), vm.lstDictsNote, { it.dictname }, { it.url })
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        vm.lstDictsTranslation_.onEach {
            binding.spnDictTranslation.makeCustomAdapter2(requireActivity(), vm.lstDictsTranslation, { it.dictname }, { it.url })
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        vm.lstTextbooks_.onEach {
            binding.spnTextbook.makeCustomAdapter2(requireActivity(), vm.lstTextbooks, { it.textbookname }, { "${vm.unitCount} units" })
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        vm.lstUnits_.onEach {
            val adapter = makeCustomAdapter(requireContext(), vm.lstUnits) { it.label }
            binding.spnUnitFrom.adapter = adapter
            binding.spnUnitTo.adapter = adapter
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        vm.lstParts_.onEach {
            val adapter = makeCustomAdapter(requireContext(), vm.lstParts) { it.label }
            binding.spnPartFrom.adapter = adapter
            binding.spnPartTo.adapter = adapter
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        vm.getData()
    }
}
