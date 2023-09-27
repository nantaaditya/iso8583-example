package com.iso8583.core.service.jpos;

import com.iso8583.core.dto.constant.IsoConstant;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOSource;
import org.jpos.transaction.Context;

@Slf4j
public class BaseParticipant {

  protected void sendMessage(Context context) {
    ISOSource source = (ISOSource) context.get(IsoConstant.SOURCE);
    ISOMsg responseIsoMsg = (ISOMsg) context.get(IsoConstant.RESPONSE);

    try {
      source.send(responseIsoMsg);
    } catch (IOException | ISOException e) {
      log.error("failed send iso response ", e);
    }
  }
}
