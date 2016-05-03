package com.jianhui_zhu.openuseragent.util;

import android.net.TrafficStats;

/**
 * Created by jianhuizhu on 2016-05-02.
 */
public final class TrafficStatisticUtil {
    private static TrafficStatisticUtil INSTANCE;
    private static final int MY_UID = android.os.Process.myUid();
    private static long preTotalByteConsume = TrafficStats.getUidRxBytes(MY_UID)+TrafficStats.getUidTxBytes(MY_UID);
    private TrafficStatisticUtil(){}
    public static synchronized TrafficStatisticUtil getInstance(){
        if(INSTANCE==null){
            instantiate();
        }
        return INSTANCE;
    }
    public static void instantiate(){
        if(INSTANCE==null){
           INSTANCE = new TrafficStatisticUtil();
        }
    }

    public synchronized long getCurTotalBytesConsume(){
        long cur = TrafficStats.getUidRxBytes(MY_UID) + TrafficStats.getUidTxBytes(MY_UID);
        long result = cur - preTotalByteConsume;
        preTotalByteConsume = cur;
        return result;
    }
}
