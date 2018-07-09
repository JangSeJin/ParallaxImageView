package com.hour24.parallaximageview;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hour24.parallaximageview.R;

import java.util.ArrayList;

/**
 * Created by N16326 on 2018. 7. 4..
 */

public class ViewAdapter extends RecyclerView.Adapter<ViewAdapter.ViewHolder> {

    private ArrayList<Model> mList;
    private Activity mActivity;

    public ViewAdapter(Activity activity, ArrayList<Model> list) {
        this.mActivity = activity;
        this.mList = list;
    }

    @Override
    public int getItemViewType(int position) {
        Model menu = mList.get(position);
        return menu.getStyle();
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        int layoutId = R.layout.main_item_image;

//        try {
//            switch (viewType) {
//                case MyPageConst.Style.MEMBER:
//                    layoutId = R.layout.mypage_member;
//                    break;
//                case MyPageConst.Style.GROUP:
//                    layoutId = R.layout.mypage_group_title;
//                    break;
//                case MyPageConst.Style.ITEM:
//                    layoutId = R.layout.mypage_menu;
//                    break;
//                case MyPageConst.Style.LAST_PADDING:
//                    layoutId = R.layout.mypage_last_padding;
//                    break;
//
//                default:
//                    layoutId = R.layout.list_item_empty;
//                    break;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        view = DataBindingUtil.inflate(LayoutInflater.from(mActivity), layoutId, parent, false).getRoot();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {

            Model item = mList.get(position);

            holder.getBinding().setVariable(BR.model, item);
            holder.getBinding().executePendingBindings();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ViewDataBinding binding;
        View view;

        public ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            this.view = itemView;
        }

        public ViewDataBinding getBinding() {
            return binding;
        }
    }
}
