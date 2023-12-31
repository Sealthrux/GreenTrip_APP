package com.example.greentrip.ui.auth.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.greentrip.ui.activity.HomeActivity
import com.example.greentrip.R
import com.example.greentrip.constants.Constants
import com.example.greentrip.constants.Constants.dataStore
import kotlinx.coroutines.flow.distinctUntilChanged
import com.example.greentrip.databinding.FragmentLoginBinding
import com.example.greentrip.models.AuthModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch


@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val viewModel by viewModels<LoginViewModel>()
    lateinit var binding: FragmentLoginBinding
    private lateinit var dataStore: DataStore<Preferences>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        collectStates()

        binding.btnLogin.setOnClickListener {
            callApi()

        }
        binding.forgetPassword.setOnClickListener {
           findNavController().navigate(R.id.action_loginFragment_to_forgetPasswordFragment)
        }

    }


    private fun callApi() {
        viewModel.loginUser(
                AuthModel(
                    email = binding.txtEmail.text.toString().trim(),
                    password = binding.txtPassword.text.toString().trim()
                )
            )
    }

    private fun collectStates() {
        viewLifecycleOwner.lifecycleScope.launch {

            viewModel.state
                .distinctUntilChanged()
                .onEach {
                    Log.e("collectStates: ", it.status.toString())
                }
                .collectLatest {

                    binding.loading.loadingIndicator.isIndeterminate = it.isLoading
                    binding.loading.loadingOverlay.isVisible = it.isLoading

                    if (!it.isLoading &&it.status != null) {
                        Toast.makeText(requireContext(), it.status, Toast.LENGTH_SHORT).show()
                    }

                    if (!it.isLoading && it.status == "success") {

                        saveToken(Constants.TOKEN, it.userLogin?.token.toString())

                        startActivity(Intent(requireContext(), HomeActivity::class.java))
                        requireActivity().finish()
                    }
                }

        }
    }

    private suspend fun saveToken(key: String, value: String) {
        dataStore = requireContext().dataStore
        val dataStoreKey = stringPreferencesKey(key)
        dataStore.edit {
            it[dataStoreKey] = value
        }
    }

}

