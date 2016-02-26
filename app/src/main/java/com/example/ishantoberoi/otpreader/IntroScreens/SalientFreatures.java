package com.example.ishantoberoi.otpreader.IntroScreens;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ishantoberoi.otpreader.R;

/**
 * Created by ishantoberoi on 24/02/16.
 */
public class SalientFreatures extends Fragment {
    private TextView txtHeader;
    private TextView txtContent;
    private ImageView imgImageHolder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.intro_screen_layout, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtHeader = (TextView) view.findViewById(R.id.txtHeader);
        txtContent = (TextView) view.findViewById(R.id.txtContent);
        imgImageHolder = (ImageView) view.findViewById(R.id.imgImageHolder);

        txtHeader.setText(R.string.headerTextSalientfeatures);
        txtContent.setText(R.string.contentSalientFeatures);
        imgImageHolder.setImageResource(R.drawable.maximize);
    }
}
