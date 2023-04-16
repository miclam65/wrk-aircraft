package com.da.wrk.aircraft.web.rest;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({ "/app" })
public class AppController {

    @Autowired
    private Environment env;

    @Value("${spring.application.name}")
    private String appName;

    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    @GetMapping({ "appname" })
    public ResponseEntity getAppName() {
      return ResponseEntity.ok(this.appName);
    }

    @GetMapping({ "dsurl" })
    public ResponseEntity getDatasourceUrl() {
      return ResponseEntity.ok(this.datasourceUrl);
    }

    @GetMapping({ "version" })
    public ResponseEntity<String> getAppVersion() {
      return ResponseEntity.ok(this.env.getProperty("app.version"));
    }

    @GetMapping({ "env" })
    public ResponseEntity<String> getAppEnv() {
      return ResponseEntity.ok(this.env.getProperty("app.env"));
    }

    @GetMapping({ "ip" })
    public ResponseEntity<String> getAppIP(HttpServletRequest request) {
      String addr = "";
      addr = request.getRemoteHost() + ":" + request.getRemotePort();
      return ResponseEntity.ok(addr);
    }
}
