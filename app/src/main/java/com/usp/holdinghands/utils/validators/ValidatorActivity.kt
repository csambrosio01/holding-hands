package com.usp.holdinghands.utils.validators

import android.widget.Button

interface ValidatorActivity {

    val validators: MutableList<Validator>

    fun setupValidators()

    fun validateFields(): Boolean {
        var isValid = true
        validators.forEach {
            it.validate()
            if (!it.isValid) isValid = false
        }
        return isValid
    }

    fun mainButtonClicked()

    fun showErrorMessage() {
        // TODO: "Should we show an error message?"
    }

    fun setupMainButton(mainButton: Button) {
        mainButton.setOnClickListener {
            if (validateFields()) {
                mainButtonClicked()
            } else {
                showErrorMessage()
            }
        }
    }
}
