package com.iso8583.simulator.controller;

import com.iso8583.simulator.dto.BaseDTO;
import com.iso8583.simulator.service.IsoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/api")
public class SimulatorController {

  @Autowired
  private IsoService isoService;

  @PostMapping(
      value = "/request",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public Mono<BaseDTO> request(@RequestBody BaseDTO request) {
    return Mono.fromCallable(() -> isoService.request(request));
  }
}
