package com.widget.refreshlayout;

/**
 * Created by dkzwm on 2017/7/24.
 *
 * @author dkzwm
 */
public interface IRefreshViewCreator {
    IRefreshView<IIndicator> createHeader(SmoothRefreshLayout layout);

    IRefreshView<IIndicator> createFooter(SmoothRefreshLayout layout);
}
