package com.hour24.parallaximageview;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


/**
 * Created by N16326 on 2018. 7. 9..
 */

public class Utils {

    @BindingAdapter({"loadImage"})
    public static void setLoadImage(final ParallaxImageView view, String url) {
        try {

            final ImageView imageView = new ImageView(view.getContext());
            Picasso.get()
                    .load(url)
                    .into(imageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            view.setImageDrawable(imageView.getDrawable());
                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
