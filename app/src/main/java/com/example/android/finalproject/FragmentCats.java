package com.example.android.finalproject;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentCats extends Fragment {
    private View mView;

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

        ImageView image = (ImageView) mView.findViewById(R.id.imageCat);
        image.setImageResource(R.drawable.cat1);

        Button btnLike = (Button) mView.findViewById(R.id.btnLike);
        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView image = (ImageView) mView.findViewById(R.id.imageCat);
                image.setImageResource(R.drawable.cat);
            }
        });

        Button btnNope = (Button) mView.findViewById(R.id.btnNope);
        image.setImageResource(R.drawable.cat);
        btnNope.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView image = (ImageView) mView.findViewById(R.id.imageCat);
                image.setImageResource(R.drawable.cat1);
            }
        });

        return mView;
    }
}
