package com.exampleproject.signup

import com.haroldadmin.vector.VectorState

data class SignUpViewState(
    val email: String = "",
    val password: String = "",
    val loading: Boolean = false,
    var result: MakeUserResponse? = null
) : VectorState