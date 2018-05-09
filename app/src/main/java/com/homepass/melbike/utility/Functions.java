package com.homepass.melbike.utility;


/**
 * Created by Tony Liu on 2016/12/18.
 */

public class Functions {
    /**
     * Request bikesite from server
     * @return
     */
    public static boolean retrieveBikeSites(){
        try{

            RestfulRequest restfulRequest = new RestfulRequest();
            restfulRequest.execute();
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
    }
}
