package com.homepass.melbike.utility;

import android.os.Handler;

import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.Marker;
import com.homepass.melbike.entitiy.BikeSite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Xintian on 2016/12/14.
 */

public class GlobalStatic {
    // BikeSite list
    public static List<BikeSite> G_List_BikeSite = new ArrayList<BikeSite>();
    // Circle list
    public static List<Circle> G_List_Circle = new ArrayList<Circle>();
    // Marker list Hash
    public static HashMap<String, Marker> G_Hash_Marker = new HashMap<String, Marker>();
    // Internal notification message handler
    public static Handler Main_Handler;
}
