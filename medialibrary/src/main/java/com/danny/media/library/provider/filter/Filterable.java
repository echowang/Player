package com.danny.media.library.provider.filter;

import java.util.List;

/**
 * Created by tingw on 2018/1/22.
 */

public interface Filterable {
    void setFilter(Filter filter);
    List<Filter> getFilters();
}
