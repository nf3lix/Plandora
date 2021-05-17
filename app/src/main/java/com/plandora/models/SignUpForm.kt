package com.plandora.models

data class SignUpForm(val uniqueName: String, val displayName: String, val email: String,
                      val password: String, val repeatPassword: String)