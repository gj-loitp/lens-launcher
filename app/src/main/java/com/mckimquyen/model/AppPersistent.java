package com.mckimquyen.model;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;
import com.orm.util.NamingHelper;

//2023.03.19 try to convert kotlin but failed
@Keep
public class AppPersistent extends SugarRecord {

    /* Required Default Constructor */
    public AppPersistent() {
    }

    private String mPackageName;
    private String mName;
    private String mIdentifier;
    private long mOpenCount;
    private int mOrderNumber;
    private boolean mAppVisible;
    private boolean mAppOpened;

    private static final boolean DEFAULT_APP_VISIBILITY = true;
    private static final boolean DEFAULT_APP_LOCK = true;
    private static final int DEFAULT_ORDER_NUMBER = -1;
    private static final long DEFAULT_OPEN_COUNT = 1;

    public AppPersistent(String packageName, String name, long openCount, int orderNumber, boolean appVisible, boolean appOpened) {
        this.mPackageName = packageName;
        this.mName = name;
        this.mIdentifier = AppPersistent.generateIdentifier(packageName, name);
        this.mOpenCount = openCount;
        this.mOrderNumber = orderNumber;
        this.mAppVisible = appVisible;
        this.mAppOpened = appOpened;
    }

    public String getPackageName() {
        return mPackageName;
    }

    public void setPackageName(String packageName) {
        this.mPackageName = packageName;
        this.mIdentifier = AppPersistent.generateIdentifier(this.mPackageName, this.mName);
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
        this.mIdentifier = AppPersistent.generateIdentifier(this.mPackageName, this.mName);
    }

    public String getIdentifier() {
        return mIdentifier;
    }

    public long getOpenCount() {
        return mOpenCount;
    }

    public void setOpenCount(long openCount) {
        this.mOpenCount = openCount;
    }

    public int getOrderNumber() {
        return mOrderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.mOrderNumber = orderNumber;
    }

    public boolean isAppVisible() {
        return mAppVisible;
    }

    public void setAppVisible(boolean appVisible) {
        this.mAppVisible = appVisible;
    }

    public boolean isAppOpened() {
        return mAppOpened;
    }

    public void setAppOpened(boolean appOpened) {
        this.mAppOpened = appOpened;
    }

    @NonNull
    @Override
    public String toString() {
        return "AppPersistent{" + "mPackageName='" + mPackageName + '\'' + ", mName='" + mName + '\'' + ", mIdentifier='" + mIdentifier + '\'' + ", mOpenCount=" + mOpenCount + ", mOrderNumber=" + mOrderNumber + ", mAppVisible=" + mAppVisible + ", mAppLock=" + mAppOpened + '}';
    }

    public static String generateIdentifier(String packageName, String name) {
        return packageName + "-" + name;
    }

    public static void incrementAppCount(String packageName, String name) {
        String identifier = AppPersistent.generateIdentifier(packageName, name);
        AppPersistent appPersistent = Select.from(AppPersistent.class).where(Condition.prop(NamingHelper.toSQLNameDefault("mIdentifier")).eq(identifier)).first();
        if (appPersistent != null) {
            appPersistent.setOpenCount(appPersistent.getOpenCount() + 1);
            appPersistent.save();
        } else {
            AppPersistent newAppPersistent = new AppPersistent(packageName, name, DEFAULT_OPEN_COUNT, DEFAULT_ORDER_NUMBER, DEFAULT_APP_VISIBILITY, DEFAULT_APP_LOCK);
            newAppPersistent.save();
        }
    }

    public static void setAppOrderNumber(String packageName, String name, int orderNumber) {
        String identifier = AppPersistent.generateIdentifier(packageName, name);
        AppPersistent appPersistent = Select.from(AppPersistent.class).where(Condition.prop(NamingHelper.toSQLNameDefault("mIdentifier")).eq(identifier)).first();
        if (appPersistent != null) {
            appPersistent.setOrderNumber(orderNumber);
            appPersistent.save();
        } else {
            AppPersistent newAppPersistent = new AppPersistent(packageName, name, DEFAULT_OPEN_COUNT, DEFAULT_ORDER_NUMBER, DEFAULT_APP_VISIBILITY, DEFAULT_APP_LOCK);
            newAppPersistent.save();
        }
    }

    public static boolean getAppOpened(String packageName, String name) {
        try {
            String identifier = AppPersistent.generateIdentifier(packageName, name);
            AppPersistent appPersistent = Select.from(AppPersistent.class).where(Condition.prop(NamingHelper.toSQLNameDefault("mIdentifier")).eq(identifier)).first();
            if (appPersistent != null) {
                return appPersistent.isAppOpened();
            } else {
                return true;
            }
        } catch (Exception e) {
            return true;
        }
    }

    public static void setAppOpened(String packageName, String name, boolean appLock) {
        String identifier = AppPersistent.generateIdentifier(packageName, name);
        AppPersistent appPersistent = Select.from(AppPersistent.class).where(Condition.prop(NamingHelper.toSQLNameDefault("mIdentifier")).eq(identifier)).first();
        if (appPersistent != null) {
            appPersistent.setAppOpened(appLock);
            appPersistent.save();
        } else {
            AppPersistent newAppPersistent = new AppPersistent(packageName, name, DEFAULT_OPEN_COUNT, DEFAULT_ORDER_NUMBER, DEFAULT_APP_VISIBILITY, appLock);
            newAppPersistent.save();
        }
    }

    public static boolean getAppVisibility(String packageName, String name) {
        try {
            String identifier = AppPersistent.generateIdentifier(packageName, name);
            AppPersistent appPersistent = Select.from(AppPersistent.class).where(Condition.prop(NamingHelper.toSQLNameDefault("mIdentifier")).eq(identifier)).first();
            if (appPersistent != null) {
                return appPersistent.isAppVisible();
            } else {
                return true;
            }
        } catch (Exception e) {
            return true;
        }
    }

    public static void setAppVisibility(String packageName, String name, boolean mHideApp) {
        String identifier = AppPersistent.generateIdentifier(packageName, name);
        AppPersistent appPersistent = Select.from(AppPersistent.class).where(Condition.prop(NamingHelper.toSQLNameDefault("mIdentifier")).eq(identifier)).first();
        if (appPersistent != null) {
            appPersistent.setAppVisible(mHideApp);
            appPersistent.save();
        } else {
            AppPersistent newAppPersistent = new AppPersistent(packageName, name, DEFAULT_OPEN_COUNT, DEFAULT_ORDER_NUMBER, mHideApp, DEFAULT_APP_LOCK);
            newAppPersistent.save();
        }
    }

    public static long getAppOpenCount(String packageName, String name) {
        try {
            String identifier = AppPersistent.generateIdentifier(packageName, name);
            AppPersistent appPersistent = Select.from(AppPersistent.class).where(Condition.prop(NamingHelper.toSQLNameDefault("mIdentifier")).eq(identifier)).first();
            if (appPersistent != null) {
                return appPersistent.getOpenCount();
            } else {
                return 0;
            }
        } catch (Exception e) {
            return 0;
        }
    }
}
