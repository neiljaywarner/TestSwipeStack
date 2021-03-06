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

package link.fls.swipestack;

import android.animation.Animator;
import android.content.res.Resources;
import android.util.Log;
import android.util.TypedValue;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import link.fls.swipestack.util.AnimationUtils;

public class SwipeHelper implements View.OnTouchListener {

    private final SwipeStack mSwipeStack;
    private View mObservedView;

    private boolean mListenForTouchEvents;
    private float mDownX;
    private float mDownY;
    private float mUpX;
    private float mUpY;
    private float mInitialX;
    private float mInitialY;
    private int mPointerId;

    private float mRotateDegrees = SwipeStack.DEFAULT_SWIPE_ROTATION;
    private float mOpacityEnd = SwipeStack.DEFAULT_SWIPE_OPACITY;
    private int mAnimationDuration = SwipeStack.DEFAULT_ANIMATION_DURATION;

    public SwipeHelper(SwipeStack swipeStack) {
        mSwipeStack = swipeStack;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_CANCEL:
                Log.e("NJW", "SwipeHelperaction cancel");
                //can swipe now b/c
                mPointerId = event.getPointerId(0);

                mUpX = event.getX(mPointerId);
                mUpY = event.getY(mPointerId);
                float scrolledX = Math.abs(mUpX - mDownX);
                float scrolledY = Math.abs(mUpY - mDownY);

                Log.e("NJW","--->dx/dy=" + scrolledX + "/" + scrolledY);

                if (scrolledY > scrolledX) {
                    Log.e("NJW", "--->dy>dx, scroll parent: dptoPix(dy)" + dpToPx(scrolledY));
                    View parent = (View) mSwipeStack.getParent();
                    //mSwipeStack.scrollBy(0, dpToPx(scrolledY)); //TODO: dpTOPix
                    ((View) mSwipeStack.getParent().getParent()).scrollBy(0, dpToPx(scrolledY));
                    return true;
                    //?? Is this a good idea?
                }
                break;
            case MotionEvent.ACTION_DOWN:
                Log.e("NJW", "SwipeHelperaction DOWN");

                if(!mListenForTouchEvents || !mSwipeStack.isEnabled()) {
                    return false;
                }

                //v.getParent().requestDisallowInterceptTouchEvent(true);
                // ----_> Put this back in if we dn't use a custom viewgroup or custom behaviour that needs to intercept.
                mSwipeStack.onSwipeStart();
                mPointerId = event.getPointerId(0);
                mDownX = event.getX(mPointerId);
                mDownY = event.getY(mPointerId);

                return true;

            case MotionEvent.ACTION_MOVE:
                Log.e("NJW", "SwipeHelperaction_move");

                int pointerIndex = event.findPointerIndex(mPointerId);
                if (pointerIndex < 0) return false;

                float dx = event.getX(pointerIndex) - mDownX;
                float dy = event.getY(pointerIndex) - mDownY;



                float newX = mObservedView.getX() + dx;
                float newY = mObservedView.getY() + dy;

                mObservedView.setX(newX);
               // mObservedView.setY(newY);
                //works to just comment out the above line.(except jiggles from slop)

                float dragDistanceX = newX - mInitialX;
                Log.e("NJW", "dragDistanceX" + Math.abs(dragDistanceX));


                float swipeProgress = Math.min(Math.max(
                        dragDistanceX / mSwipeStack.getWidth(), -1), 1);

                mSwipeStack.onSwipeProgress(swipeProgress);

                if (mRotateDegrees > 0) {
                    float rotation = mRotateDegrees * swipeProgress;
                    mObservedView.setRotation(rotation);
                }

                if (mOpacityEnd < 1f) {
                    float alpha = 1 - Math.min(Math.abs(swipeProgress * 2), 1);
                    mObservedView.setAlpha(alpha);
                }

                return true;

            case MotionEvent.ACTION_UP:
                Log.e("NJW", "SwipeHelperaction_up, about to onSwipeEnd, whihc i can modify");
                //v.getParent().requestDisallowInterceptTouchEvent(false);
                mSwipeStack.onSwipeEnd();
                checkViewPosition();

                return true;

        }

