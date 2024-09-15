package com.mckimquyen.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mckimquyen.R
import com.mckimquyen.adt.AppAdapter
import com.mckimquyen.app.RAppsSingleton.Companion.instance
import com.mckimquyen.enums.SortType
import com.mckimquyen.itf.AppsInterface
import com.mckimquyen.model.App
import com.mckimquyen.services.BroadcastReceivers.AppsEditedReceiver
import com.mckimquyen.util.UtilSettings
import me.zhanghai.android.materialprogressbar.MaterialProgressBar

class FrmApps : Fragment(), AppsInterface {

    companion object {
        fun newInstance(): FrmApps {
            return FrmApps()
        }
    }

    private var rvApps: RecyclerView? = null
    private var progressBarApps: MaterialProgressBar? = null
    private var utilSettings: UtilSettings? = null
    private var indexScrolledItem = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.frm_apps, container, false)
        utilSettings = UtilSettings(requireContext())
        return view
    }

    override fun onViewCreated(
        view: View, savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        setupViews(view)
        instance?.apps?.let {
//            Log.d("", "onCreateView $it")
            setupRecycler(it)
        }
    }

    private fun setupViews(view: View) {
        rvApps = view.findViewById(R.id.rvApps)
        progressBarApps = view.findViewById(R.id.progressBarApps)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (activity != null && activity is ActSettings) {
            (activity as ActSettings?)?.setAppsInterface(this)
        }
    }

    private fun sendEditAppsBroadcast() {
        val editAppsIntent = Intent(activity, AppsEditedReceiver::class.java)
        activity?.sendBroadcast(editAppsIntent)
    }

    private fun setupRecycler(apps: ArrayList<App>?) {
//        Log.d("", "setupRecycler $apps")
        if (activity == null || apps?.size == 0) {
            progressBarApps?.isVisible = false
            return
        }
//        Log.d("", "apps?.size ${apps?.size}")
        rvApps?.layoutManager?.let { lm ->
            indexScrolledItem = (lm as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
        }

        progressBarApps?.isVisible = false
        rvApps?.isVisible = true

        val appAdapter = AppAdapter(activity, apps)
        rvApps?.apply {
            adapter = appAdapter
            layoutManager = GridLayoutManager(activity, resources.getInteger(R.integer.columns_apps))
            itemAnimator = DefaultItemAnimator()
            scrollToPosition(indexScrolledItem)
        }
        indexScrolledItem = 0
//        Log.d("", "done")
    }

    override fun onDefaultsReset() {
        utilSettings?.let { us ->
            if (us.sortType != SortType.values()[UtilSettings.DEFAULT_SORT_TYPE]) {
                us.save(UtilSettings.KEY_SORT_TYPE, UtilSettings.DEFAULT_SORT_TYPE)
                sendEditAppsBroadcast()
            }
        }
    }

    override fun onAppsUpdated(apps: ArrayList<App>?) {
//        Log.d("", "onAppsUpdated $apps")
        setupRecycler(apps)
    }
}
