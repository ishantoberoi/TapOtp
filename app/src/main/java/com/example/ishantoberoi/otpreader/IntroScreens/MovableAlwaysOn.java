package com.example.ishantoberoi.otpreader.IntroScreens;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ishantoberoi.otpreader.LandingPage;
import com.example.ishantoberoi.otpreader.R;

/**
 * Created by ishantoberoi on 24/02/16.
 */
public class MovableAlwaysOn extends Fragment implements View.OnClickListener {
    private TextView txtHeader;
    private TextView txtContent;
    private ImageView imgImageHolder;
    private TextView txtSwipeForNext;
    private Button btnDoneIntro;

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
        txtSwipeForNext = (TextView) view.findViewById(R.id.txtSwipeForNext);
        btnDoneIntro = (Button) view.findViewById(R.id.btnDoneIntro);
        btnDoneIntro.setVisibility(View.VISIBLE);
        btnDoneIntro.setOnClickListener(this);
        txtHeader.setText(R.string.headerTextMovableAlwaysOn);
        txtContent.setText(R.string.contentMovableAlwaysOn);
        imgImageHolder.setImageResource(R.drawable.maximize);
        txtSwipeForNext.setText(R.string.swipeForNextMovable);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnDoneIntro:
                startActivity(new Intent(getActivity(), LandingPage.class));
                getActivity().finish();
                break;
        }
    }
}
