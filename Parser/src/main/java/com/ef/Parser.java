package com.ef;

import com.ef.dao.ServerIllegalAccessDao;
import com.ef.service.ServerLogAnalyzeService;
import com.ef.Constent.Constents;
import com.ef.model.ServerIllegalAccess;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.*;

/**
 * Created by yaoquanyu on 11/15/17.
 */
public class Parser {

    public static void main(String[] args) {

        String file = null, startDate = null, duration = null, threshold = null;
        long timeInterval = 0L;
        for (String i: args) {
            String[] parameter = i.split(Constents.ARGUMENT_DELIMITER);
            switch (parameter[Constents.KEY_INDEX]) {
                case Constents.ACCESS_LOG_FLAG:
                    file = parameter[Constents.VALUE_INDEX] + "/access.log";
                    break;
                case Constents.START_DATE_FLAG:
                    startDate = parameter[Constents.VALUE_INDEX];
                    break;
                case Constents.DURATION_FLAG:
                    duration = parameter[Constents.VALUE_INDEX];
                    break;
                case Constents.THRESHOLD_FLAG:
                    threshold = parameter[Constents.VALUE_INDEX];
                    break;
                default:
                    break;
            }
        }

        if (Constents.DURATION_TYPE_HOURLY.equalsIgnoreCase(duration)) {
            timeInterval = Constents.HOUR_INTERVAL;
        } else if (Constents.DURATION_TYPE_DAILY.equalsIgnoreCase(duration)) {
            timeInterval = Constents.DAYILY_INTERVAL;
        }

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:META-INF.spring/applicationContext.xml");
        ServerIllegalAccessDao serverIllegalAccessDao = context.getBean(ServerIllegalAccessDao.class);
        ServerLogAnalyzeService serverLogAnalyzeService = context.getBean(ServerLogAnalyzeService.class);

        HashMap<String, Integer> accessCounterPerIP = new HashMap<String, Integer>();
        List<ServerIllegalAccess> exceedLimitAccesses = new ArrayList<>();

        serverLogAnalyzeService.analyzeLog(file, startDate, timeInterval, Integer.parseInt(threshold), accessCounterPerIP);
        serverLogAnalyzeService.getIllegalAccessRecords(startDate, threshold, accessCounterPerIP, duration, exceedLimitAccesses);

        serverIllegalAccessDao.saveExceedLimitAccesses(exceedLimitAccesses);

        context.close();
    }
}