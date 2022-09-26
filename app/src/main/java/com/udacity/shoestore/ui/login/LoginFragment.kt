package com.udacity.shoestore.ui.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.udacity.shoestore.R
import com.udacity.shoestore.databinding.FragmentLoginBinding
import com.udacity.shoestore.ui.login.LoginState.*

class LoginFragment : Fragment() {

    private lateinit var loginViewModel: LoginViewModel

    /* This is a backing property for the binding object. */
    private var _binding: FragmentLoginBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)

        val usernameEditText = binding.username
        val passwordEditText = binding.password
        val loginButton = binding.signIn
        val signupButton = binding.signUp
        val loadingProgressBar = binding.loading

        loginViewModel.loginFormState.observe(viewLifecycleOwner,
            Observer { loginFormState ->
                if (loginFormState == null) {
                    return@Observer
                }
                /* Enabling the login button if the form is valid. */
                loginButton.isEnabled = loginFormState.isDataValid
                loginFormState.usernameError?.let {
                    /* Setting the error message for the username field. */
                    usernameEditText.error = getString(it)
                }

                signupButton.isEnabled = loginFormState.isDataValid
                loginFormState.usernameError?.let {
                    /* Setting the error message for the username field. */
                    usernameEditText.error = getString(it)
                }

                loginFormState.passwordError?.let {
                    /* Setting the error message for the password field. */
                    passwordEditText.error = getString(it)
                }
            })

        loginViewModel.loginResult.observe(viewLifecycleOwner,
            Observer { loginResult ->
                loginResult ?: return@Observer
                loadingProgressBar.visibility = View.GONE
                loginResult.error?.let {
                    showLoginFailed(it)
                }
                loginResult.success?.let {
                    updateUiWithUser(it)
                }
            })

        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        binding.signIn.setOnClickListener {
            loginViewModel.login(binding.username.text.toString(),
                binding.password.text.toString())
        }

//        loginViewModel.loginState.observe(this as LifecycleOwner, Observer { ls ->
//            when (ls) {
//                REGISTER -> {
//                    navigateToWelcome()
//                    loginViewModel.onEventLoginComplete()
//                }
//                LoginState.LOGIN -> {
//                    navigateToShoeList()
//                    loginViewModel.onEventLoginComplete()
//                }
//                LOGIN -> TODO()
//                NOOP -> TODO()
//            }
//        })

        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // ignore
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // ignore
            }

            override fun afterTextChanged(s: Editable) {
                loginViewModel.loginDataChanged(
                    usernameEditText.text.toString(),
                    passwordEditText.text.toString()
                )
            }
        }
        usernameEditText.addTextChangedListener(afterTextChangedListener)
        passwordEditText.addTextChangedListener(afterTextChangedListener)
        passwordEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loginViewModel.login(
                    usernameEditText.text.toString(),
                    passwordEditText.text.toString()
                )
            }
            false
        }


        loginButton.setOnClickListener {
            loadingProgressBar.visibility = View.VISIBLE
            loginViewModel.login(
                usernameEditText.text.toString(),
                passwordEditText.text.toString()
            )
        }

        signupButton.setOnClickListener {
            loadingProgressBar.visibility = View.VISIBLE
            loginViewModel.login(
                usernameEditText.text.toString(),
                passwordEditText.text.toString()
            )
        }
    }

    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome) + model.displayName
        // TODO : initiate successful logged in experience

        val proceedToWelcome = LoginFragmentDirections.actionLoginToWelcome(LoginFormState(isDataValid = true).toString())
        findNavController().navigate(proceedToWelcome)

        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, welcome, Toast.LENGTH_LONG).show()
    }

    private fun navigateToWelcome() {
        val action = LoginFragmentDirections.actionLoginToWelcome(loginViewModel.emailText.value?:"")
        findNavController().navigate(action)
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, errorString, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}