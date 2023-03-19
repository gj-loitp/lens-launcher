package com.roy.a

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.roy.R
import com.roy.ui.FApps
import com.roy.ui.FLens
import com.roy.ui.FSettings

class FragmentPagerAdapter(
    fragmentManager: FragmentManager,
    private val mContext: Context
) : FragmentStatePagerAdapter(fragmentManager) {

    companion object {
        private const val NUM_PAGES = 3
    }

    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> return FLens.newInstance()
            1 -> return FApps.newInstance()
            2 -> return FSettings.newInstance()
        }
        return Fragment()
    }

    override fun getCount(): Int {
        return NUM_PAGES
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when (position) {
            0 -> return mContext.resources.getString(R.string.tab_lens)
            1 -> return mContext.resources.getString(R.string.tab_apps)
            2 -> return mContext.resources.getString(R.string.tab_settings)
        }
        return super.getPageTitle(position)
    }
}