        return false;
    }

    //TODO: Consider if it's  good idea to use constant scale value like in circularProgressBar in subway app from Praveen
    // for consistency? Maybe ask in PR... reaedability is not as good imho
    private int dpToPx(float dy) {
        /// Converts 14 dip into its equivalent px
        /*
        Resources r = mSwipeStack.getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dy, r.getDisplayMetrics());
        //NOTE: Rounding is OK because it is for ScrollTo.
        // scrollParentDp() method Might even be a good idea
        return Math.round(px);
        */
        return Math.round(dy);
    }

    private void checkViewPosition() {
        if(!mSwipeStack.isEnabled()) {
            resetViewPosition();
            return;
        }

        float viewCenterHorizontal = mObservedView.getX() + (mObservedView.getWidth() / 2);
        float parentFirstThird = mSwipeStack.getWidth() / 3f;
        float parentLastThird = parentFirstThird * 2;

        if (viewCenterHorizontal < parentFirstThird &&
                mSwipeStack.getAllowedSwipeDirections() != SwipeStack.SWIPE_DIRECTION_ONLY_RIGHT) {
            swipeViewToLeft(mAnimationDuration / 2);
        } else if (viewCenterHorizontal > parentLastThird &&
                mSwipeStack.getAllowedSwipeDirections() != SwipeStack.SWIPE_DIRECTION_ONLY_LEFT) {
            swipeViewToRight(mAnimationDuration / 2);
        } else {
            resetViewPosition();
        }
    }

    private void resetViewPosition() {
        mObservedView.animate()
                .x(mInitialX)
                .y(mInitialY)
                .rotation(0)
                .alpha(1)
                .setDuration(mAnimationDuration)
                .setInterpolator(new OvershootInterpolator(1.4f))
                .setListener(null);
    }

    private void swipeViewToLeft(int duration) {
        if (!mListenForTouchEvents) return;
        mListenForTouchEvents = false;
        mObservedView.animate().cancel();
        mObservedView.animate()
                .x(-mSwipeStack.getWidth() + mObservedView.getX())
                .rotation(-mRotateDegrees)
                .alpha(0f)
                .setDuration(duration)
                .setListener(new AnimationUtils.AnimationEndListener() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mSwipeStack.onViewSwipedToLeft();
                    }
                });
    }

    private void swipeViewToRight(int duration) {
        if (!mListenForTouchEvents) return;
        mListenForTouchEvents = false;
        mObservedView.animate().cancel();
        mObservedView.animate()
                .x(mSwipeStack.getWidth() + mObservedView.getX())
                .rotation(mRotateDegrees)
                .alpha(0f)
                .setDuration(duration)
                .setListener(new AnimationUtils.AnimationEndListener() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mSwipeStack.onViewSwipedToRight();
                    }
                });
    }

    public void registerObservedView(View view, float initialX, float initialY) {
        if (view == null) return;
        mObservedView = view;
        mObservedView.setOnTouchListener(this);
        mInitialX = initialX;
        mInitialY = initialY;
        mListenForTouchEvents = true;
    }

    public void unregisterObservedView() {
        if (mObservedView != null) {
            mObservedView.setOnTouchListener(null);
        }
        mObservedView = null;
        mListenForTouchEvents = false;
    }

    public void setAnimationDuration(int duration) {
        mAnimationDuration = duration;
    }

    public void setRotation(float rotation) {
        mRotateDegrees = rotation;
    }

    public void setOpacityEnd(float alpha) {
        mOpacityEnd = alpha;
    }

    public void swipeViewToLeft() {
        swipeViewToLeft(mAnimationDuration);
    }

    public void swipeViewToRight() {
        swipeViewToRight(mAnimationDuration);
    }

}
