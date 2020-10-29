package com.usp.holdinghands.utils.validators

import android.text.TextUtils
import com.google.android.material.textfield.TextInputLayout
import com.usp.holdinghands.App
import com.usp.holdinghands.R

class PasswordValidator(
    private val isMandatory: Boolean,
    private val textInputLayout: TextInputLayout,
    private val passwordInputLayout: TextInputLayout? = null
) : Validator(textInputLayout) {

    private val validPasswordRegex =
        Regex("""^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&#(){}:;><=.,^~_+-\[\]])[A-Za-z\d@$!%*?&#(){}:;><=.,^~_+-\[\]]{8,40}$""")

    override fun validate(text: String) {
        errorText = if (TextUtils.isEmpty(text)) {
            if (this.isMandatory) App.context!!.getString(R.string.mandatory_field)
            else ""
        } else if (text.length < 8 || !validPasswordRegex.matches(text)) {
            App.context!!.getString(R.string.wrong_password)
        } else if (passwordInputLayout != null && passwordInputLayout.editText!!.text.toString() != textInputLayout.editText!!.text.toString()) {
            App.context!!.getString(R.string.wrong_confirmation_password)
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
