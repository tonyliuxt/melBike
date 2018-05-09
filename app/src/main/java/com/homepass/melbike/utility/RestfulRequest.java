package com.homepass.melbike.utility;

import android.os.AsyncTask;
import android.util.Log;

import com.homepass.melbike.Constants;
import com.homepass.melbike.entitiy.BikeSite;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Xintian on 2016/12/14.
 */

public class RestfulRequest extends AsyncTask<Void, Void, List<BikeSite>> {
    private static final String tag  = "RestfulRequest";
    @Override
    protected List<BikeSite> doInBackground(Void... params){
        try{
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            ResponseEntity<BikeSite[]> response = restTemplate.getForEntity(Constants.API_URL, BikeSite[].class);
            if(response != null){
                return Arrays.asList(response.getBody());
            }else {
                return null;
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }



    @Override
    protected void onPostExecute(List<BikeSite> bikelist){
        GlobalStatic.G_List_BikeSite = bikelist;
        if(GlobalStatic.G_List_BikeSite != null) Log.v(tag, "bike count returned:"+GlobalStatic.G_List_BikeSite.size());
        if(GlobalStatic.Main_Handler != null){
            GlobalStatic.Main_Handler.sendEmptyMessage(Constants.MSG_SITES_READY);
        }
    }
}
