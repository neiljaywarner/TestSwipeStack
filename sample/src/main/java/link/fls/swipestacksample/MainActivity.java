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
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import link.fls.swipestack.SwipeStack;

public class MainActivity extends AppCompatActivity implements SwipeStack.SwipeStackListener, View.OnClickListener {

    private Button mButtonLeft, mButtonRight;

    private ArrayList<String> mData;
    private SwipeStack mSwipeStack;
    private SwipeStackAdapter mAdapter;
    private boolean doDispatch = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSwipeStack = (SwipeStack) findViewById(R.id.swipeStack);
        mButtonLeft = (Button) findViewById(R.id.buttonSwipeLeft);
        mButtonRight = (Button) findViewById(R.id.buttonSwipeRight);

        mButtonLeft.setOnClickListener(this);
        mButtonRight.setOnClickListener(this);

        mData = new ArrayList<>();
        mAdapter = new SwipeStackAdapter(mData);
        mSwipeStack.setAdapter(mAdapter);
        mSwipeStack.setListener(this);

        fillWithTestData();

        ViewGroup outer = (ViewGroup) findViewById(R.id.outer);


        NestedScrollView scrollView = (NestedScrollView) findViewById(R.id.scrollView);
        scrollView.setNestedScrollingEnabled(false);
        // with no SLL this might work.
    }

    private void fillWithTestData() {
        for (int x = 0; x < 5; x++) {
            mData.add(getString(R.string.dummy_text) + " " + (x + 1));
        }
    }


    //Hint from Stackoverflow post where I put bounty
    // been meaning to try it...


    // So I can use dispatchTouchEvent somewhere besides the activity if i want to..
    /*
    Touch Events propagates as

Activity.dispatchTouchEvent()
ViewGroup.dispatchTouchEvent()
View.dispatchTouchEvent()
View.onTouchEvent()
ViewGroup.onTouchEvent()
Activity.onTouchEvent()
If you use GestureDetector inside your Activity's dispatchTouchEvent() method you will be able to consume or propagate event to ViewGroups or Views correctly.



     */

    float mDown;
    /*
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        final GestureDetector myG;

        if (doDispatch) {
            return true;
        } else {
            return false;
        }
    }
    */

    float mDownX,mDownY;
/*
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.e("NJW", "Fixing to dispatch:" + MotionEvent.actionToString(ev.getAction()));
        boolean shouldDispatch;
        //TODO: Build an array
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.e("NJW", "Dispatch-down");
                setTitle("Down:" + ev.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e("NJW", "Dispatch-move");
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                Log.e("NJW", "Dispatch-cancel");
                setTitle(getTitle().toString() + "...Cancel" + ev.getY());

                break;

        }
        // e.g. dispatch if true/false...

        return super.dispatchTouchEvent(ev);
    }
    */

    @Override
    public void onClick(View v) {
        if (v.equals(mButtonLeft)) {
            mSwipeStack.swipeTopViewToLeft();
        } else if (v.equals(mButtonRight)) {
            mSwipeStack.swipeTopViewToRight();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuReset:
                mSwipeStack.resetStack();
                return true;
            case R.id.menuGitHub:
                Intent browserIntent = new Intent(
                        Intent.ACTION_VIEW, Uri.parse("https://github.com/flschweiger/SwipeStack"));
                startActivity(browserIntent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

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

    @Override
    public void onStackEmpty() {
        Toast.makeText(this, R.string.stack_empty, Toast.LENGTH_SHORT).show();
    }

    public class SwipeStackAdapter extends BaseAdapter {

        private List<String> mData;

        public SwipeStackAdapter(List<String> data) {
            this.mData = data;
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public String getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.card, parent, false);
            }

            TextView textViewCard = (TextView) convertView.findViewById(R.id.textViewCard);
            textViewCard.setText(mData.get(position));

            return convertView;
        }
    }
}
