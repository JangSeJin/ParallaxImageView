package com.hour24.parallaximageview;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final int EXTRA_HEIGHT = 6;

    private RecyclerView mRecyclerView;
    private ViewAdapter mAdapter;
    private View mHeader;
    private ImageView mLogo;
    private View mFix;
    private View mMove;

    private ArrayList<Model> mList;

    int mHeight; // 원래 헤더 높이
    int mExtraHeight; // 추가 헤더 높이
    int mLogoHeight;
    float mFixX;
    float mFixY;
    float mMoveX;
    float mMoveY;
    float mHeaderY;
    float mDiffHeader;
    float mDiffX;
    float mDiffY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mHeader = findViewById(R.id.header);
        mLogo = (ImageView) findViewById(R.id.logo);
        mFix = findViewById(R.id.fix);
        mMove = findViewById(R.id.move);

        mAdapter = new ViewAdapter(this, getList());
        mRecyclerView.setAdapter(mAdapter);

        mHeight = getResources().getDimensionPixelOffset(R.dimen.header);
        mExtraHeight = mHeight * EXTRA_HEIGHT; // 스크롤 Y 값과 header 높이 값이 맞지 않음

        // 0 > 투명, 1 > 불투명
        mHeader.setAlpha((float) 0.0);
        mLogo.setAlpha((float) 0.0);
        mHeader.setY(-mHeight); // 최초 헤더를 숨김

        ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                try {
                    Log.e("sjjang", "height : " + mHeight);

                    // 스크롤 후 이동할 View
                    mFixX = (float) mFix.getX();
                    mFixY = (float) mFix.getY();
                    Log.e("sjjang", "fixX : " + mFixX);
                    Log.e("sjjang", "fixY : " + mFixY);

                    // 스크롤에 따라 이동할 View
                    mMoveX = (float) mMove.getX();
                    mMoveY = (float) mMove.getY();
                    Log.e("sjjang", "moveX : " + mMoveX);
                    Log.e("sjjang", "moveY : " + mMoveY);

                    // 스크롤에 따라 이동할 값 (수치)
                    mDiffX = (float) Math.abs(mFixX - mMoveX) / mExtraHeight;
                    mDiffY = (float) Math.abs(mFixY - mMoveY) / mExtraHeight;
                    Log.e("sjjang", "diffX : " + mDiffX);
                    Log.e("sjjang", "diffY : " + mDiffY);

                    // 헤더 Y Point
                    mHeaderY = (float) mHeader.getY();
                    // 스크롤에 따라 이동할 헤더 값
                    mDiffHeader = (float) mHeight / mExtraHeight;
                    Log.e("sjjang", "mDiffHeader : " + mDiffHeader);

                    // Logo
                    mLogoHeight = mLogo.getHeight();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        mRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    } else {
                        mRecyclerView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        mRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(globalLayoutListener);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                int y = recyclerView.computeVerticalScrollOffset();
                Log.e("sjjang", "y : " + y);

                // Set Header
                mHeader.setY(getHeaderY(y));
                mHeader.setAlpha(getHeaderAlpha(y));

                // Set Logo
                mLogo.setAlpha(getLogoAlpha(y));

                // Set Move X, Y
                mMove.setX(getMoveX(y));
                mMove.setY(getMoveY(y));

            }
        });
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

    // Get Move X Point
    private float getMoveX(int y) {
        try {
            if (y < 1) {
                // 최초 위치
                return mMoveX;
            } else if (y >= 1 && y <= mExtraHeight) {
                // Moving;
                return (float) mMoveX + (mDiffX * y);
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
            if (y < 1) {
                // 최초 위치
                return mMoveY;
            } else if (y >= 1 && y <= mExtraHeight) {
                // Moving;
                return (float) mMoveY - (mDiffY * y);
            } else {
                // 스크롤 내리고 난 뒤 위치
                return mFixY;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mFixY;
    }

    public ArrayList<Model> getList() {

        mList = new ArrayList<>();

        mList.add((new Model(0, "http://tourimage.interpark.com/product/tour/00161/A30/500/A3014824_1_993.jpg")));
        mList.add((new Model(0, "http://tourimage.interpark.com/product/tour/00161/A30/500/A3014824_6_987.jpg")));
        mList.add((new Model(0, "http://tourimage.interpark.com/product/tour/00161/A30/500/A3014824_7_533.jpg")));
        mList.add((new Model(0, "http://tourimage.interpark.com/product/tour/00161/A30/500/A3014824_1_993.jpg")));
        mList.add((new Model(0, "http://tourimage.interpark.com/product/tour/00161/A30/500/A3014824_6_987.jpg")));
        mList.add((new Model(0, "http://tourimage.interpark.com/product/tour/00161/A30/500/A3014824_7_533.jpg")));
        mList.add((new Model(0, "http://tourimage.interpark.com/product/tour/00161/A30/500/A3014824_1_993.jpg")));
        mList.add((new Model(0, "http://tourimage.interpark.com/product/tour/00161/A30/500/A3014824_6_987.jpg")));
        mList.add((new Model(0, "http://tourimage.interpark.com/product/tour/00161/A30/500/A3014824_7_533.jpg")));
        mList.add((new Model(0, "http://tourimage.interpark.com/product/tour/00161/A30/500/A3014824_1_993.jpg")));
        mList.add((new Model(0, "http://tourimage.interpark.com/product/tour/00161/A30/500/A3014824_6_987.jpg")));
        mList.add((new Model(0, "http://tourimage.interpark.com/product/tour/00161/A30/500/A3014824_7_533.jpg")));

        return mList;
    }
}
