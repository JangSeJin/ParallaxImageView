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

    private final int MAX_ALPHA = 225;

    private RecyclerView mRecyclerView;
    private ViewAdapter mAdapter;
    private View mHeader;
    private View mFix;
    private View mMove;

    private ArrayList<Model> mList;

    int mHeight;
    float mFixX;
    float mFixY;
    float mMoveX;
    float mMoveY;
    float mDiffX;
    float mDiffY;
    float mTemp;

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
        mHeight = getResources().getDimensionPixelOffset(R.dimen.header) * 5; // 스크롤 Y 값과 header 높이 값이 맞지 않음

        ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                Log.e("sjjang", "height : " + mHeight);

                mFixX = (float) mFix.getX();
                mFixY = (float) mFix.getY();
                Log.e("sjjang", "fixX : " + mFixX);
                Log.e("sjjang", "fixY : " + mFixY);

                mMoveX = (float) mMove.getX();
                mMoveY = (float) mMove.getY();
                Log.e("sjjang", "moveX : " + mMoveX);
                Log.e("sjjang", "moveY : " + mMoveY);

                mDiffX = (float) Math.abs(mFixX - mMoveX) / mHeight;
                mDiffY = (float) Math.abs(mFixY - mMoveY) / mHeight;
                Log.e("sjjang", "diffX : " + mDiffX);
                Log.e("sjjang", "diffY : " + mDiffY);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    mRecyclerView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
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

                Log.e("sjjang", "y : " + recyclerView.computeVerticalScrollOffset());
                int y = recyclerView.computeVerticalScrollOffset();

                // header alpha
                mHeader.setAlpha(getAlpha(y));

//                07-11 09:17:30.152 7202-7202/com.hour24.parallaximageview E/sjjang: height : 1000
//                07-11 09:17:30.152 7202-7202/com.hour24.parallaximageview E/sjjang: fixX : 1280
//                07-11 09:17:30.152 7202-7202/com.hour24.parallaximageview E/sjjang: fixY : 40
//                07-11 09:17:30.152 7202-7202/com.hour24.parallaximageview E/sjjang: moveX : 1120
//                07-11 09:17:30.152 7202-7202/com.hour24.parallaximageview E/sjjang: moveY : 320
//                07-11 09:17:30.152 7202-7202/com.hour24.parallaximageview E/sjjang: diffX : 6.25
//                07-11 09:17:30.152 7202-7202/com.hour24.parallaximageview E/sjjang: diffY : 280.0

//                if (y < 1) {
//                    // 최초 위치
//                    mMove.setY(mMoveY);
//                } else if (y >= 1 && y <= mHeight) {
//                    // Moving;
//                    mMove.setY(mMoveY - (mDiffY * y));
//                } else {
//                    // 스크롤 내리고 난 뒤 위치
//                    mMove.setY(mFixY);
//                }
                mMove.setX(getMoveX(y));
                mMove.setY(getMoveY(y));
            }
        });
    }

    // Get Alpha
    private float getAlpha(int y) {
        try {
            // 헤더 높이 만큼 Alpha 값 구함
            if (y > 0 && y <= mHeight) {
                return (float) y / mHeight;
            }

            // y 값 한번 더 체크
            if (y < 1) {
                return (float) 0.0;
            } else if (y >= mHeight) {
                return (float) 1.0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return (float) 1.0;
        }
    }

    // Get Move X Point
    private float getMoveX(int y) {
        try {
            if (y < 1) {
                // 최초 위치
                return mMoveX;
            } else if (y >= 1 && y <= mHeight) {
                // Moving;
                return (float) mMoveX + (mDiffX * y);
            } else {
                // 스크롤 내리고 난 뒤 위치
                return mFixX;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return mFixX;
        }
    }

    // Get Move Y Point
    private float getMoveY(int y) {
        try {
            if (y < 1) {
                // 최초 위치
                return mMoveY;
            } else if (y >= 1 && y <= mHeight) {
                // Moving;
                return (float) mMoveY - (mDiffY * y);
            } else {
                // 스크롤 내리고 난 뒤 위치
                return mFixY;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return mFixY;
        }
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
