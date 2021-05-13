package com.plandora.validator.validators

import android.text.TextUtils
import com.plandora.R
import com.plandora.activity.launch.SignInActivity
import com.plandora.models.SignInForm
import com.plandora.validator.ValidationResult
import com.plandora.validator.Validator

class SignInValidator(private val activity: SignInActivity): Validator {

    override fun <T> getValidationState(data: T): ValidationResult {
        if(data !is SignInForm) {
            throw Exception()
        }
        return when {
            TextUtils.isEmpty(data.email) -> ValidationResults.EmptyEmailResult(activity)
            TextUtils.isEmpty(data.password) -> ValidationResults.EmptyPasswordResult(activity)
            else -> ValidationResults.ValidDataResult(activity)
        }
    }

    sealed class ValidationResults {
        class EmptyEmailResult(activity: SignInActivity) : ValidationResult(Validator.ValidationState.INVALID, activity.getString((R.string.empty_email)))
        class EmptyPasswordResult(activity: SignInActivity) : ValidationResult(Validator.ValidationState.INVALID, activity.getString((R.string.empty_password)))
        class ValidDataResult(activity: SignInActivity) : ValidationResult(Validator.ValidationState.VALID, activity.getString((R.string.sign_up_valid_form)))
    }

}