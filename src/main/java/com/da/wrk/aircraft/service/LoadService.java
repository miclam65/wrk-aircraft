package com.da.wrk.aircraft.service;

import org.springframework.stereotype.Service;

@Service
public interface LoadService {
    public String loadMemory(int numMemory, int numWaitingTime);
    public String loadCPU(int numCore, int numThreadsPerCore, long duration);

    public boolean sqlTest(int duration, int timeout);
}