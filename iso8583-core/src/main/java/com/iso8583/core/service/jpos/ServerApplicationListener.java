package com.iso8583.core.service.jpos;

import com.iso8583.core.dto.constant.IsoConstant;
import lombok.extern.slf4j.Slf4j;
import org.jpos.core.Configurable;
import org.jpos.core.Configuration;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISORequestListener;
import org.jpos.iso.ISOSource;
import org.jpos.space.Space;
import org.jpos.space.SpaceFactory;
import org.jpos.transaction.Context;

@Slf4j
public class ServerApplicationListener implements ISORequestListener, Configurable {

  private Configuration configuration;

  @Override
  public void setConfiguration(Configuration cfg) {
    this.configuration = cfg;
  }

  @Override
  public boolean process(ISOSource source, ISOMsg isoMsg) {
    String spaceName = configuration.get(IsoConstant.SPACE_NAME);
    String queueName = configuration.get(IsoConstant.QUEUE_NAME);
    Long timeout = configuration.getLong(IsoConstant.SPACE_TIME_OUT);

    Context context = new Context();
    Space<String, Context> space = SpaceFactory.getSpace(spaceName);

    try {
      ISOMsg respMsg = (ISOMsg) isoMsg.clone();
      respMsg.setResponseMTI();

      context.put(IsoConstant.REQUEST, isoMsg);
      context.put(IsoConstant.RESPONSE, respMsg);
      context.put(IsoConstant.SOURCE, source);
    } catch (ISOException e) {
      log.error("failed listen iso msg ", e);
    }

    space.out(queueName, context, timeout);
    return true;
  }
}
