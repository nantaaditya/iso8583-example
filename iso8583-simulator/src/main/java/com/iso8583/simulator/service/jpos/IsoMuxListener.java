package com.iso8583.simulator.service.jpos;

import com.iso8583.simulator.dto.constant.IsoConstant;
import org.jpos.core.Configurable;
import org.jpos.core.Configuration;
import org.jpos.core.ConfigurationException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISORequestListener;
import org.jpos.iso.ISOSource;
import org.jpos.space.SpaceFactory;
import org.jpos.transaction.Context;

public class IsoMuxListener implements ISORequestListener, Configurable {

  private String queue;
  private long timeout;

  @Override
  public void setConfiguration(Configuration cfg) throws ConfigurationException {
    this.queue = cfg.get(IsoConstant.QUEUE_NAME);
    this.timeout = cfg.getLong(IsoConstant.SPACE_TIME_OUT);
  }

  @Override
  public boolean process(ISOSource source, ISOMsg m) {
    Context context = new Context();
    context.put(IsoConstant.SOURCE, source);
    context.put(IsoConstant.REQUEST, m);

    SpaceFactory.getSpace().push(this.queue, context, this.timeout);

    return true;
  }
}
