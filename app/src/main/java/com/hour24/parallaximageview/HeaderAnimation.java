package com.hour24.parallaximageview;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

/**
 * Created by N16326 on 2018. 7. 11..
 */

public class HeaderAnimation {

    private final String TAG = HeaderAnimation.class.getSimpleName();
    private final int EXTRA_HEIGHT = 6;

    private Activity mActivity;

    private RecyclerView mRecyclerView;
    private View mHeader; // 헤더
    private View mLogo; // 로고
    private View mSearchFixed; // 고정된 검색 버튼
    private View mSearch; // 검색 버튼
    private View mSearchBg; // 검색 버튼 배경

    private int mHeaderHeight; // 원래 헤더 높이
    private int mExtraHeight; // 추가 헤더 높이
    private int mLogoHeight; // 로고 높이
    private int mSearchBgHeight; // 검색 버튼 배경 높이
    private float mSearchFixedX; // 검색 버튼 고정된 X 값
    private float mSearchFixedY; // 검색 버튼 고정된 Y 값
    private float mSearchX; // 검색 버튼 X 값
    private float mSearchY; // 검색 버튼 Y 값
    private float mHeaderY; // 헤더 Y 값
    private float mDiffHeader; // 스크롤에 따라 변경할 헤더 값
    private float mDiffSearchX; // move - fix
    private float mDiffSearchY; // move - fix

    private ViewTreeObserver.OnGlobalLayoutListener mGlobalLayoutListener;
    private RecyclerView.OnScrollListener mOnScrollListener;

    public HeaderAnimation(Activity activity) {
        this.mActivity = activity;

        init();
        eventListener();
    }

    private void init() {

        mRecyclerView = (RecyclerView) mActivity.findViewById(R.id.recyclerview);
        mHeader = mActivity.findViewById(R.id.header); // 헤더
        mLogo = mActivity.findViewById(R.id.logo); // 로고
        mSearch = mActivity.findViewById(R.id.search); // 검색 버튼
        mSearchFixed = mActivity.findViewById(R.id.search_fixed); // 고정된 검색 버튼 (움직일 기준 값)
        mSearchBg = mActivity.findViewById(R.id.search_bg); // 검색 버튼 배경

        mHeaderHeight = mActivity.getResources().getDimensionPixelOffset(R.dimen.header);
        mSearchBgHeight = mActivity.getResources().getDimensionPixelOffset(R.dimen.search_bg_collapse);

        mExtraHeight = mHeaderHeight * EXTRA_HEIGHT; // 스크롤 Y 값과 header 높이 값이 맞지 않음

        // 0 > 투명, 1 > 불투명
        mHeader.setAlpha((float) 0.0);
        mHeader.setY(-mHeaderHeight); // 최초 헤더를 숨김

        mLogo.setAlpha((float) 0.0);
        mSearchBg.setAlpha((float) 1.0);

    }

