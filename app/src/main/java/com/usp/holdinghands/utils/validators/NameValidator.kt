package com.usp.holdinghands.utils.validators

import android.text.TextUtils
import com.google.android.material.textfield.TextInputLayout
import com.usp.holdinghands.App
import com.usp.holdinghands.R

class NameValidator(
    private val isMandatory: Boolean,
    private val textInputLayout: TextInputLayout
) : Validator(textInputLayout) {

    override fun validate(text: String) {
        errorText = ""

        if (TextUtils.isEmpty(text)) {
            if (this.isMandatory) errorText =
                App.context!!.getString(R.string.mandatory_field)
        } else {
            val strings = text.split(" ")

            if (strings.size < 2) {
                errorText = App.context!!.getString(R.string.wrong_name)
            } else {
                for (word in strings) {
                    if (word.length < 2) {
                        errorText = App.context!!.getString(R.string.wrong_name)
                    }
                }
            }
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
