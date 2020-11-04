package com.usp.holdinghands.utils.validators

import android.text.TextUtils
import com.google.android.material.textfield.TextInputLayout
import com.usp.holdinghands.App
import com.usp.holdinghands.R
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class DateValidator(
    private val isMandatory: Boolean,
    private val textInputLayout: TextInputLayout,
    private val formatter: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))
) : Validator(textInputLayout) {

    override fun validate(text: String) {
        errorText = ""

        if (TextUtils.isEmpty(text)) {
            if (this.isMandatory) errorText =
                App.context!!.getString(R.string.mandatory_field)
        } else {
            try {
                formatter.parse(text)
            } catch (e: Exception) {
                errorText = App.context!!.getString(R.string.wrong_date, formatter.toPattern())
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
