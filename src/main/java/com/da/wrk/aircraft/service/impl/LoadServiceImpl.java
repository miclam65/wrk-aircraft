package com.da.wrk.aircraft.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.HashMap;
import java.util.Map;

import com.da.wrk.aircraft.repository.LoadRepository;
import com.da.wrk.aircraft.service.LoadService;

@Component
public class LoadServiceImpl implements LoadService {

    static final Logger logger = LoggerFactory.getLogger(LoadServiceImpl.class);

    private final LoadRepository loadRepo;

    @Autowired
	LoadServiceImpl(LoadRepository loadRepo) {
		this.loadRepo = loadRepo;
	}
    
    private static final String messageTemplate = "\"%s\":%s";
    private static final long MEGABYTE = 1024L * 1024L;

    public static long bytesToMegabytes(long bytes) {
        return bytes / MEGABYTE;
    }

    @Autowired
	private EntityManager entityManager;

    @Override
    public String loadMemory(int numMemory, int numWaitingTime) {
        String jsonResult = "{\"Load Memory (Mb)\":{";
        Runtime runtime = Runtime.getRuntime();
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();

        try {
            Map<String, byte[]> map = new HashMap<>();

            for (int i = 0; i < numMemory; i++) {   
                map.put("" + i, new byte[(int)MEGABYTE]);
            }
            long totalMemory = runtime.totalMemory();
            long freeMemory = runtime.freeMemory();
    
            Thread.sleep(1000L * numWaitingTime);
    
            String msgLoadedMemory = String.format(messageTemplate, "Loaded Memory [mem]", numMemory) + ",";
            String msgTotalMemory = String.format(messageTemplate, "Total Memory", bytesToMegabytes(totalMemory)) + ",";
            String msgFreeMemory = String.format(messageTemplate, "Free Memory", bytesToMegabytes(freeMemory)) + ",";
            String msgUsedMemory = String.format(messageTemplate, "Used Memory", bytesToMegabytes(totalMemory - freeMemory)) + ",";
            String msgNumWaitingTime = String.format(messageTemplate, "Waiting Time [wait]", numWaitingTime);
            jsonResult += msgLoadedMemory + msgTotalMemory + msgFreeMemory + msgUsedMemory + msgNumWaitingTime + "}}";                       
            logger.info("loadMemory: " + jsonResult);
        } 
        catch (Exception e) {
            jsonResult = "{\"Exception\": \"" + e +"\"}";
            logger.error("loadMemory: " + jsonResult);
        }
        catch (OutOfMemoryError me) {
            MemoryUsage heapUsage = memoryBean.getHeapMemoryUsage();
            long maxMemory = heapUsage.getMax() / MEGABYTE;
            long usedMemory = heapUsage.getUsed() / MEGABYTE;
            jsonResult = "{\"OutOfMemoryError\": \"" + me + " - Used Memory: " + usedMemory + " Mb - Max Memory: " + maxMemory + " Mb" +"\"}";
            logger.error("loadMemory: " + jsonResult);
        }
        
        runtime.gc();  
        System.gc();  

        return jsonResult;
    }

    @Override
    public String loadCPU(int numCore, int numThreadsPerCore, long duration) {
        String jsonResult = "{\"Load CPU\":{";

        String msgNumCore = String.format(messageTemplate, "Core [core]", numCore) + ",";
        String msgNumThreadsPerCore = String.format(messageTemplate, "Threads Per Core [th]", numThreadsPerCore) + ",";
        String msgDuration = String.format(messageTemplate, "Duration [du]", duration);
        jsonResult += msgNumCore + msgNumThreadsPerCore + msgDuration + "}}";    
        
        double load = 0.8;
        for (int thread = 0; thread < numCore * numThreadsPerCore; thread++) {
            new BusyThread("Thread" + thread, load, duration).start();
        }

        logger.info("loadMemory: " + jsonResult);

        return jsonResult;
    }

    private static class BusyThread extends Thread {
        private double load;
        private long duration;

        public BusyThread(String name, double load, long duration) {
            super(name);
            this.load = load;
            this.duration = duration;
        }

        @Override
        public void run() {
            long startTime = System.currentTimeMillis();
            try {
                while (System.currentTimeMillis() - startTime < duration) {
                    // Every 100ms, sleep for the percentage of unladen time
                    if (System.currentTimeMillis() % 100 == 0) {
                        Thread.sleep((long) Math.floor((1 - load) * 100));
                    }
                }
            } catch (InterruptedException e) {
                logger.error("loadMemory: " + e.getMessage());
            }
        }
    }

	@Override
	public boolean sqlTest(int duration, int timeout) {
		boolean r = true;
		List results = null;
		String q = "select setting from pg_settings where name = 'statement_timeout'";
		Query query  = entityManager.createNativeQuery(q);
		results = query.getResultList();
		logger.info("statement_timeout: " + results);

		q = "SELECT 666 FROM pg_sleep(" + duration +");";
		logger.info("Query: " + q + " - Timeout: " + timeout);
		query  = entityManager.createNativeQuery(q);
		if (timeout > 0) {
			query.setHint("javax.persistence.query.timeout", (int) TimeUnit.SECONDS.toMillis(timeout));
		}
		
		results = null;
		try {
			results = query.getResultList();
			logger.info("Query: " + q + " - Result: " + results.toString());
		} catch (Exception e) {
			logger.error("Query: " + q + " - Result: " + results.toString());
			r =false;
		}
		
		return r;
	}	    
}
