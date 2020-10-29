package com.usp.holdinghands.utils.validators

import android.view.View
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

abstract class Validator(
    private val textInputLayout: TextInputLayout
) : View.OnFocusChangeListener {

    var isValid = true
    var errorText = ""

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        if (!hasFocus) {
            validate((v as TextInputEditText).text.toString())
        }
    }

    abstract fun validate(text: String = textInputLayout.editText!!.text.toString())
}
