package com.roy.enums;

import com.roy.R;

public enum SortType {
    LABEL_ASCENDING(R.string.setting_sort_type_label_ascending),
    LABEL_DESCENDING(R.string.setting_sort_type_label_descending),
    INSTALL_DATE_ASCENDING(R.string.setting_sort_type_install_date_ascending),
    INSTALL_DATE_DESCENDING(R.string.setting_sort_type_install_date_descending),
    ICON_COLOR_ASCENDING(R.string.setting_sort_type_icon_color_ascending),
    ICON_COLOR_DESCENDING(R.string.setting_sort_type_icon_color_descending),
    OPEN_COUNT_ASCENDING(R.string.setting_sort_type_open_count_ascending),
    OPEN_COUNT_DESCENDING(R.string.setting_sort_type_open_count_descending);

    final int mDisplayNameResId;

    SortType(int displayNameResId) {
        mDisplayNameResId = displayNameResId;
    }

    public int getDisplayNameResId() {
        return mDisplayNameResId;
    }
}
