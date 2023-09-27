package com.iso8583.core.service;

import com.iso8583.core.dto.constant.IsoConstant;
import com.iso8583.core.utils.JposContextUtil;
import java.io.Serializable;
import lombok.extern.slf4j.Slf4j;
import org.jpos.core.Configurable;
import org.jpos.core.Configuration;
import org.jpos.core.ConfigurationException;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.transaction.Context;
import org.jpos.transaction.TransactionParticipant;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.util.StringUtils;

@Slf4j
public class ParticipantSelector
    implements TransactionParticipant, Configurable {

  private Configuration configuration;

  @Override
  public void setConfiguration(Configuration configuration) throws ConfigurationException {
    this.configuration = configuration;
  }

  @Override
  public int prepare(long l, Serializable serializable) {
    Context context = (Context) serializable;
    ISOMsg reqIsoMsg = (ISOMsg) context.get(IsoConstant.REQUEST);

    String beanName = getSelector(reqIsoMsg);
    IService service = null;
    try {
      service = JposContextUtil.getBean(beanName, IService.class);
    } catch (NoSuchBeanDefinitionException e) {
      service = JposContextUtil.getBean(UnknownParticipant.class);
    }

    service.processIsoMessage(context);
    return PREPARED;
  }

  private String getSelector(ISOMsg reqIsoMsg) {
    String selector = "";
    try {
      selector = configuration.get(reqIsoMsg.getMTI());
    } catch (ISOException e) {
      log.error("error selector", e);
    } finally {
      log.info("selector {}", selector);
    }

    if (!StringUtils.hasLength(selector)) selector = "UnknownParticipant";

    return selector;
  }
}
