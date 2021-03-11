package com.worldwidemobiledevelopment.recipes.utils.decorators

import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import com.worldwidemobiledevelopment.recipes.utils.InsetItemDecoration
import com.worldwidemobiledevelopment.recipes.view.home.INSET
import com.worldwidemobiledevelopment.recipes.view.home.INSET_TYPE_KEY

class InsetItemDecoration(@ColorInt backgroundColor: Int, @Dimension padding: Int) :
    InsetItemDecoration(backgroundColor, padding, INSET_TYPE_KEY, INSET)