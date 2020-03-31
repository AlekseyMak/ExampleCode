package com.exampleproject.signup

import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.exampleproject.signup.SignUpViewModel
import com.haroldadmin.vector.fragmentViewModel
import kotlinx.android.synthetic.main.sign_up_fragment.*
import org.koin.android.ext.android.inject
import reactivecircus.flowbinding.android.widget.textChanges
import timber.log.Timber

class SignUpFragment(override val layoutId: Int = R.layout.sign_up_fragment) : BaseFragment() {

    private val viewModel: SignUpViewModel by fragmentViewModel()

    private val userInfo: UserInfoRepository by inject()

    private val regStatus by lazy {
        userInfo.getUserProfile()?.registrationStatus ?: RegStatus.NOT_AUTHORIZED
    }

    private val signUpClickListener: (View) -> Unit = {
        viewModel.signUp()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        renderState(viewModel) { state ->
            if (!isAdded) {
                return@renderState
            }
            if (state.loading) {
                btSignUp.showLoading()
            } else {
                btSignUp.hideLoading()
            }
            Timber.i("Registrations status: $regStatus")
            when (regStatus) {
                RegStatus.NOT_AUTHORIZED -> Unit//no-op
                RegStatus.NEED_FILL_PROFILE -> {
                    findNavController().navigate(
                        SignUpFragmentDirections.actionFromSignUpToProfileFragment()
                    )
                    return@renderState
                }
                RegStatus.AUTHORIZED -> navigateToMain()
                else -> TODO()
            }

            state.result?.let {
                with(it.data) {
                    code?.let {
                        showError(it)
                    } ?: run {
                        if (this.registrationStatus == RegStatus.AUTHORIZED) {
                            navigateToMain()
                        } else {
                            findNavController().navigate(R.id.action_from_sign_up_to_profile_fragment)
                        }
                    }
                }
                state.result = null
            }
            val validEmail =
                ilEmail.validate(getString(R.string.sign_error_incorrect_email)) { state.email.isValidEmail() }
            val validPassword =
                ilPassword.validate(getString(R.string.sign_error_password_length)) { state.password.length >= 6 }

            val isEnabled = !state.loading && validEmail && validPassword
            btSignUp.setOnClickListener(if (isEnabled) signUpClickListener else null)
        }

        btSignIn.setOnClickListener {
            navigateToSignIn()
        }

        edEmail.textChanges()
            .onEach { viewModel.setEmail(it.toString()) }
            .launchIn(viewScope)
        edPassword.textChanges()
            .onEach { viewModel.setPassword(it.toString()) }
            .launchIn(viewScope)
    }

    private fun navigateToSignIn() {
        val direction =
            SignUpFragmentDirections.actionSignupToSigninFragment(
                btSignUp.text.toString()
            )
        view?.findNavController()?.navigate(direction)
    }

    private fun navigateToMain() {
        findNavController().navigate(R.id.action_global_searchFragment)
    }
}