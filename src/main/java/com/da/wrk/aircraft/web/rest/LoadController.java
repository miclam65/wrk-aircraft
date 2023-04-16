package com.da.wrk.aircraft.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.da.wrk.aircraft.service.LoadService;

@RestController
@RequestMapping("/load")
public class LoadController {

	static final Logger logger = LoggerFactory.getLogger(LoadController.class);

	private final LoadService loadService;

	@Value("${spring.jdbc.validation.query.timeout}")
    private String timeoutDB;

	@Autowired
	LoadController(LoadService loadService) {
		this.loadService = loadService;
	}

	@GetMapping(value = "/ram")
	public String loadMemory(
			@RequestParam(value = "mem", defaultValue = "1", required = true) int numMemory,
			@RequestParam(value = "wait", defaultValue = "1", required = false) int numWaitingTime) {
		logger.info("loadMemory - #Memory: " + numMemory + " - #WaitingTime: " + numWaitingTime);
		return loadService.loadMemory(numMemory, numWaitingTime);
	}

	@GetMapping(value = "/cpu")
	public String loadCPU(@RequestParam(value = "core", defaultValue = "1", required = true) int numCore,
			@RequestParam(value = "th", defaultValue = "1", required = false) int numThreadsPerCore,
			@RequestParam(value = "du", defaultValue = "1", required = false) int numDuration) {
		logger.info("LoadCPU #Core: " + numCore + " - #ThreadsPerCore: " + numThreadsPerCore + " - #Duration:" + numDuration);
		return loadService.loadCPU(numCore, numThreadsPerCore, numDuration);
	}

	@GetMapping(value = "/sqltest/")
    public ResponseEntity<String> sqlTest(
        @RequestParam(value = "duration", defaultValue = "1", required = true) int duration, 
        @RequestParam(value = "timeout", defaultValue = "0", required = true) int timeout) {
      boolean r = loadService.sqlTest(duration, timeout);
      logger.info("sqlTest");
      if (r) {
        return ResponseEntity.ok("sqlTest: (timeout DB: " + timeoutDB + ") - (duration: " + duration + ") - (timeout: " + timeout + ")");
      }
      else {
        return new ResponseEntity<String>(HttpStatus.EXPECTATION_FAILED);
      }		
    }
}
