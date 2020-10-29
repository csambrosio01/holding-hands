package com.usp.holdinghands.utils.validators

import android.text.TextUtils
import com.google.android.material.textfield.TextInputLayout
import com.usp.holdinghands.App
import com.usp.holdinghands.R

class TextValidator(
    private val isMandatory: Boolean,
    private val textInputLayout: TextInputLayout,
    private val minLength: Int? = null,
    private val maxLength: Int? = null,
    private val exactLength: Int? = null
) : Validator(textInputLayout) {

    override fun validate(text: String) {
        errorText = ""

        if (TextUtils.isEmpty(text)) {
            if (this.isMandatory) errorText =
                App.context!!.getString(R.string.mandatory_field)
        } else {
            if (minLength != null && text.length < minLength) {
                errorText = if (maxLength != null) App.context!!.getString(
                    R.string.wrong_min_max_length,
                    minLength.toString(),
                    maxLength.toString()
                )
                else App.context!!.getString(R.string.wrong_min_length, minLength.toString())
            }
            if (maxLength != null && text.length > maxLength) {
                errorText = if (minLength != null) App.context!!.getString(
                    R.string.wrong_min_max_length,
                    minLength.toString(),
                    maxLength.toString()
                )
                else App.context!!.getString(R.string.wrong_max_length, maxLength.toString())
            }
            if (exactLength != null && text.length != exactLength) {
                errorText =
                    App.context!!.getString(R.string.wrong_exact_length, exactLength.toString())
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
