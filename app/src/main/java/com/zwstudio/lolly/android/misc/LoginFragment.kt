package com.zwstudio.lolly.android.misc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import com.androidisland.vita.VitaOwner
import com.androidisland.vita.vita
import com.zwstudio.lolly.android.databinding.FragmentLoginBinding
import com.zwstudio.lolly.data.misc.Global
import com.zwstudio.lolly.data.misc.LoginViewModel
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    val vm by lazy { vita.with(VitaOwner.Single(this)).getViewModel<LoginViewModel>() }
    var binding by autoCleared<FragmentLoginBinding>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            model = vm
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.login.setOnClickListener {
            vm.viewModelScope.launch {
                Global.userid = vm.login()
            }
        }
    }
}