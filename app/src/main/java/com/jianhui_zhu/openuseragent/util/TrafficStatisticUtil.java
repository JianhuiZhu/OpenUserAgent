package com.jianhui_zhu.openuseragent.util;

import android.net.TrafficStats;

/**
 * Created by jianhuizhu on 2016-05-02.
 */
public final class TrafficStatisticUtil {
    private static TrafficStatisticUtil INSTANCE;
    private static final int MY_UID = android.os.Process.myUid();
    private static long preTotalByteConsume = TrafficStats.getTotalRxBytes()+TrafficStats.getTotalTxBytes();
    private static long preMobileByteConsume = TrafficStats.getMobileRxBytes()+TrafficStats.getMobileTxBytes();
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
    public  synchronized long getCurMobileBytesConsume(){
        long cur = TrafficStats.getMobileTxBytes()+TrafficStats.getMobileRxBytes();
        long result = cur - preMobileByteConsume;
        preMobileByteConsume = cur;
        preTotalByteConsume = TrafficStats.getTotalRxBytes()+TrafficStats.getTotalTxBytes();
        return result;
    }
    public  synchronized long getCurTotalBytesConsume(){
        long cur = TrafficStats.getTotalRxBytes() + TrafficStats.getTotalTxBytes();
        long result = cur - preTotalByteConsume;
        preTotalByteConsume = cur;
        preMobileByteConsume = TrafficStats.getMobileRxBytes()+ TrafficStats.getMobileTxBytes();
        return result;
    }
}
