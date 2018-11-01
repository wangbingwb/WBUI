package xyz.wbsite.wbsiteui.base.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Criteria;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * GPS定位服务类，封装了GPS类的细节，只向用户暴露简单的start()和stop()两个方法。需要用户实现{@link IGPSCallback}接口中的方法
 * <p>
 * 使用方法： <br>
 * GPSUtil GPSUtil = new GPSUtil(gpsCallback, MainActivity.this, true);
 * <br>
 * GPSUtil.start();
 *
 * @author 优化设计，QQ:50199907 Email:yw1530@126.com
 * @see IGPSCallback
 */
public class GPSUtil {
    private String TAG = GPSUtil.class.getSimpleName();
    /**
     * GPS函数回调接口
     */
    private IGPSCallback gpsCallback;

    private LocationManager loctionManager;

    private String provider;

    /**
     * GPSUtil是否运行
     */
    private boolean isRun;

    /**
     * @return GPS状态，true为正在运行，false已停止。
     */
    public boolean getIsRun() {
        return isRun;
    }

    /**
     * 自动停止
     */
    private boolean isAutoStop = false;

    /**
     * 是否调试
     */
    private boolean isTest = false;

    /**
     * 超时时间（秒）
     */
    private int outTime = 120;

    /**
     * 精度
     */
    private final float accuracy = 100.0f;

    /**
     * GPS配置参数
     */
    private Criteria criteria;

    /**
     * @return 获取criteria
     */
    public Criteria getCriteria() {
        initCriteria();
        return criteria;
    }

    /**
     * 初始化GPS参数
     */
    private void initCriteria() {
        if (criteria == null) {
            criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE); // 高精度
            criteria.setAltitudeRequired(false); // 不要求海拔
            criteria.setBearingRequired(false); // 不要求方位
            criteria.setCostAllowed(true); // 允许有花费
            criteria.setPowerRequirement(Criteria.POWER_LOW);// 设置低功耗模式
        }
    }

    /**
     * 最后一次错误描述
     */
    private String lastErrorDescription = "";

    /**
     * @return 获取GPSSerivce最后一次出错的描述
     */
    public String getError() {
        return lastErrorDescription;
    }

    /**
     * 设置最后一次错误描述，该描述可以通过getError()方法获取。
     *
     * @param error 错误说明
     * @see GPSUtil#getError()
     */
    private void setError(String error) {
        if (error == null)
            return;
        this.lastErrorDescription = error;
    }

    /**
     * GPSUtil构造函数
     *
     * @param gpsCallback 回调函数接口
     * @param context     Context
     */
    public GPSUtil(IGPSCallback gpsCallback, Context context) {
        this(gpsCallback, context, true);
    }

    public GPSUtil(IGPSCallback gpsCallback, Context context, boolean isAutoStop) {

        this(gpsCallback, context, isAutoStop, false);
    }

    public GPSUtil(IGPSCallback gpsCallback, Context context, boolean isAutoStop, boolean isTest) {
        this.gpsCallback = gpsCallback;
        this.isAutoStop = isAutoStop;
        this.isTest = isTest;
        loctionManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        initCriteria();

        // 从可用的位置提供器中，匹配以上标准的最佳提供器
        provider = loctionManager.getBestProvider(criteria, true);
    }

    /**
     * 返回超时时间（单位：秒）
     *
     * @return
     */
    public int getOutTime() {
        return outTime;
    }

    /**
     * 设置超时时间
     *
     * @param outTime 超时时间（单位：秒，范围：10—600），只可在Start()方法调用前使用，默认180秒，如果小于10秒则超时无效，
     *                只能手动调用Stop() 方法停止GPS。
     */
    public void setOutTime(int outTime) {
        if (outTime >= 0 && outTime <= 600) {
            this.outTime = outTime;
        }
    }

    /**
     * 开始GPS定位
     *
     * @return 返回false表示GPS打开失败，可能没有硬件打开（由手机用户控制硬件开关）。
     */
    @SuppressLint("MissingPermission")
    public boolean start() {
        try {
            if (!loctionManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
                // GPS没有打开
                setError("GPS未打开");
                gpsCallback.failCallBack(IGPSCallback.ERROR_CLOSE);
                return false;
            }

            // 注册监听函数
            if (locationListener != null) {
                loctionManager.requestLocationUpdates(provider, 1000 * 10, accuracy, locationListener);
            }

            // 调试
            if (isTest) {
                loctionManager.addGpsStatusListener(new GpsStatus.Listener() {
                    private List<GpsSatellite> numSatelliteList = new ArrayList();

                    @Override
                    public void onGpsStatusChanged(int event) {
                        GpsStatus status = loctionManager.getGpsStatus(null); //取当前状态

                        StringBuilder sb = new StringBuilder("");
                        if (status == null) {
                            sb.append("搜索到卫星个数：" + 0);
                        } else if (event == GpsStatus.GPS_EVENT_SATELLITE_STATUS) {
                            int maxSatellites = status.getMaxSatellites();
                            Iterator<GpsSatellite> it = status.getSatellites().iterator();
                            numSatelliteList.clear();
                            int count = 0;
                            while (it.hasNext() && count <= maxSatellites) {
                                GpsSatellite s = it.next();
                                numSatelliteList.add(s);
                                count++;
                            }
                            sb.append("搜索到卫星个数：" + numSatelliteList.size());
                        }
                        Log.d(TAG, sb.toString());
                    }
                });
            }

            isRun = true;
            return true;
        } catch (Exception e) {
            setError(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 停止GPS定位
     *
     * @return
     */
    public boolean stop() {
        try {
            if (locationListener != null) {
                loctionManager.removeUpdates(locationListener);
            }
            isRun = false;
            return true;
        } catch (Exception e) {
            setError(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 位置监听器
     */
    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        // 当位置变化时触发
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                if (location.hasAccuracy() && location.getAccuracy() <= accuracy) {
                    // 是否自动停止
                    if (isAutoStop) {
                        stop();
                    }
                    gpsCallback.gpsCallback(location);
                }
            }
        }
    };

    /**
     * GPS定位服务回调函数接口，需要实现{@link IGPSCallback#gpsCallback(Location location)}和
     * {@link IGPSCallback#failCallBack(String error)}方法
     *
     * @author 优化设计，QQ:50199907 Email:yw1530@126.com
     * @see GPSUtil
     */
    public interface IGPSCallback {
        /**
         * GPS无信号
         */
        public final static String ERROR_NO_SIGNAL = "GPS无信号";

        /**
         * GPS超时退出
         */
        public final static String ERROR_OUTTIME = "GPS超时退出";

        /**
         * GPS硬件没有打开
         */
        public final static String ERROR_CLOSE = "GPS硬件关闭";

        /**
         * GPS执行成功，返回Location时的回调函数
         *
         * @param location 位置信息
         */
        void gpsCallback(Location location);

        /**
         * GPS错误时的回调函数 包括GPS无信号、GPS超时退出、GPS硬件没有打开
         *
         * @param error 错误描述，一般为{@link IGPSCallback#ERROR_NO_SIGNAL}、
         *              {@link IGPSCallback#ERROR_OUTTIME}、
         *              {@link IGPSCallback#ERROR_CLOSE}。也可以由GPSUtil类自定义
         */
        void failCallBack(String error);
    }

}
