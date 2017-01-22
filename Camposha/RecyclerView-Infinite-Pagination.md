# Android RecyclerView Infinite/Endless Pagination


An infinite/endless pagination example with a RecyclerView.If we reach the end of a given page,the next page gets automatically loaded.While its being loaded we display a progressbar.We are autogenerating data using handles to simulate download of fresh data.

## Overview
- Infinite/Endless pagination in a RecyclerView.
- RecyclerView consists of CardViews with text.
- If we reach the end of a page,we automatically load more data.
- While loading data we display a progressbar.
- We simulate downloading of data using handlers.
- We also implement Swipe to refresh.
- When we refresh we are taken back to the first page.
- The next page gets loaded in advance.
- We are using PullLoadView library.

# Setup
## Build.gradle
Lets fetch the library pulltoloadview from jcenter.
```groovy

dependencies {
    compile fileTree(dir: 'libs', include: ['*.jar'])
    testCompile 'junit:junit:4.12'
    compile 'com.android.support:appcompat-v7:24.2.1'
    compile 'com.android.support:design:24.2.1'
    compile 'com.android.support:cardview-v7:24.2.1'
    compile 'com.github.tosslife:pullloadview:1.1.0'
}

```

Inside our ContentMain.xml we have the pullloadview layout
```xml
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:paddingBottom="@dimen/activity_vertical_margin"
    android:paddingLeft="@dimen/activity_horizontal_margin"
    android:paddingRight="@dimen/activity_horizontal_margin"
    android:paddingTop="@dimen/activity_vertical_margin"
    app:layout_behavior="@string/appbar_scrolling_view_behavior"
    tools:context="com.tutorials.hp.rvinfinitepagination.MainActivity"
    tools:showIn="@layout/activity_main">

    <com.srx.widget.PullToLoadView
        android:id="@+id/pullToLoadView"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        />
</RelativeLayout>

```

#CLASSES
## Spacecraft
- Our data object.


```java
package com.tutorials.hp.rvinfinitepagination.mData;

/**
 *  * Created by Oclemmy on 12/9/2016 for ProgrammingWizards Channel and http://www.Camposha.info.

 */
public class Spaceship {
    private String name;


    public Spaceship(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}

```

## RecyclerView Adapter class
```java
package com.tutorials.hp.rvinfinitepagination.mRecycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tutorials.hp.rvinfinitepagination.R;
import com.tutorials.hp.rvinfinitepagination.mData.Spaceship;


import java.util.ArrayList;

/**
 * Created by Oclemmy on 12/9/2016 for ProgrammingWizards Channel and http://www.Camposha.info.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {

    Context c;
    ArrayList<Spaceship> spaceships;

    /*
    CONSTRUCTOR
     */
    public MyAdapter(Context c, ArrayList<Spaceship> spaceships) {
        this.c = c;
        this.spaceships = spaceships;
    }

    //INITIALIE VH
    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.model,parent,false);
        MyHolder holder=new MyHolder(v);
        return holder;
    }

    //BIND DATA
    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        holder.nametxt.setText(spaceships.get(position).toString());

    }

    /*
    TOTAL ITEMS
     */
    @Override
    public int getItemCount() {
        return spaceships.size();

    }

    /*
    ADD DATA TO ADAPTER
     */
    public void add(Spaceship s) {
        spaceships.add(s);
        notifyDataSetChanged();
    }

    /*
    CLEAR DATA FROM ADAPTER
     */
    public void clear() {
        spaceships.clear();
        notifyDataSetChanged();
    }

    /*
    VIEW HOLDER CLASS
     */
    class MyHolder extends RecyclerView.ViewHolder {

        TextView nametxt;

        public MyHolder(View itemView) {
            super(itemView);

            this.nametxt= (TextView) itemView.findViewById(R.id.nameTxt);

        }
    }

}

```

## Paginator class
- Shall handle our pagination.
- We simulate download of data with handlers.

```java
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
    private ArrayList<Spaceship> spaceships=new ArrayList<>();


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

```

## MainActivity
```java
package com.tutorials.hp.rvinfinitepagination;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.srx.widget.PullToLoadView;
import com.tutorials.hp.rvinfinitepagination.mData.Paginator;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        PullToLoadView pullToLoadView= (PullToLoadView) findViewById(R.id.pullToLoadView);
        new Paginator(this,pullToLoadView).initializePaginator();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }




}

```

## Demos

- Project Structure

![](/Camposha/demos/Project-Structure.PNG)


- RecyclerView Swipe To Refrsh
![](/Camposha/demos/RecyclerView-PullToRefresh.PNG)

- RecyclerView Pull To Load More with ProgressBar

![](/Camposha/demos/RecyclerView-infinite-Pagination.PNG)

- RecyclerView Infinite Pagination with ProgressBar

![](/Camposha/demos/RecyclerView-Infinite-ProgressBar.PNG)

## How to Run.
Clone the project into your android studio or download and extract the project and import to your android studio.

## More
Visit http://camposha.info for more examples like these.

Author :
Oclemy,cheers. http://camposha.info
