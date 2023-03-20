package com.roy.util;

import com.roy.enums.SortType;
import com.roy.model.App;
import com.roy.model.AppPersistent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class UtilAppSorter {

    public static void sort(ArrayList<App> apps, SortType sortType) {
        switch (sortType) {
            case LABEL_ASCENDING:
                sortByLabelAscending(apps);
                break;
            case LABEL_DESCENDING:
                sortByLabelDescending(apps);
                break;
            case INSTALL_DATE_ASCENDING:
                sortByInstallDateAscending(apps);
                break;
            case INSTALL_DATE_DESCENDING:
                sortByInstallDateDescending(apps);
                break;
            case OPEN_COUNT_ASCENDING:
                sortByOpenCountAscending(apps);
                break;
            case OPEN_COUNT_DESCENDING:
                sortByOpenCountDescending(apps);
                break;
            case ICON_COLOR_ASCENDING:
                sortByIconColorAscending(apps);
                break;
            case ICON_COLOR_DESCENDING:
                sortByIconColorDescending(apps);
                break;
            default:
                sortByLabelAscending(apps);
                break;
        }
    }

    private static void sortByLabelAscending(ArrayList<App> apps) {
        Collections.sort(apps, (a1, a2) -> (Objects.requireNonNull(a1.getLabel()).toString()).compareToIgnoreCase(Objects.requireNonNull(a2.getLabel()).toString()));
    }

    private static void sortByLabelDescending(ArrayList<App> apps) {
        sortByLabelAscending(apps);
        Collections.reverse(apps);
    }

    private static void sortByInstallDateAscending(ArrayList<App> apps) {
        Collections.sort(apps, (a1, a2) -> {
            if (a1.getInstallDate() > a2.getInstallDate()) {
                return -1;
            } else if (a1.getInstallDate() < a2.getInstallDate()) {
                return +1;
            }
            return 0;
        });
    }

    private static void sortByInstallDateDescending(ArrayList<App> apps) {
        sortByInstallDateAscending(apps);
        Collections.reverse(apps);
    }

    private static void sortByOpenCountAscending(ArrayList<App> apps) {
        Collections.sort(apps, (a1, a2) -> {
            long a1OpenCount =
                    AppPersistent.getAppOpenCount(Objects.requireNonNull(a1.getPackageName()).toString(), Objects.requireNonNull(a1.getName()).toString());
            long a2OpenCount =
                    AppPersistent.getAppOpenCount(Objects.requireNonNull(a2.getPackageName()).toString(), Objects.requireNonNull(a2.getName()).toString());
            if (a1OpenCount > a2OpenCount) {
                return -1;
            } else if (a1OpenCount < a2OpenCount) {
                return +1;
            }
            return 0;
        });
    }

    private static void sortByOpenCountDescending(ArrayList<App> apps) {
        sortByOpenCountAscending(apps);
        Collections.reverse(apps);
    }

    private static void sortByIconColorAscending(ArrayList<App> apps) {
        Collections.sort(apps, (a1, a2) -> {
            float a1HSVColor = UtilColor.getHueColorFromApp(a1);
            float a2HSVColor = UtilColor.getHueColorFromApp(a2);

            if (a1HSVColor > a2HSVColor) {
                return -1;
            } else if (a1HSVColor < a2HSVColor) {
                return +1;
            }
            return 0;
        });
    }

    private static void sortByIconColorDescending(ArrayList<App> apps) {
        sortByIconColorAscending(apps);
        Collections.reverse(apps);
    }
}
