package com.iso8583.core.controller;

import com.iso8583.core.model.Response;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class TransactionController {

  @GetMapping(
      value = "/api/transfer/{amount}",
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public Mono<Response> transfer(@PathVariable int amount) {
    return Mono.just(new Response(amount % 2 == 0 ? "00" : "06"));
  }
}
