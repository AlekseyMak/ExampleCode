package com.exampleproject.signup

import androidx.lifecycle.viewModelScope
import com.haroldadmin.vector.VectorViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.core.KoinComponent
import org.koin.core.inject
import timber.log.Timber

class SignUpViewModel(
    initialState: SignUpViewState
) : VectorViewModel<SignUpViewState>(initialState), KoinComponent {

    private val userInfo: UserInfoRepository by inject()

    @ExperimentalCoroutinesApi
    fun signUp() {
        viewModelScope.launch {
            Client.userPipelineTubeService.register(currentState.email, currentState.password)
                .onStart { setState { copy(loading = true) } }
                .catch {
                    Timber.e(it)
                    setState { copy(result = null, loading = false) }
                }
                .onEach {
                    Timber.e("Have response -> ${it.toString()}")
                    userInfo.saveDataFromMakeUser(it)
                    setState { copy(result = it, loading = false) }
                }
                .first()
        }
    }

    fun setEmail(email: String) {
        setState { copy(email = email) }
    }

    fun setPassword(password: String) {
        setState { copy(password = password) }
    }

}