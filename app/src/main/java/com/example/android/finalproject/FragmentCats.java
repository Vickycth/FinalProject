package com.example.android.finalproject;

import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentCats extends Fragment {
    private View mView;
    private ImageView catImageView;
    private ImageView loadingImageView;
    private String catImageId;  // save the id of the last displayed cat image.. .

    public FragmentCats() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (mView == null) {
            mView = inflater.inflate(R.layout.fg_cats, container, false);
        }
        catImageView = (ImageView) mView.findViewById(R.id.imageCat);
        loadingImageView = (ImageView) mView.findViewById(R.id.imageLoading);
        catImageView.setImageResource(R.drawable.cat1);
        catImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

        final MyHandler mHandler = new MyHandler(this);

        Button btnLike = (Button) mView.findViewById(R.id.btnLike);
        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // before load next image, display loading image.
                showLoading();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // Favourite the current image.
                        WebApi.postCatFavourite(catImageId);
                        // show next image (same as nope button)
                        Cat cat = WebApi.getCat();
                        CatInfo catInfo = null;
                        if (cat != null) {
                            Bitmap bmp = WebApi.getURLImage(cat.getUrl());
                            catInfo = new CatInfo(cat.id, cat.url, bmp);
                        }
                        Message msg = new Message();
                        msg.what = 0;
                        msg.obj = catInfo;
                        mHandler.sendMessage(msg);
                    }
                }).start();
            }
        });

        Button btnNope = (Button) mView.findViewById(R.id.btnNope);
        btnNope.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // before load next image, display loading image.
                showLoading();
                // create thread to call web api loading image.
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Cat cat = WebApi.getCat();
                        CatInfo catInfo = null;
                        if (cat != null) {
                            Bitmap bmp = WebApi.getURLImage(cat.getUrl());
                            catInfo = new CatInfo(cat.id, cat.url, bmp);
                        }
                        Message msg = Message.obtain();
                        msg.what = 0;
                        msg.obj = catInfo;
                        mHandler.sendMessage(msg);
                    }
                }).start();
            }
        });
        return mView;
    }

    private void showLoading() {
        AnimationDrawable animation = (AnimationDrawable)loadingImageView.getBackground();
        if (animation == null) {
            loadingImageView.setBackgroundResource(R.drawable.cat_loading_list);
            animation = (AnimationDrawable)loadingImageView.getBackground();
            animation.setOneShot(false);
        }
        animation.start();
        loadingImageView.setVisibility(View.VISIBLE);
        catImageView.setVisibility(View.INVISIBLE);
    }

    private void hideLoading() {
        AnimationDrawable animation = (AnimationDrawable)loadingImageView.getBackground();
        if (animation != null && animation.isRunning()) {
            animation.stop();
        }
        catImageView.setVisibility(View.VISIBLE);
        loadingImageView.setVisibility(View.GONE);
    }

    private void showImage(CatInfo catInfo) {
        if (catInfo != null) {
            catImageId = catInfo.imageId;
            Bitmap bmp = catInfo.bmp;
            if (bmp != null) {
                catImageView.setImageBitmap(bmp);
            }
        }
        hideLoading();
    }

    private class CatInfo {
        String imageId;
        String imageUrl;
        Bitmap bmp;

        public CatInfo(String imageId, String imageUrl, Bitmap bmp) {
            this.imageId = imageId;
            this.imageUrl = imageUrl;
            this.bmp = bmp;
        }
    }

    /**
     * Inner class for handling message.
     */
    private static class MyHandler extends Handler {
        // weak reference member
        private final WeakReference<FragmentCats> fragment_cats_;

        private MyHandler(FragmentCats fragment) {
            fragment_cats_ = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            FragmentCats fragmentCats = fragment_cats_.get();
            if (fragmentCats != null) {
                switch (msg.what) {
                    case 0:
                        CatInfo catInfo=(CatInfo) msg.obj;
                        fragmentCats.showImage(catInfo);
                        break;
                }
            }
        }
    }
}
