package com.iso8583.core.service;

import com.iso8583.core.dto.constant.IsoConstant;
import com.iso8583.core.service.jpos.BaseParticipant;
import lombok.extern.slf4j.Slf4j;
import org.jpos.iso.ISOMsg;
import org.jpos.transaction.Context;
import org.springframework.stereotype.Component;

@Slf4j
@Component("UnknownParticipant")
public class UnknownParticipant extends BaseParticipant implements IService {

  @Override
  public void processIsoMessage(Context context) {
    ISOMsg resIsoMsg = (ISOMsg) context.get(IsoConstant.RESPONSE);

    resIsoMsg.set(39, "06");
    resIsoMsg.set(48, "Message tidak dikenali");

    context.put(IsoConstant.RESPONSE, resIsoMsg);
    sendMessage(context);
  }
}
