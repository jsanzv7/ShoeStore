package com.udacity.shoestore.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import com.udacity.shoestore.data.LoginRepository
import com.udacity.shoestore.data.Result

import com.udacity.shoestore.R

enum class LoginState() {
    LOGIN,
    REGISTER,
    NOOP
}

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun login(username: String, password: String) {
        // can be launched in a separate asynchronous job
        val result = loginRepository.login(username, password)

        if (result is Result.Success) {
            _loginResult.value =
                LoginResult(success = LoggedInUserView(displayName = result.data.displayName))
        } else {
            _loginResult.value = LoginResult(error = R.string.login_failed)
        }
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains("@")) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }

    private var _emailText = MutableLiveData<String>()
    val emailText : LiveData<String>
        get() = _emailText

    private var _passwordText = MutableLiveData<String>()
    val passwordText : LiveData<String>
        get() = _passwordText

    private var _loginState = MutableLiveData<LoginState>()
    val loginState : LiveData<LoginState>
        get() = _loginState

    init {
        _emailText.value = ""
        _passwordText.value = ""
        _loginState.value = LoginState.NOOP
    }

    fun onRegister(email: String, password : String) {
        _emailText.value = email
        _passwordText.value = password
        _loginState.value = LoginState.REGISTER
    }

    fun onLogin(email: String? = null, password: String? = null) {
        _emailText.value = email ?: _emailText.value
        _passwordText.value = password ?: _passwordText.value
        _loginState.value = LoginState.LOGIN
    }

    /**
     * > When the user logs in, we want to set the login state to NOOP
     */
    fun onEventLoginComplete() {
        _loginState.value = LoginState.NOOP
    }
}