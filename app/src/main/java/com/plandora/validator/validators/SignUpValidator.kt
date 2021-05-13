package com.plandora.validator.validators

import android.text.TextUtils
import com.plandora.R
import com.plandora.activity.launch.SignUpActivity
import com.plandora.models.SignUpForm
import com.plandora.validator.ValidationResult
import com.plandora.validator.Validator

class SignUpValidator(private val activity: SignUpActivity) : Validator {

    override fun <T> getValidationState(data: T): ValidationResult {
        if(data !is SignUpForm) {
            throw Exception()
        }
        return when {
            TextUtils.isEmpty(data.uniqueName) -> ValidationResults.EmptyUniqueNameResult(activity)
            TextUtils.isEmpty(data.displayName) -> ValidationResults.EmptyDisplayNameResult(activity)
            TextUtils.isEmpty(data.email) -> ValidationResults.EmptyEmailResult(activity)
            TextUtils.isEmpty(data.password) -> ValidationResults.EmptyPasswordResult(activity)
            (data.password != data.repeatPassword) -> ValidationResults.PasswordDoNotMatchResult(activity)
            else -> ValidationResults.ValidDataResult(activity)
        }
    }

    sealed class ValidationResults {
        class EmptyUniqueNameResult(activity: SignUpActivity) : ValidationResult(Validator.ValidationState.INVALID, activity.getString((R.string.empty_unique_name)))
        class EmptyDisplayNameResult(activity: SignUpActivity) : ValidationResult(Validator.ValidationState.INVALID, activity.getString((R.string.empty_display_name)))
        class EmptyEmailResult(activity: SignUpActivity) : ValidationResult(Validator.ValidationState.INVALID, activity.getString((R.string.empty_email)))
        class EmptyPasswordResult(activity: SignUpActivity) : ValidationResult(Validator.ValidationState.INVALID, activity.getString((R.string.empty_password)))
        class PasswordDoNotMatchResult(activity: SignUpActivity) : ValidationResult(Validator.ValidationState.INVALID, activity.getString((R.string.passwords_do_not_match)))
        class ValidDataResult(activity: SignUpActivity) : ValidationResult(Validator.ValidationState.VALID, activity.getString((R.string.sign_up_valid_form)))
    }

}