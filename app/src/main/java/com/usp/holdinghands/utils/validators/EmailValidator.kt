package com.usp.holdinghands.utils.validators

import android.text.TextUtils
import com.google.android.material.textfield.TextInputLayout
import com.usp.holdinghands.App
import com.usp.holdinghands.R

class EmailValidator(
    private val isMandatory: Boolean,
    private val textInputLayout: TextInputLayout
) : Validator(textInputLayout) {

    private val validEmailRegex =
        Regex("^(([^<>()\\[\\]\\\\.,;:\\s@\"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$")

    override fun validate(text: String) {
        errorText = if (TextUtils.isEmpty(text)) {
            if (this.isMandatory) App.context!!.getString(R.string.mandatory_field)
            else ""
        } else if (!validEmailRegex.matches(text)) {
            App.context!!.getString(R.string.wrong_email)
        } else {
            ""
        }

        if (errorText.isEmpty()) {
            isValid = true
            textInputLayout.isErrorEnabled = false
            textInputLayout.error = ""
        } else {
            isValid = false
            textInputLayout.isErrorEnabled = true
            textInputLayout.error = errorText
        }
    }
}
