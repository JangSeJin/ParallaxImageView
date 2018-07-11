package com.hour24.parallaximageview;

import android.app.Activity;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * Created by N16326 on 2018. 7. 11..
 */

public class HeaderScroll {

    private final String TAG = HeaderScroll.class.getSimpleName();
    private final int EXTRA_HEIGHT = 6;

    private Activity mActivity;

    private RecyclerView mRecyclerView;
    private View mHeader;
    private View mLogo;
    private View mFix;
    private View mMove;
    private View mMoveBg;

    private int mHeight; // 원래 헤더 높이
    private int mExtraHeight; // 추가 헤더 높이
    private int mLogoHeight;
    private int mMoveHeight;
    private int mMoveBgHeight;
    private float mFixX;
    private float mFixY;
    private float mMoveX;
    private float mMoveY;
    private float mHeaderY;
    private float mDiffHeader;
    private float mDiffX;
    private float mDiffY;

    private ViewTreeObserver.OnGlobalLayoutListener mGlobalLayoutListener;
    private RecyclerView.OnScrollListener mOnScrollListener;

    public HeaderScroll(Activity activity) {
        this.mActivity = activity;

        init();
        eventListener();
    }

    private void init() {

        mRecyclerView = (RecyclerView) mActivity.findViewById(R.id.recyclerview);
        mHeader = mActivity.findViewById(R.id.header); // 헤더
        mLogo = mActivity.findViewById(R.id.logo); // 로고
        mFix = mActivity.findViewById(R.id.fix); // 기준 View
        mMove = mActivity.findViewById(R.id.move);
        mMoveBg = mActivity.findViewById(R.id.move_bg);

        mHeight = mActivity.getResources().getDimensionPixelOffset(R.dimen.header);
        mExtraHeight = mHeight * EXTRA_HEIGHT; // 스크롤 Y 값과 header 높이 값이 맞지 않음

        // 0 > 투명, 1 > 불투명
        mHeader.setAlpha((float) 0.0);
        mHeader.setY(-mHeight); // 최초 헤더를 숨김

        mLogo.setAlpha((float) 0.0);
        mMoveBg.setAlpha((float) 1.0);

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
                    mDiffHeader = (float) mHeight / mExtraHeight;
                    Log.d(TAG, "mDiffHeader : " + mDiffHeader);


                    // Move Background
                    mMoveHeight = mMove.getHeight();
                    Log.d(TAG, "mMoveHeight : " + mMoveHeight);

                    // Move Background
                    mMoveBgHeight = mMoveBg.getHeight();
                    Log.d(TAG, "mMoveBgHeight : " + mMoveBgHeight);

                    // Logo
                    mLogoHeight = mLogo.getHeight();

                    // 스크롤 후 이동할 View
                    mFixX = (float) mFix.getX();
                    mFixY = (float) mFix.getY();
                    Log.d(TAG, "fixX : " + mFixX);
                    Log.d(TAG, "fixY : " + mFixY);

                    // 스크롤에 따라 이동할 View
                    mMoveX = (float) mMove.getX();
                    mMoveY = (float) mMove.getY();
                    Log.d(TAG, "moveX : " + mMoveX);
                    Log.d(TAG, "moveY : " + mMoveY);

                    // 스크롤에 따라 이동할 값 (수치)
                    mDiffX = (float) Math.abs(mFixX - mMoveX) / (mExtraHeight - mMoveBgHeight);
                    mDiffY = (float) Math.abs(mFixY - mMoveY) / (mExtraHeight - mMoveBgHeight);
                    Log.d(TAG, "diffX : " + mDiffX);
                    Log.d(TAG, "diffY : " + mDiffY);


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        mRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    } else {
                        mRecyclerView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }

                    mRecyclerView.addOnScrollListener(mOnScrollListener);

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
                mMoveBg.setAlpha(getMoveBackgroundAlpha(y));

                // Set Move X, Y - moveBg 처리 후 움직임
                mMove.setX(getMoveX(y));
                mMove.setY(getMoveY(y));

            }
        };
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
                return (float) -mHeight;
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
            } else if (y >= 1 && y <= mMoveBgHeight) {
                // Moving
                return (1 - (float) y / mMoveBgHeight);
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
            if (y < mMoveBgHeight) {
                // 최초 위치
                return mMoveX;
            } else if (y >= mMoveBgHeight && y <= mExtraHeight) {
                // Moving;
                return (float) mMoveX + (mDiffX * (y - mMoveBgHeight));
            } else {
                // 스크롤 내리고 난 뒤 위치
                return mFixX;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mFixX;
    }

    // Get Move Y Point
    private float getMoveY(int y) {
        try {
            if (y < mMoveBgHeight) {
                // 최초 위치
                return mMoveY;
            } else if (y >= mMoveBgHeight && y <= mExtraHeight) {
                // Moving;
                return (float) mMoveY - (mDiffY * (y - mMoveBgHeight));
            } else {
                // 스크롤 내리고 난 뒤 위치
                return mFixY;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mFixY;
    }
}
