package com.zwstudio.lolly.views.misc

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.androidisland.vita.VitaOwner
import com.androidisland.vita.vita
import com.zwstudio.lolly.GlobalUser
import com.zwstudio.lolly.viewmodels.misc.LoginViewModel
import com.zwstudio.lolly.views.databinding.FragmentLoginBinding
import io.reactivex.rxjava3.kotlin.subscribeBy

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
            vm.login().subscribeBy {
                GlobalUser.userid = it
                if (GlobalUser.userid.isEmpty())
                    AlertDialog.Builder(requireContext())
                        .setTitle("Login")
                        .setMessage("Wrong username or password!")
                        .show()
                else {
                    requireContext().getSharedPreferences("users", 0)
                        .edit()
                        .putString("userid", GlobalUser.userid)
                        .apply()
                    findNavController().navigateUp()
                }
            }
        }
    }
}