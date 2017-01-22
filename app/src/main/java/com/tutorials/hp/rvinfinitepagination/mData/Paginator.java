package com.tutorials.hp.rvinfinitepagination.mData;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.srx.widget.PullCallback;
import com.srx.widget.PullToLoadView;
import com.tutorials.hp.rvinfinitepagination.mRecycler.MyAdapter;

import java.util.ArrayList;

/**
 * Created by Oclemy on 12/8/2016 for ProgrammingWizards Channel and http://www.camposha.com.
 */
public class Paginator {

    Context c;
    private PullToLoadView pullToLoadView;
    RecyclerView rv;
    private MyAdapter adapter;
    private boolean isLoading = false;
    private boolean hasLoadedAll = false;
    private int nextPage;


    public Paginator(Context c, PullToLoadView pullToLoadView) {
        this.c = c;
        this.pullToLoadView = pullToLoadView;

        //RECYCLERVIEW
        RecyclerView rv=pullToLoadView.getRecyclerView();
        rv.setLayoutManager(new LinearLayoutManager(c, LinearLayoutManager.VERTICAL,false));

        adapter=new MyAdapter(c,new ArrayList<Spaceship>());
        rv.setAdapter(adapter);

        initializePaginator();
    }

/*
PAGE DATA
 */
    public void initializePaginator()
    {
        pullToLoadView.isLoadMoreEnabled(true);
        pullToLoadView.setPullCallback(new PullCallback() {

            //LOAD MORE DATA
            @Override
            public void onLoadMore() {
                loadData(nextPage);
            }

            //REFRESH AND TAKE US TO FIRST PAGE
            @Override
            public void onRefresh() {
                adapter.clear();
                hasLoadedAll=false;
                loadData(1);
            }

            //IS LOADING
            @Override
            public boolean isLoading() {
                return isLoading;
            }

            //CURRENT PAGE LOADED
            @Override
            public boolean hasLoadedAllItems() {
                return hasLoadedAll;
            }
        });

        pullToLoadView.initLoad();
    }

/*
 LOAD MORE DATA
 SIMULATE USING HANDLERS
 */
    public void loadData(final int page)
    {
       isLoading=true;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                //ADD CURRENT PAGE'S DATA
                for (int i=0;i<=5;i++)
                {
                    adapter.add(new Spaceship("Spaceship : "+String.valueOf(i)+" in Page : "+String.valueOf(page)));
                }

                //UPDATE PROPETIES
                pullToLoadView.setComplete();
                isLoading=false;
                nextPage=page+1;

            }
        },3000);
    }



}
