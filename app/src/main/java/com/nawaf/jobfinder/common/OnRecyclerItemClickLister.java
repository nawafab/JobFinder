package com.nawaf.jobfinder.common;

/**
 * This generic interface is for the recyclerView item click call back
 * add the type you want to app with its object
 * @param <T> type of object you want to pass and retrieve
 */
public interface OnRecyclerItemClickLister<T>{

    void onItemClicked(T call);

}
