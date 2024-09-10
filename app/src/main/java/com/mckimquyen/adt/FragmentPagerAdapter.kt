package com.mckimquyen.adt

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.mckimquyen.R
import com.mckimquyen.ui.FrmApps
import com.mckimquyen.ui.FrmLens
import com.mckimquyen.ui.FrmSettings

class FragmentPagerAdapter(
    fragmentManager: FragmentManager,
    private val mContext: Context,
) : FragmentStatePagerAdapter(fragmentManager) {

    companion object {
        private const val NUM_PAGES = 3
    }

    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> return FrmLens.newInstance()
            1 -> return FrmApps.newInstance()
            2 -> return FrmSettings.newInstance()
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
