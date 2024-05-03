package com.zwstudio.lolly.ui.misc

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.zwstudio.lolly.R
import com.zwstudio.lolly.databinding.FragmentLoginBinding
import com.zwstudio.lolly.ui.common.autoCleared
import com.zwstudio.lolly.viewmodels.misc.GlobalUserViewModel
import com.zwstudio.lolly.viewmodels.misc.LoginViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : Fragment() {

    val vm by viewModel<LoginViewModel>()
    var binding by autoCleared<FragmentLoginBinding>()

    val compositeDisposable = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            model = vm
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.login.setOnClickListener {
            compositeDisposable.add(vm.login(requireContext()).subscribe {
                if (GlobalUserViewModel.isLoggedIn)
                    findNavController().navigateUp()
                else
                    AlertDialog.Builder(requireContext())
                        .setTitle(requireContext().getString(R.string.login_string))
                        .setMessage(requireContext().getString(R.string.login_fail_message))
                        .show()
            })
        }
    }
}