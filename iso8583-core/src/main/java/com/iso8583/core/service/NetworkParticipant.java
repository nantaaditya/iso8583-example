package com.iso8583.core.service;

import com.iso8583.core.dto.HealtCheckDTO;
import com.iso8583.core.dto.constant.IsoConstant;
import com.iso8583.core.service.jpos.BaseExternalCallParticipant;
import lombok.extern.slf4j.Slf4j;
import org.jpos.iso.ISOMsg;
import org.jpos.transaction.Context;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component("NetworkParticipant")
public class NetworkParticipant
    extends BaseExternalCallParticipant<HealtCheckDTO>
    implements IService {

  private WebClient webClient = WebClient.builder()
      .baseUrl("http://localhost:1000/actuator/health")
      .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
      .build();

  @Override
  public void processIsoMessage(Context context) {
    Context ctx = (Context) context;

    webClient.get()
        .retrieve()
        .bodyToMono(HealtCheckDTO.class)
        .doOnError(error -> log.error("failed call client ", error))
        .subscribe(response -> composeIsoResponse(response, ctx));
  }

  @Override
  protected void prepareIsoResponse(ISOMsg msg, HealtCheckDTO healtCheckDTO) {
    log.info("response {}", healtCheckDTO);
    if (healtCheckDTO == null) {
      msg.set(39, "06");
      return;
    }

    if (!"UP".equals(healtCheckDTO.status())) {
      msg.set(39, "06");
      return;
    }

    msg.set(39, IsoConstant.SUCCESS_ISO_MSG);
  }
}
