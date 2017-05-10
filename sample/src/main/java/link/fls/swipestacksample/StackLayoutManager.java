package link.fls.swipestacksample;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by neil.warner on 5/10/17.
 */

public class StackLayoutManager extends RecyclerView.LayoutManager{

    private int verticalScrollOffset = 1;

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.WRAP_CONTENT,
                RecyclerView.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
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
        //before we layout child views, we first scrap all current attached views
        detachAndScrapAttachedViews(recycler);

        //layoutInfo is a Rect[], each element contains coordinates for a view.
        for(int i = 0; i < layoutInfo.length; i++){
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
}
