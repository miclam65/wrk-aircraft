package com.da.wrk.aircraft.web.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/shell")
public class ShellController {

  @Autowired
  private Environment env;

  @GetMapping()
	public ResponseEntity getCustomShell(@RequestParam(value="cmd", defaultValue="echo enter a command!", required=true) String shellCommand) {
		return ResponseEntity.ok(callShell(shellCommand));
  }
  
  public String callShell(String shellCommand) {
    String result = "";
    Process process = null;
    String osName = System.getProperty("os.name");
    String osVersion = System.getProperty("os.version");
    String osArch = System.getProperty("os.arch");
    String logShellCommand = "Shell Command: ";

    try {
      log.info("osName: " + osName + " - osVersion: " + osVersion + " - osArch: " + osArch);
      String[] completeShellCommand = { "/bin/sh", "-c", shellCommand };
      if (osName.toLowerCase().contains("win")) {
        completeShellCommand[0] = "cmd";
        completeShellCommand[1] = "/c";
      }
      logShellCommand += completeShellCommand[0] + " " + completeShellCommand[1] + " " + completeShellCommand[2];
      log.info(logShellCommand);
      process = Runtime.getRuntime().exec(completeShellCommand);
      result = captureShellOutput(process);
    } 
    catch (Throwable e) {
      log.error(logShellCommand + " - " + e);
    } 
    finally {
      if (null != process)
        process.destroy();
    }
    return result;
  }

  private String captureShellOutput(Process process) throws IOException, InterruptedException {
    StringBuilder result = new StringBuilder();
    int exitCode = process.waitFor();
    if (exitCode == 0) {      
      BufferedReader output = new BufferedReader(new InputStreamReader(process.getInputStream()));
      String line;
      while ((line = output.readLine()) != null) {
        result.append("\n" + line);
      }
    } else {
      BufferedReader error = new BufferedReader(new InputStreamReader(process.getErrorStream()));
      String line;
      while ((line = error.readLine()) != null) {
        result.append("\n" + line);
      }
    }
    log.info(result.toString());
    return result.toString();
  }
}
