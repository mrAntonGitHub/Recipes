package com.worldwidemobiledevelopment.recipes.adapters

import android.content.Context
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.worldwidemobiledevelopment.recipes.R
import com.worldwidemobiledevelopment.recipes.view.profile.BookedRecipesFragment
import com.worldwidemobiledevelopment.recipes.view.profile.MyRecipesFragment

private val TAB_TITLES = arrayOf(
    R.string.profile_tab_1,
    R.string.profile_tab_2
)

class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return when(position){
            0 -> { MyRecipesFragment() }
            1 -> { BookedRecipesFragment() }
            else -> {
                Log.e("SectionsPagerAdapter", "Wrong position = $position when only 1 and 2 possible")
                MyRecipesFragment()
            }
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        return TAB_TITLES.size
    }
}