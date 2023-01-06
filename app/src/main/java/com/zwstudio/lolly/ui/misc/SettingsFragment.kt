package com.zwstudio.lolly.ui.misc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import com.zwstudio.lolly.common.vmSettings
import com.zwstudio.lolly.databinding.FragmentSettingsBinding
import com.zwstudio.lolly.ui.common.autoCleared
import com.zwstudio.lolly.ui.common.makeCustomAdapter
import com.zwstudio.lolly.ui.common.makeCustomAdapter2
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class SettingsFragment : Fragment() {

    var vm = vmSettings
    var binding by autoCleared<FragmentSettingsBinding>()

    val compositeDisposable = CompositeDisposable()

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
            compositeDisposable.add(vm.previousUnitPart().subscribe())
        }

        binding.btnNext.setOnClickListener {
            compositeDisposable.add(vm.nextUnitPart().subscribe())
        }

        vm.lstLanguages_.onEach {
            binding.spnLanguage.adapter = makeCustomAdapter(requireContext(), vm.lstLanguages) { it.langname }
        }.launchIn(vm.viewModelScope)

        vm.lstVoices_.onEach {
            binding.spnVoice.makeCustomAdapter2(requireActivity(), vm.lstVoices, { it.voicelang }, { it.voicename })
        }.launchIn(vm.viewModelScope)

        vm.lstDictsReference_.onEach {
            binding.spnDictReference.makeCustomAdapter2(requireActivity(), vm.lstDictsReference, { it.dictname }, { it.url })
        }.launchIn(vm.viewModelScope)

        vm.lstDictsNote_.onEach {
            binding.spnDictNote.makeCustomAdapter2(requireActivity(), vm.lstDictsNote, { it.dictname }, { it.url })
        }.launchIn(vm.viewModelScope)

        vm.lstDictsTranslation_.onEach {
            binding.spnDictTranslation.makeCustomAdapter2(requireActivity(), vm.lstDictsTranslation, { it.dictname }, { it.url })
        }.launchIn(vm.viewModelScope)

        vm.lstTextbooks_.onEach {
            binding.spnTextbook.makeCustomAdapter2(requireActivity(), vm.lstTextbooks, { it.textbookname }, { "${vm.unitCount} units" })
        }.launchIn(vm.viewModelScope)

        vm.selectedTextbookIndex_.onEach {
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
        }.launchIn(vm.viewModelScope)

        compositeDisposable.add(vm.getData().subscribe())
    }
}
