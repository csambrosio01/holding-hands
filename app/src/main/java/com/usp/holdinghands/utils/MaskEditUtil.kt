package com.usp.holdinghands.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

object MaskEditUtil {

    const val PHONE_MASK = "(##) #####-####"
    const val ZIP_MASK = "#####-###"
    const val DATE_MASK = "##/##/####"

    fun mask(editText: EditText, mask: String): TextWatcher {
        return object : TextWatcher {
            var isUpdating = false
            var old = ""

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val string = unmask(s.toString())
                var masked = ""

                if (isUpdating) {
                    old = string
                    isUpdating = false
                    return
                }

                var i = 0
                for (char in mask.toCharArray()) {
                    if (char != '#' && string.length > old.length) {
                        masked += char
                        continue
                    }
                    masked += try {
                        string[i]
                    } catch (e: Exception) {
                        break
                    }
                    i++
                }

                isUpdating = true
                editText.setText(masked)
                editText.setSelection(masked.length)
            }
        }
    }

    fun unmask(s: String): String {
        return s.replace("[^0-9]".toRegex(), "")
    }
}
