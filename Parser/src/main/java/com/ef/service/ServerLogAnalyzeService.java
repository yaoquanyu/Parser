package com.ef.service;

import com.ef.model.ServerIllegalAccess;

import java.util.HashMap;
import java.util.List;

/**
 * Created by yaoquanyu on 11/15/17.
 */
public interface ServerLogAnalyzeService {

    void analyzeLog(String fileName, String start, long timeInterval, int threshold, HashMap<String, Integer> accessCounterPerIP);
    boolean withinTimeInterval(String startTime, String logTime, Long interval);
    void getIllegalAccessRecords(String startDate, String threshold, HashMap<String, Integer> accessCounterPerIP,
                                 String duration, List<ServerIllegalAccess> exceedLimitAccesses);

}
