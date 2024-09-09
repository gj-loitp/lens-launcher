package com.mckimquyen.model

class Grid {
    var itemCount = 0
    var itemCountHorizontal = 0
    var itemCountVertical = 0
    var itemSize = 0f
    var spacingHorizontal = 0f
    var spacingVertical = 0f
    override fun toString(): String {
        return "Grid{" +
                "mItemCount=" + itemCount +
                ", mItemCountHorizontal=" + itemCountHorizontal +
                ", mItemCountVertical=" + itemCountVertical +
                ", mItemSize=" + itemSize +
                ", mSpacingHorizontal=" + spacingHorizontal +
                ", mSpacingVertical=" + spacingVertical +
                '}'
    }
}
