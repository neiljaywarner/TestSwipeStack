package link.fls.swipestacksample;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

/**
 * Created by neil.warner on 5/10/17.
 */

public class StackLayoutManager extends RecyclerView.LayoutManager{

    private int verticalScrollOffset = 1;
    private int horizontalScrollOffset = 0;

    public static final String TAG = "SLM";

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.WRAP_CONTENT,
                RecyclerView.LayoutParams.WRAP_CONTENT);
    }




    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        //also checkout http://wiresareobsolete.com/2014/09/building-a-recyclerview-layoutmanager-part-1/
        fillVisibleChildren(recycler);
    }



    /// *** These values are with LinearLayoutManager horizontal orientation
   //  trlb/74/247/74/976
//
    /*74
05-10 17:59:45.971 28610-28610/link.fls.swipestacksample E/NJW: 462
            05-10 17:59:45.971 28610-28610/link.fls.swipestacksample E/NJW: 289
            05-10 17:59:45.971 28610-28610/link.fls.swipestacksample E/NJW: 976
    */

    Rect[] layoutInfo = {
            new Rect(74,74,205,976),
            new Rect(247,74,378,976),
            new Rect(420,74,551,976),
            new Rect(593,74,724,976),
            new Rect(766,74,897,976)



            //Note: perhaps the last one isnt' visible?
    };

    private void fillVisibleChildren(RecyclerView.Recycler recycler){
        fillVisibleChildren(recycler, 0);
    }
   // when scorlling, change pos plus or minus... maybe record it in a member variable
    public void fillVisibleChildren(RecyclerView.Recycler recycler, int top) {
        Log.d(TAG, "fill start with:" + top);
        //before we layout child views, we first scrap all current attached views

        detachAndScrapAttachedViews(recycler);

        //layoutInfo is a Rect[], each element contains coordinates for a view.

        //TODO: Change to do while so it can circle around
        for(int i = top; i < layoutInfo.length; i++){
            if(isVisible(i)){
                View view = recycler.getViewForPosition(i);
                addView(view);
                layoutDecorated(view, layoutInfo[i].left, layoutInfo[i].top - verticalScrollOffset,
                        layoutInfo[i].right, layoutInfo[i].bottom - verticalScrollOffset);
            }


        }
    }

    /*determine whether a child view is now visible
    **getVerticalSpace() and getHorizontalSpace() returns layout space minus paddings.
    **verticalScrollOffset and horizontalScrollOffset are current offset distances
    **according to the initial left top corner, before any scrolling.
    */
    private boolean isVisible(int index) {
        //Note: perhaps the last one isnt' visible?

        return true;
    }

    private int getVerticalSpace() {
        return 33;
    }

    @Override
    public boolean canScrollHorizontally() {
        Log.d(TAG, "CanscrollHorizontally=true");
        return true;
    }

    @Override
    public boolean canScrollVertically() {
        return false;
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        Log.d(TAG, "dx=" + dx);
        if (dx < 0) {
            Log.d(TAG, "scrolling left");
        } else {
            Log.d(TAG, "scrolling right");
        }

        if (state.didStructureChange()) {
            Log.d(TAG, "Structure changed");
        }
        
        if (state.isPreLayout()) {
            Log.d(TAG, "isPreLayout");
        }

        int travel;
        final int leftLimit = 0;
        final int rightLimit = findRightLimit(); //a helper method to find the rightmost child's right side.
        //-----> TODO: Modify this

        if(dx + horizontalScrollOffset < leftLimit){
            travel = horizontalScrollOffset;
            horizontalScrollOffset = leftLimit;
        }
        else if(dx + horizontalScrollOffset + getHorizontalSpace() > rightLimit){
            travel = rightLimit - horizontalScrollOffset - getHorizontalSpace();
            horizontalScrollOffset = rightLimit - getHorizontalSpace();
        }
        else{
            travel = dx;
            horizontalScrollOffset += dx;
        }


        fillVisibleChildren(recycler, 2);
        //requestLayout();

        //moveView(0,2);
        /*
        @return The actual distance scrolled. The return value will be negative if dx was
         * negative and scrolling proceeeded in that direction.
         * <code>Math.abs(result)</code> may be less than dx if a boundary was reached.
         */
        //Boundaries reached makes no sense. so far...
        //animations, swiping...
        return dx;
    }

    private int getHorizontalSpace() {
        return this.getWidth() - this.getPaddingRight();
    }

    private int findRightLimit() {
        // is it last visilbe or lst period?

        // Either way, our scroll will be totally different
        View thisView = this.getChildAt(getChildCount()-1);
        return  thisView.getRight();
    }




    // had to fill out several functions that they didn't do for us

    // STARTING WITH https://simpleandstupid.com/2015/05/01/recyclerview-and-its-custom-layoutmanager/

    // Might be helpful - 3 part series
    //http://wiresareobsolete.com/2014/09/building-a-recyclerview-layoutmanager-part-1/
}
