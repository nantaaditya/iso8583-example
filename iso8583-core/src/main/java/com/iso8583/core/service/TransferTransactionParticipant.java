package com.iso8583.core.service;

import com.iso8583.core.dto.TransferDTO;
import com.iso8583.core.service.jpos.BaseExternalCallParticipant;
import java.util.Calendar;
import lombok.extern.slf4j.Slf4j;
import org.jpos.iso.ISOMsg;
import org.jpos.transaction.Context;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component("TransferTransactionParticipant")
public class TransferTransactionParticipant
    extends BaseExternalCallParticipant<TransferDTO>
    implements IService {

  private WebClient webClient = WebClient.builder()
      .baseUrl("http://localhost:1000/api/transfer")
      .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
      .build();

  @Override
  public void processIsoMessage(Context context) {
    Context ctx = (Context) context;

    webClient.get()
        .uri(uri -> uri.path("/{amount}").build(Calendar.getInstance().get(Calendar.SECOND)))
        .retrieve()
        .bodyToMono(TransferDTO.class)
        .doOnError(error -> log.error("failed call client ", error))
        .subscribe(response -> composeIsoResponse(response, ctx));
  }

  @Override
  protected void prepareIsoResponse(ISOMsg msg, TransferDTO transferDTO) {
    log.info("response {}", transferDTO);

    msg.set(39, transferDTO.responseCode());
  }
}
