package com.coolweather.android.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.coolweather.android.MainActivity;

/**
 * Created by Administrator on 2017/9/10.
 */

public class LBS {

    private LocationCallBack callBack;
    private static LBS lbs;

    private LocationClient locationClient;
    private BDLocationListener localListener = new MyBDLocationListener();

    private String city;

    private LBS(Context context){
        //第一步实例化定位核心类
        locationClient = new LocationClient(context, getLocOption());
        //第二布设置位置变化回调监听
        locationClient.registerLocationListener(localListener);
        Log.d("City", "LBS: " );
    }

    public void start() {
//        第三步开始定位
        locationClient.start();
    }

    public static LBS getInstance(Context context) {
        Log.d("LBS", "getInstance:  LBS " + lbs);
        lbs = null;
        if (lbs == null) {
            lbs = new LBS(context);
            Log.d("LBS", "getInstance: new LBS " + lbs);
        }
        return lbs;
    }

    private LocationClientOption getLocOption(){
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);
        return option;
    }

    public String getCity(LocationClient locationClient) {
        return city;
    }

    //一般会在Activity的OnDestroy方法调用

    public void stop() {
        if (locationClient !=null) {
            locationClient.unRegisterLocationListener(localListener);
            locationClient.stop();
            locationClient = null;
        }
    }

    /*private class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            city = bdLocation.getCity();
            Log.d("City", "onReceiveLocation: city " + city);
            if (bdLocation.getLocType() == BDLocation.TypeGpsLocation) {
                Log.d("City", "定位方式：GPS");
            } else if (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
                Log.d("City", "定位方式：网络");
            }
        }
    }*/

    private class MyBDLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (callBack != null && bdLocation!= null){
                Log.d("City", "onReceiveLocation: " + bdLocation.getCity());
                if (bdLocation.getLocType() == BDLocation.TypeGpsLocation) {
                    Log.d("City", "定位方式：GPS");
                } else if (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
                    Log.d("City", "定位方式：网络");
                }
                callBack.callBack(bdLocation.getCity());
            }
            //多次定位必须要调用stop方法
            locationClient.stop();
        }
    }

    //回调接口
    public interface LocationCallBack{
        void callBack(String city);
    }

    public LocationCallBack getCallBack() {
        return callBack;
    }

    public void setCallBack(LocationCallBack callBack) {
        this.callBack = callBack;
    }
}

