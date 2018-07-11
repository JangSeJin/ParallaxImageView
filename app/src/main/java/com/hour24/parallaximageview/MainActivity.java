package com.hour24.parallaximageview;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final int EXTRA_HEIGHT = 6;

    private RecyclerView mRecyclerView;
    private ViewAdapter mAdapter;
    private View mHeader;
    private View mFix;
    private View mMove;

    private ArrayList<Model> mList;

    int mHeight; // 원래 헤더 높이
    int mExtraHeight; // 추가 헤더 높이
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
        mFix = findViewById(R.id.fix);
        mMove = findViewById(R.id.move);

        mAdapter = new ViewAdapter(this, getList());
        mRecyclerView.setAdapter(mAdapter);

        // 0 > 투명, 1 > 불투명
        mHeader.setAlpha((float) 0.0);

        mHeight = getResources().getDimensionPixelOffset(R.dimen.header);
        mHeader.setY(-mHeight); // 최초 헤더를 숨김

        ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                try {
                    Log.e("sjjang", "height : " + mHeight);

                    mExtraHeight = mHeight * EXTRA_HEIGHT; // 스크롤 Y 값과 header 높이 값이 맞지 않음

                    mFixX = (float) mFix.getX();
                    mFixY = (float) mFix.getY();
                    Log.e("sjjang", "fixX : " + mFixX);
                    Log.e("sjjang", "fixY : " + mFixY);

                    mMoveX = (float) mMove.getX();
                    mMoveY = (float) mMove.getY();
                    Log.e("sjjang", "moveX : " + mMoveX);
                    Log.e("sjjang", "moveY : " + mMoveY);

                    mDiffX = (float) Math.abs(mFixX - mMoveX) / mExtraHeight;
                    mDiffY = (float) Math.abs(mFixY - mMoveY) / mExtraHeight;
                    Log.e("sjjang", "diffX : " + mDiffX);
                    Log.e("sjjang", "diffY : " + mDiffY);

                    mHeaderY = (float) mHeader.getY();
                    mDiffHeader = (float) mHeight / mExtraHeight;
                    Log.e("sjjang", "mDiffHeader : " + mDiffHeader);

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

//                // Header
//                if (y < 1) {
//                    // 최초 위치
//                    mHeader.setY(-mHeight);
//                } else if (y >= 1 && y <= mExtraHeight) {
//                    mHeader.setY((float) mHeaderY + (mDiffHeader * y));
//                } else {
//                    // 스크롤 마지막 위치
//                    mHeader.setY(0);
//                }

                mHeader.setY(getHeaderY(y));
                mHeader.setAlpha(getHeaderAlpha(y)); // header alpha

                // Get Move X, Y
                mMove.setX(getMoveX(y));
                mMove.setY(getMoveY(y));
            }
        });
    }

    // Get Alpha
    private float getHeaderAlpha(int y) {
        try {
            if (y < 1) {
                // 최초 위치
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
