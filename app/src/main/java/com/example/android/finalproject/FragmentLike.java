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
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentLike extends Fragment {
    private View mView;
    private MyHandler mHandler;
    private ImageView catImageView;
    private ImageView loadingImageView;
    private TextView txtFavInfo;
    private Button btnNext;
    private List<FavCat> favList;   // save the list of favourites.
    private int favIndex = -1;     // save the index of favList.

    public FragmentLike() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (mView == null) {
            mView = inflater.inflate(R.layout.fg_like, container, false);
        }
        catImageView = (ImageView) mView.findViewById(R.id.imageCat);
        loadingImageView = (ImageView) mView.findViewById(R.id.imageLoading);
        txtFavInfo = (TextView) mView.findViewById(R.id.txtFavInfo);
        btnNext = (Button) mView.findViewById(R.id.btnNextFav);
        catImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        //catImageView.setImageResource(R.drawable.cat1);

        // Message handler.
        if (mHandler == null) {
            mHandler = new MyHandler(this);
        }

        Button btnPrev = (Button) mView.findViewById(R.id.btnPrevFav);
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // before load next image, display loading image.
                showLoading();
                // create thread to call web api loading image.
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        FavCat favCat = getPrevFavCat();
                        Bitmap bmp = null;
                        if (favCat != null) {
                            bmp = WebApi.getURLImage(favCat.getUrl());
                        }
                        Message msg = Message.obtain();
                        msg.what = 0;
                        msg.obj = bmp;
                        mHandler.sendMessage(msg);
                    }
                }).start();
            }
        });

        Button btnNext = (Button) mView.findViewById(R.id.btnNextFav);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // before load next image, display loading image.
                showLoading();
                // create thread to call web api loading image.
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        FavCat favCat = getNextFavCat();
                        Bitmap bmp = null;
                        if (favCat != null) {
                            bmp = WebApi.getURLImage(favCat.getUrl());
                        }
                        Message msg = Message.obtain();
                        msg.what = 0;
                        msg.obj = bmp;
                        mHandler.sendMessage(msg);
                    }
                }).start();
            }
        });

        Button btnUnFav = (Button) mView.findViewById(R.id.btnUnFav);
        btnUnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // UnFav button event don't update cat image, so do not display loading image.
                // showLoading();
                // create thread to call web api loading image.
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        FavCat favCat = getCurrentFavCat();
                        if (favCat != null) {
                            WebApi.deleteCatFavourite(favCat.getFavId());
                        }
                        // update favourite list.
                        List<FavCat> list = WebApi.getFavouriteCatList();
                        Message msg = Message.obtain();
                        msg.what = 1;
                        msg.obj = list;
                        msg.arg1 = -1;
                        mHandler.sendMessage(msg);
                    }
                }).start();
            }
        });
        return mView;
    }

    // do when visible state of current fragment changed
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            // Each time when fragment is visible, refresh favorite list.
            new Thread(new Runnable() {
                @Override
                public void run() {
                    List<FavCat> list = WebApi.getFavouriteCatList();
                    Message msg = Message.obtain();
                    msg.what = 1;
                    msg.obj = list;
                    msg.arg1 = 1;
                    mHandler.sendMessage(msg);
                }
            }).start();
        } else {
            // do nothing when this fragment switch to invisible
        }
    }

    // show count and current index of favourites in text view.
    private void showFavInfo() {
        if (favList == null || favList.size() == 0) {
            txtFavInfo.setText("no favourite.");
        } else {
            txtFavInfo.setText(String.format("Favourite: %1$s of %2$s", favIndex + 1, favList.size()));
        }
    }

    // params:
    //   onVisible == true when this page is switched visible
    //   onVisible == false when unFav button clicked
    private void setFavList(List<FavCat> list, boolean onVisible) {
        favList = list;
        if (onVisible) {
            // on visible, if there is no image displayed, display the 1st favourite cat image.
            if (favIndex == -1 && favList.size() > 0) {
                btnNext.performClick();
            }
        } else {
            // UnFav button clicked, auto display the next.(simulate to click btnNext)
            if (favIndex >= 0) {
                favIndex -= 1;
            }
            btnNext.performClick();
        }
        showFavInfo();
    }

    // get current FavCat from list.
    private FavCat getCurrentFavCat() {
        if (favList == null || favList.size() == 0) {
            return null;
        }
        int favListSize = favList.size();
        if (favIndex < 0) {
            return null;
        }
        else if (favIndex >= favListSize) {
            return null;
        } else {
            return favList.get(favIndex);
        }
    }

    // get prev FavCat from list.
    private FavCat getPrevFavCat() {
        if (favList == null || favList.size() == 0) {
            return null;
        }
        int favListSize = favList.size();
        int newIndex = favIndex - 1;
        if (newIndex < 0) {
            newIndex = 0;
        } else if (newIndex >= favListSize) {
            newIndex = favListSize - 1;
        }
        favIndex = newIndex;
        return favList.get(favIndex);
    }

    // get next FavCat from list.
    private FavCat getNextFavCat() {
        if (favList == null || favList.size() == 0) {
            return null;
        }
        int favListSize =favList.size();
        int newIndex = favIndex + 1;
        if (newIndex < 0) {
            newIndex = 0;
        }
        else if (newIndex >= favListSize) {
            newIndex = favListSize - 1;
        }
        favIndex = newIndex;
        return favList.get(favIndex);
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

    private void showImage(Bitmap bmp) {
        if (bmp != null) {
            catImageView.setImageBitmap(bmp);
        } else {
            catImageView.setImageDrawable(null);
        }
        showFavInfo();
        hideLoading();
    }

    /**
     * Inner class for handling message.
     */
    private static class MyHandler extends Handler {
        // weak reference member
        private final WeakReference<FragmentLike> fragment_like_;

        private MyHandler(FragmentLike fragment) {
            fragment_like_ = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            FragmentLike FragmentLike = fragment_like_.get();
            if (FragmentLike != null) {
                switch (msg.what) {
                    case 0:
                        Bitmap bmp=(Bitmap) msg.obj;
                        FragmentLike.showImage(bmp);
                        break;
                    case 1:
                        boolean onVisible = msg.arg1 == 1;
                        FragmentLike.setFavList((List<FavCat>)msg.obj, onVisible);
                        break;
                }
            }
        }
    }
}
