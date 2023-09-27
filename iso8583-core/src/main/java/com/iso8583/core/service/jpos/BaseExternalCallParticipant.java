package com.iso8583.core.service.jpos;

import com.iso8583.core.dto.constant.IsoConstant;
import lombok.extern.slf4j.Slf4j;
import org.jpos.iso.ISOMsg;
import org.jpos.transaction.Context;

@Slf4j
public abstract class BaseExternalCallParticipant<RESPONSE> extends BaseParticipant{

  protected void composeIsoResponse(RESPONSE response, Context context) {
    ISOMsg isoResponse = (ISOMsg) context.get(IsoConstant.RESPONSE);

    prepareIsoResponse(isoResponse, response);

    context.put(IsoConstant.RESPONSE, isoResponse);

    sendMessage(context);
  }

  protected abstract void prepareIsoResponse(ISOMsg msg, RESPONSE response);
}
