package com.example.android.finalproject;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentLike extends Fragment {
    private View mView;
    private TextView txtView;

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
        txtView = (TextView)mView.findViewById(R.id.txtInfo);

        final FragmentLike.MyHandler mHandler = new FragmentLike.MyHandler(this);

        Button btn = (Button) mView.findViewById(R.id.btnTest);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        List<FavCat> favList = WebApi.getFavouriteCatList();
                        Message msg = Message.obtain();
                        msg.what = 0;
                        msg.obj = favList;
                        mHandler.sendMessage(msg);
                    }
                }).start();
            }
        });

        return mView;
    }

    private void showFav(List<FavCat> favList) {
        if (favList != null) {
            int num = favList.size();
            StringBuilder sb = new StringBuilder();
            for (int i=0; i<num; i++) {
                FavCat favCat = favList.get(i);
                sb.append("favId=");
                sb.append(favCat.favId);
                sb.append("/");
                sb.append("id=");
                sb.append(favCat.id);
                sb.append("\n");
                sb.append("url=");
                sb.append(favCat.url);
                sb.append("\n");
            }
            txtView.setText(sb.toString());
        }
    }

    private static class MyHandler extends Handler {
        // weak reference member
        private final WeakReference<FragmentLike> fragment_like_;

        private MyHandler(FragmentLike fragment) {
            fragment_like_ = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            FragmentLike fragmentLike = fragment_like_.get();
            if (fragmentLike != null) {
                switch (msg.what) {
                    case 0:
                        List<FavCat> favList = (List<FavCat>)msg.obj;
                        fragmentLike.showFav(favList);
                        break;
                }
            }
        }
    }

}
