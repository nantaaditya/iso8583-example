package com.iso8583.core.configuration;

import java.io.FileNotFoundException;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.jpos.q2.Q2;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

@Slf4j
@Configuration
public class Q2Configuration {

  private Q2 q2;

  @PostConstruct
  public void setUp() throws FileNotFoundException {
    String path = ResourceUtils.getFile("classpath:deploy").getPath();
    log.info("path {}", path);
    q2 = new Q2(path);
    q2.start();
  }

  @PreDestroy
  public void tearDown() {
    if(q2.running()) q2.shutdown();
  }
}
