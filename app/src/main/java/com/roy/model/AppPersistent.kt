package com.roy.model

import com.orm.SugarRecord
import com.orm.query.Condition
import com.orm.query.Select
import com.orm.util.NamingHelper

class AppPersistent(
    private var mPackageName: String,
    private var mName: String,
    openCount: Long,
    orderNumber: Int,
    appVisible: Boolean
) : SugarRecord() {
    private var identifier: String
    var openCount: Long
    var orderNumber: Int
    var isAppVisible: Boolean

    init {
        identifier = generateIdentifier(packageName = mPackageName, name = mName)
        this.openCount = openCount
        this.orderNumber = orderNumber
        isAppVisible = appVisible
    }

    var packageName: String
        get() = mPackageName
        set(packageName) {
            mPackageName = packageName
            identifier = generateIdentifier(packageName = mPackageName, name = mName)
        }
    var name: String
        get() = mName
        set(name) {
            mName = name
            identifier = generateIdentifier(packageName = mPackageName, name = mName)
        }

    override fun toString(): String {
        return "AppPersistent{mPackageName='$mPackageName', mName='$mName', mIdentifier='$identifier', mOpenCount=$openCount, mOrderNumber=$orderNumber, mAppVisible=$isAppVisible}"
    }

    companion object {
        private const val DEFAULT_APP_VISIBILITY = true
        private const val DEFAULT_ORDER_NUMBER = -1
        private const val DEFAULT_OPEN_COUNT: Long = 1

        fun generateIdentifier(
            packageName: String, name: String
        ): String {
            return "$packageName-$name"
        }

        @JvmStatic
        fun incrementAppCount(
            packageName: String, name: String
        ) {
            val identifier = generateIdentifier(packageName, name)
            val appPersistent = Select.from(
                AppPersistent::class.java
            ).where(Condition.prop(NamingHelper.toSQLNameDefault("mIdentifier")).eq(identifier))
                .first()
            if (appPersistent != null) {
                appPersistent.openCount = appPersistent.openCount + 1
                appPersistent.save()
            } else {
                val newAppPersistent = AppPersistent(
                    mPackageName = packageName,
                    mName = name,
                    openCount = DEFAULT_OPEN_COUNT,
                    orderNumber = DEFAULT_ORDER_NUMBER,
                    appVisible = DEFAULT_APP_VISIBILITY
                )
                newAppPersistent.save()
            }
        }

        fun setAppOrderNumber(
            packageName: String, name: String, orderNumber: Int
        ) {
            val identifier = generateIdentifier(packageName, name)
            val appPersistent = Select.from(
                AppPersistent::class.java
            ).where(Condition.prop(NamingHelper.toSQLNameDefault("mIdentifier")).eq(identifier))
                .first()
            if (appPersistent != null) {
                appPersistent.orderNumber = orderNumber
                appPersistent.save()
            } else {
                val newAppPersistent = AppPersistent(
                    mPackageName = packageName,
                    mName = name,
                    openCount = DEFAULT_OPEN_COUNT,
                    orderNumber = DEFAULT_ORDER_NUMBER,
                    appVisible = DEFAULT_APP_VISIBILITY
                )
                newAppPersistent.save()
            }
        }

        @JvmStatic
        fun getAppVisibility(
            packageName: String, name: String
        ): Boolean {
            return try {
                val identifier = generateIdentifier(packageName, name)
                val appPersistent = Select.from(
                    AppPersistent::class.java
                ).where(Condition.prop(NamingHelper.toSQLNameDefault("mIdentifier")).eq(identifier))
                    .first()
                appPersistent?.isAppVisible ?: true
            } catch (e: Exception) {
                true
            }
        }

        @JvmStatic
        fun setAppVisibility(
            packageName: String, name: String, mHideApp: Boolean
        ) {
            try {
                val identifier = generateIdentifier(packageName, name)
                val appPersistent = Select.from(
                    AppPersistent::class.java
                ).where(Condition.prop(NamingHelper.toSQLNameDefault("mIdentifier")).eq(identifier))
                    .first()
                if (appPersistent != null) {
                    appPersistent.isAppVisible = mHideApp
                    appPersistent.save()
                } else {
                    val newAppPersistent = AppPersistent(
                        mPackageName = packageName,
                        mName = name,
                        openCount = DEFAULT_OPEN_COUNT,
                        orderNumber = DEFAULT_ORDER_NUMBER,
                        appVisible = mHideApp
                    )
                    newAppPersistent.save()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        @JvmStatic
        fun getAppOpenCount(
            packageName: String, name: String
        ): Long {
            return try {
                val identifier = generateIdentifier(packageName, name)
                val appPersistent = Select.from(
                    AppPersistent::class.java
                ).where(Condition.prop(NamingHelper.toSQLNameDefault("mIdentifier")).eq(identifier))
                    .first()
                appPersistent?.openCount ?: 0
            } catch (e: Exception) {
                0
            }
        }
    }
}