/*
 * Copyright (C) 2016 Frederik Schweiger
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package link.fls.swipestacksample;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity  {

    private ArrayList<String> mData;
    private RecyclerView mSwipeStack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mSwipeStack = (RecyclerView) findViewById(R.id.swipeStack);


        mData = new ArrayList<>();

        fillWithTestData();

        StackLayoutManager layoutManager = new StackLayoutManager();

        // LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        //layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mSwipeStack.setAdapter(new MYRecyclerVIewAdapter(mData));
        mSwipeStack.setLayoutManager(layoutManager);


        ((Button) findViewById(R.id.buttonSwipeLeft)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                log(mSwipeStack);

            }
        });

        ((Button) findViewById(R.id.buttonSwipeRight)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSwipeStack.scrollToPosition(3);

            }
        });

        mSwipeStack.setNestedScrollingEnabled(true);
        mSwipeStack.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                 Log.d("NJW", "onScrollstatechanged");
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                Log.d("NJW", "onScrolled");

                super.onScrolled(recyclerView, dx, dy);
            }
        });


        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
     //   mSwipeStack.setHasFixedSize(true);

        //This might help if we get themn to stack on top of each other

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                        Log.e("NJW", "onMove of recyclerView... targetViewHolder...");
                        return false;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                        Log.e("NJW", "onswipe of recyclerView... targetViewHolder...");

                        /// put it at the beignning and refresh.
                        //items.remove(viewHolder.getAdapterPosition());
                        //adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                    }
                };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mSwipeStack);

        ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);



    }

    private void log(RecyclerView stack) {
        Log.e("NJW", "log: count=" + stack.getChildCount());
        for (int i=0; i < stack.getChildCount(); i++) {
            View view = stack.getChildAt(i);
            Log.e("NJW", "START----" + i + "-----/>");

            Log.e("NJW", "l,t,r,b");
            Log.e("NJW", "" + view.getLeft() + "," + view.getTop() + "," + view.getRight() + "," + view.getBottom());

            Log.e("NJW", "END----" + i + "-----/>");


        }
    }

    private void fillWithTestData() {
        for (int x = 0; x < 5; x++) {
            mData.add(" " + (x + 1));
        }
    }

/*
    @Override
    public void onViewSwipedToRight(int position) {
        String swipedElement = mAdapter.getItem(position);
        Toast.makeText(this, getString(R.string.view_swiped_right, swipedElement),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onViewSwipedToLeft(int position) {
        String swipedElement = mAdapter.getItem(position);
        Toast.makeText(this, getString(R.string.view_swiped_left, swipedElement),
                Toast.LENGTH_SHORT).show();
    }
    */



}
