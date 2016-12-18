package com.homepass.melbike;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.view.inputmethod.InputMethodManager;

import com.google.android.gms.maps.model.Marker;

/**
 * Created by Tony Liu on 2016/12/7.
 */

public class BaseActivity extends FragmentActivity {
    // Navigate to the selected Marker
    protected void navigateTo(Marker inmarker){
        //Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse( "http://maps.google.com/maps?saddr=51.5, 0.125&daddr=51.5, 0.15"));
        Intent intentD = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse( "http://maps.google.com/maps?q=loc:"+inmarker.getPosition().latitude+","+inmarker.getPosition().longitude));
        startActivity(intentD);
    }

    // hideKeyBoard
    protected void hideKeyBoard(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }
}
