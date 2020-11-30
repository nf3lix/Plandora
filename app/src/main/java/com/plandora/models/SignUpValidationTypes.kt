package com.plandora.models

import com.plandora.R

enum class SignUpValidationTypes(val messageId: Int) {

    EMPTY_UNIQUE_NAME(R.string.empty_unique_name),
    EMPTY_DISPLAY_NAME(R.string.empty_display_name),
    EMPTY_EMAIL(R.string.empty_email),
    EMPTY_PASSWORD(R.string.empty_password),
    PASSWORDS_DO_NOT_MATCH(R.string.passwords_do_not_match),
    SUCCESS(R.string.sign_up_valid_form)

}