    private void eventListener() {
        mGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {

                try {

                    Log.d(TAG, "mExtraHeight : " + mExtraHeight);

                    // 헤더 Y Point
                    mHeaderY = (float) mHeader.getY();
                    // 스크롤에 따라 이동할 헤더 값
                    mDiffHeader = (float) mHeaderHeight / mExtraHeight;
                    Log.d(TAG, "diffHeader : " + mDiffHeader);

                    // Move Background
                    Log.d(TAG, " searchBgHeight : " + mSearchBgHeight);

                    // Logo
                    mLogoHeight = mLogo.getHeight();

                    // 스크롤 후 이동할 View
                    mSearchFixedX = (float) mSearchFixed.getX();
                    mSearchFixedY = (float) mSearchFixed.getY();
                    Log.d(TAG, "searchFixedX : " + mSearchFixedX);
                    Log.d(TAG, "searchFixedY : " + mSearchFixedY);

                    // 스크롤에 따라 이동할 View
                    mSearchX = (float) mSearch.getX();
                    mSearchY = (float) mSearch.getY();
                    Log.d(TAG, "searchX : " + mSearchX);
                    Log.d(TAG, "searchY : " + mSearchY);

                    // 스크롤에 따라 이동할 값 (수치)
                    mDiffSearchX = (float) Math.abs(mSearchFixedX - mSearchX) / (mExtraHeight - mSearchBgHeight);
                    mDiffSearchY = (float) Math.abs(mSearchFixedY - mSearchY) / (mExtraHeight - mSearchBgHeight);
                    Log.d(TAG, "diffSearchX : " + mDiffSearchX);
                    Log.d(TAG, "diffSearchY : " + mDiffSearchY);


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        mRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    } else {
                        mRecyclerView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }

                    mRecyclerView.addOnScrollListener(mOnScrollListener);

                    // 검색 배경 Animation
                    animateShapeSearchBg();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        // View Tree Observer
        mRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(mGlobalLayoutListener);

        mOnScrollListener = new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                int y = recyclerView.computeVerticalScrollOffset();
                Log.d(TAG, "y : " + y);

                // Set Header
                mHeader.setY(getHeaderY(y));
                mHeader.setAlpha(getHeaderAlpha(y));

                // Set Logo
                mLogo.setAlpha(getLogoAlpha(y));

                // Set Alpha Background
                mSearchBg.setAlpha(getMoveBackgroundAlpha(y));

                // Set Move X, Y - moveBg 처리 후 움직임
                mSearch.setX(getMoveX(y));
                mSearch.setY(getMoveY(y));

            }
        };
    }

    // Animate view to get shrink effect.
    public void animateShapeSearchBg() {

        // 큰값 > 작은값
        ValueAnimator anim = ValueAnimator.ofInt(mSearchBg.getWidth(), mSearchBgHeight);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = mSearchBg.getLayoutParams();
                layoutParams.width = val;
                mSearchBg.requestLayout();
            }
        });

        anim.setDuration(500);
        anim.start();

        anim = ValueAnimator.ofInt(mSearchBg.getHeight(), mSearchBgHeight);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = mSearchBg.getLayoutParams();
                layoutParams.height = val;
                mSearchBg.requestLayout();
            }
        });

        anim.setDuration(500);
        anim.start();

    }

    // Get Alpha
    private float getHeaderAlpha(int y) {
        try {
            if (y < 1) {
                return (float) 0.0;
            } else if (y >= 1 && y <= mExtraHeight) {
                // Moving;
                return (float) y / mExtraHeight;
            } else {
                // 스크롤 내리고 난 뒤 위치
                return (float) 1.0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return (float) 1.0;
    }

    // Get Header Y Point
    private float getHeaderY(int y) {
        try {
            if (y < 1) {
                // 최초 위치
                return (float) -mHeaderHeight;
            } else if (y >= 1 && y <= mExtraHeight) {
                // Moving;
                return (float) mHeaderY + (mDiffHeader * y);
            } else {
                // 스크롤 내리고 난 뒤 위치
                return (float) 0.0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return (float) 0.0;
    }

    // Get Logo Alpha
    private float getLogoAlpha(float y) {
        try {
            if (y < mExtraHeight) {
                return (float) 0.0;
            } else if (y >= mExtraHeight && y <= (mExtraHeight + mLogoHeight)) {
                // Moving;
                return (float) (y - mExtraHeight) / mLogoHeight;
            } else {
                return (float) 1.0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return (float) 1.0;
    }

    // Get Move Background Alpha
    private float getMoveBackgroundAlpha(int y) {
        try {
            if (y < 1) {
                return (float) 1.0;
            } else if (y >= 1 && y <= mSearchBgHeight) {
                // Moving
                return (1 - (float) y / mSearchBgHeight);
            } else {
                return (float) 0.0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return (float) 0.0;
    }

    // Get Move X Point
    private float getMoveX(int y) {
        try {
            if (y < mSearchBgHeight) {
                // 최초 위치
                return mSearchX;
            } else if (y >= mSearchBgHeight && y <= mExtraHeight) {
                // Moving;
                return (float) mSearchX + (mDiffSearchX * (y - mSearchBgHeight));
            } else {
                // 스크롤 내리고 난 뒤 위치
                return mSearchFixedX;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mSearchFixedX;
    }

    // Get Move Y Point
    private float getMoveY(int y) {
        try {
            if (y < mSearchBgHeight) {
                // 최초 위치
                return mSearchY;
            } else if (y >= mSearchBgHeight && y <= mExtraHeight) {
                // Moving;
                return (float) mSearchY - (mDiffSearchY * (y - mSearchBgHeight));
            } else {
                // 스크롤 내리고 난 뒤 위치
                return mSearchFixedY;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mSearchFixedY;
    }
}
