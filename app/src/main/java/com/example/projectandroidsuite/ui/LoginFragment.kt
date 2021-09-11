package com.example.projectandroidsuite.ui

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.domain.SessionManager
import com.example.network.dto.auth.LoginRequest
import com.example.projectandroidsuite.R
import com.example.projectandroidsuite.databinding.FragmentLoginBinding
import com.example.projectandroidsuite.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class LoginFragment : Fragment() {

    val viewModel: LoginViewModel by activityViewModels()

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        if(sessionManager.isAuthenticated()){
            val action = LoginFragmentDirections.actionLoginFragmentToTaskProjectPagerFragment()
            findNavController().navigate(action)
        }

        val binding = FragmentLoginBinding.inflate(layoutInflater, container, false)

        binding.loginViewModel = viewModel

        viewModel.inputPortalValidated.observe(viewLifecycleOwner, {
            if (it == false) {
                binding.portalNameCaution.visibility = View.VISIBLE
                binding.portalNameCaution.text =
                    "please enter the portal name in a format xxxxxxxxx.onlyoffice.com/eu/sg"
            } else if (it == true && viewModel.canConnectToPortal.value == false) {
                binding.portalNameCaution.text = "checking if the portal exists..."
            }
        })

        viewModel.canConnectToPortal.observe(viewLifecycleOwner, {
            if (it == false && viewModel.inputPortalValidated.value == true) {
                binding.portalNameCaution.visibility = View.VISIBLE
            }
            if (it == true && viewModel.inputPortalValidated.value == true) {
                binding.portalNameCaution.visibility = View.GONE
            }
        })

        binding.buttonCheck.setOnClickListener {
            if (viewModel.inputPortalValidated.value == true &&
                viewModel.canConnectToPortal.value == true &&
                viewModel.inputEmailValidated.value == true &&
                viewModel.inputPasswordValidated.value == true
            ) {
                //Log.d("LoginFragment", "all is well")
                sessionManager.authenticate(
                    LoginRequest(
                        viewModel.emailInput.value!!,
                        viewModel.passwordInput.value!!
                    ), requireContext(),
                    viewModel.loginPortalInput.value!!
                )
            }
        }

        sessionManager.authenticated.observe(viewLifecycleOwner, {
            if (it == true) {
                Log.d("LoginFragment", "Is authenticated")
                val action = LoginFragmentDirections.actionLoginFragmentToTaskProjectPagerFragment()
                findNavController().navigate(action)
            }
        })



        binding.lifecycleOwner = activity

        return binding.root
    }
}