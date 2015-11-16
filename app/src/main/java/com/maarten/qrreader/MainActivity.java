package com.maarten.qrreader;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {

    private CoordinatorLayout snackbarCoordinatorLayout;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);

        setContentView(R.layout.activity_main);
        snackbarCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.snackbarCoordinatorLayout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
            integrator.addExtra("SCAN_WIDTH", 480);
            integrator.addExtra("SCAN_HEIGHT", 480);
            integrator.addExtra("SCAN_MODE", "QR_CODE_MODE,PRODUCT_MODE");
            integrator.addExtra("PROMPT_MESSAGE", "Place a QR Code inside the viewfinder rectangle to scan it.");
            integrator.initiateScan();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (result != null) {
            String contents = result.getContents();
            if (contents != null) {
                if (URLUtil.isHttpUrl(contents) || URLUtil.isHttpsUrl(contents)){
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(contents));
                    startActivity(browserIntent);
                } else {
                    Snackbar.make(snackbarCoordinatorLayout, contents, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }

            } else {
                Snackbar.make(snackbarCoordinatorLayout, "Failed to scan QR code", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
            Log.d("QR Reader", result.toString());
        }
    }

}
