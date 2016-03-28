package com.example.ishantoberoi.otpreader;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URI;

/**
 * Created by ishantoberoi on 24/02/16.
 */
public class LandingPage extends AppCompatActivity implements View.OnClickListener, IConstants {

    private TextView txtTakeTour;
    private ImageButton imgbtnLinkedIn;
    private ImageButton imgbtnFacebook;
    private Button btnmanagePermissions;
    private Button btnOverlayPermissions;
    private LinearLayout llPermisions;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing_page);
        txtTakeTour = (TextView) findViewById(R.id.txtTakeTour);
        imgbtnLinkedIn = (ImageButton) findViewById(R.id.imgbtnLinkedIn);
        imgbtnFacebook = (ImageButton) findViewById(R.id.imgbtnFacebook);
        btnmanagePermissions = (Button) findViewById(R.id.btnManagePermissions);
        llPermisions = (LinearLayout) findViewById(R.id.llPermissions);
        btnOverlayPermissions = (Button) findViewById(R.id.btnOverlayPermissions);
        txtTakeTour.setOnClickListener(this);
        imgbtnFacebook.setOnClickListener(this);
        imgbtnLinkedIn.setOnClickListener(this);
        btnmanagePermissions.setOnClickListener(this);
        btnOverlayPermissions.setOnClickListener(this);
        if(Build.VERSION.SDK_INT >= 23) {
            llPermisions.setVisibility(View.VISIBLE);
        } else {
            llPermisions.setVisibility(View.GONE);
        }
        checkForSmsPermissions();
    }


    private void checkForSmsPermissions() {
        int bootCompletedPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_BOOT_COMPLETED);
        if(bootCompletedPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_BOOT_COMPLETED}, BOOT_COMPLETED_PERMISSIONS);
        }
        int smsPermissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);
        if(smsPermissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS},
                    SMS_PERMISSIONS);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case BOOT_COMPLETED_PERMISSIONS:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(LandingPage.this, "You may need to restart the app once manually on phone reboot", Toast.LENGTH_LONG).show();
                }
            case SMS_PERMISSIONS:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(Build.VERSION.SDK_INT >= 23) {
                        if(! Settings.canDrawOverlays(LandingPage.this)) {
                            startActivityForOverlayPermission();
                        }
                    }
                } else {
                    Toast.makeText(LandingPage.this, "Please enable the permission for the app, else we will not be able to serve you", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtTakeTour:
                startActivity(new Intent(LandingPage.this, MainActivity.class));
                break;
            case R.id.imgbtnLinkedIn:
                startFbLinkedInOpenActivity(IConstants.LINKEDIN_URI);
                break;
            case R.id.imgbtnFacebook:
                startFbLinkedInOpenActivity(IConstants.FACEBOOK_URI);
                break;
            case R.id.btnManagePermissions:
                startInstalledAppDetailsActivity(LandingPage.this);
                break;
            case R.id.btnOverlayPermissions:
                startActivityForOverlayPermission();
                break;

        }
    }

    public void startFbLinkedInOpenActivity(String uri) {
        Intent openActivity = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(openActivity);
    }

    public static void startInstalledAppDetailsActivity(final Context context) {
        if (context == null) {
            return;
        }
        final Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + context.getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(i);
    }

    public void startActivityForOverlayPermission() {
        startActivity(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:com.example.ishantoberoi.otpreader")));
    }

}
