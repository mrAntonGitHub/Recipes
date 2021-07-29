package com.worldwidemobiledevelopment.recipes.utils

import android.text.Editable
import android.text.TextWatcher

/**
 * Class for convenient TextWatcher use
 **/

class DynamicTextWatcher(
    private val beforeChanged: ((CharSequence?, Int, Int, Int) -> Unit) = { _, _, _, _ -> },
    private val afterChanged: ((Editable?) -> Unit) = {},
    private val onChanged: ((CharSequence?, Int, Int, Int) -> Unit) = { _, _, _, _ -> }
) : TextWatcher{
    override fun beforeTextChanged(char: CharSequence?,  start: Int, count: Int, after: Int) {
        beforeChanged(char, start, count, after)
    }

    override fun onTextChanged(char: CharSequence?, start: Int, before: Int, count: Int) {
        onChanged(char, start, before, count)
    }

    override fun afterTextChanged(editable: Editable?) {
        afterChanged(editable)
    }
}