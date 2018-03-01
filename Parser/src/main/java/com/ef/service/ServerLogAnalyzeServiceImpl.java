package com.ef.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.ef.Constent.Constents;
import com.ef.model.ServerIllegalAccess;

/**
 * Created by yaoquanyu on 11/15/17.
 */
public class ServerLogAnalyzeServiceImpl implements ServerLogAnalyzeService {
    public void analyzeLog(String fileName, String start, long timeInterval, int threshold, HashMap<String, Integer> accessCounterPerIP) {
        try {
            System.out.println(Constents.ANSI_BLUE + "Start analyzing access log. "  + Constents.ANSI_RESET);
            File file = new File(fileName);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] data = line.split(Pattern.quote(Constents.DELIMITER));
                if (withinTimeInterval(start, data[Constents.TIME_INDEX], timeInterval)) {
                    if (accessCounterPerIP.containsKey(data[Constents.IP_INDEX])) {
                        accessCounterPerIP.put(data[Constents.IP_INDEX], accessCounterPerIP.get(data[Constents.IP_INDEX]) + 1);
                    } else {
                        accessCounterPerIP.put(data[Constents.IP_INDEX], 1);
                    }
                }
            }
            System.out.println(Constents.ANSI_BLUE + "Finish processing access log. "  + Constents.ANSI_RESET);
        } catch (Exception e) {
            System.err.println("Error while processing access log file");
            e.printStackTrace();
        }
    }

    public boolean withinTimeInterval(String startTime, String logTime, Long interval) {
        try {
            DateFormat startTimeFormat = new SimpleDateFormat(Constents.INPUT_TIME_STRING_FORMAT);
            DateFormat logTimeFormat = new SimpleDateFormat(Constents.LOG_TIME_STRING_FORMAT);

            Date startDate = startTimeFormat.parse(startTime);
            Date logDate = logTimeFormat.parse(logTime);

            if (logDate.getTime() - startDate.getTime() >= 0 &&
                    logDate.getTime() - startDate.getTime() < interval) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            System.err.println("Error while checking time Interval for: start time: " + startTime
                    + ", log time: " + logTime + ", interval: " + interval);
            e.printStackTrace();
        }

        return false;
    }

    public void getIllegalAccessRecords(String startDate, String threshold, HashMap<String, Integer> accessCounterPerIP,
                                        String duration, List<ServerIllegalAccess> exceedLimitAccesses) {
        try {
            DateFormat startTimeFormat = new SimpleDateFormat(Constents.INPUT_TIME_STRING_FORMAT);

            Date startTime = startTimeFormat.parse(startDate);
            Date endDate = new Date(startTime.getTime() + Constents.HOUR_INTERVAL);

            System.out.println(Constents.ANSI_BLUE + "Start processing access records: "  + Constents.ANSI_RESET);
            int accessLimit = Integer.parseInt(threshold);
            for (Map.Entry<String, Integer> i : accessCounterPerIP.entrySet()) {
                if (i.getValue() >= accessLimit) {
                    ServerIllegalAccess serverIllegalAccess = new ServerIllegalAccess();
                    serverIllegalAccess.setStartDate(startTime);
                    serverIllegalAccess.setEndDate(endDate);
                    serverIllegalAccess.setIp(i.getKey());
                    serverIllegalAccess.setAccessCount(i.getValue());
                    System.out.println(Constents.ANSI_BLUE + "IP: " + i.getKey()
                            + ", exceed access limit: " + threshold + " with acces count: " + i.getValue()
                            + " since: " + startDate + " for duration: " + duration  + Constents.ANSI_RESET);
                    exceedLimitAccesses.add(serverIllegalAccess);
                }
            }
        } catch (Exception e) {
            System.err.println("Error while processing result");
            e.printStackTrace();
        }
    }
}
