package link.fls.swipestacksample;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import link.fls.swipestack.SwipeStack;

/**
 * Created by neil.warner on 5/11/17.
 */

public class SpecialLinearLayout extends LinearLayout {

    public SpecialLinearLayout(Context context) {
        super(context);
    }

    public SpecialLinearLayout(Context context,
            @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SpecialLinearLayout(Context context,
            @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    boolean handled = true;
    //dispatch = true and it goes up and down
    float mDownX = 0;
    float mDownY = 0;

    public static boolean isYScrolling = false;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        // if down is passed, then move, then it knows waht type of scroll...
        // down then click should go through...

        // **** NOTE: at first true, they stop going down...
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                Log.d("M11_SLL",  "oninttouch event- DOWN, buble it down");

                mDownX = event.getX();
                mDownY = event.getY();
                // bubble the down event dow n
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d("M11_SLL",  "oninttouch event- MOVE");

                //scroll somewhere... x or y?
                float dy = Math.abs(event.getY() - mDownY);
                float dx = Math.abs(event.getX() - mDownX);
                Log.d("M11","dy=" + dy);
                Log.d("M11","dx=" + dx);
                // TODO: Fingers are sloppy; also check touch sensitivity > slop
                //to make sure touch works
                if (dy > dx && dy > 33) {
                    isYScrolling = true;
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:

                Log.d("M11_SLL", "oninttouchup or cancel");
        }
        return super.onInterceptTouchEvent(event);
        //return true ifintercepter
    }


    //if intercepted, stop, true, handled
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Log.d("NJW_M11", "------ViewGroup: onTouchEvent" +MotionEvent.actionToString(event.getAction()));
        }
        Log.d("NJW_M11", "*** ViewGroup: onTouchEvent:IF Y-SCROLLING, Send cancel event down to child");
        // ONce one iof these is intercepted, the chain stays here.
        return true;
    }

    /*

    public boolean dispatchTouchEvent(MotionEvent ev) {
        super.dispatchTouchEvent(ev);
        if (handled) {
            return true;
        } else {
            return false;
        }
    }
    */
}
