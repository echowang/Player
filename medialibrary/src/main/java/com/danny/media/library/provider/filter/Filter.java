package com.danny.media.library.provider.filter;

/**
 * Created by tingw on 2018/1/22.
 */

public abstract class Filter<T> {
    /**
     * @param t
     * @return   true:符合要求  false:不符合要求
     */
    public abstract boolean performFiltering(T t);

    public abstract String getFilterName();
}
