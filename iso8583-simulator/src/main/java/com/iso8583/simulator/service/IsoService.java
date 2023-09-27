package com.iso8583.simulator.service;

import com.iso8583.simulator.dto.BaseDTO;
import com.iso8583.simulator.dto.constant.IsoConstant;
import lombok.extern.slf4j.Slf4j;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;
import org.jpos.iso.MUX;
import org.jpos.q2.iso.QMUX;
import org.jpos.util.NameRegistrar.NotFoundException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class IsoService {

  public BaseDTO request(BaseDTO request) throws NotFoundException {
    BaseDTO response = request.clone();
    response.setResponseMti();

    MUX mux = QMUX.getMUX("simulator_mux");
    if (mux==null) {
      log.error("mux is not found");
      response.setErrorCode(IsoConstant.SYSTEM_ERROR_ISO_MSG);
      return response;
    }

    if (!mux.isConnected()) {
      response.setErrorCode(IsoConstant.NOT_CONNECTED_ISO_MSG);
      return response;
    }


    ISOMsg resIsoMsg = null;
    try{
      resIsoMsg = mux.request(request.toIsoMsg(), request.getTimeout());
    } catch(ISOException e) {
      log.error("Exception when request message ",  e);
      response.setErrorCode(IsoConstant.ERROR_ON_PROCESS_ISO_MSG);
      return response;
    }

    if (resIsoMsg == null) {
      response.setErrorCode(IsoConstant.NO_RESPONSE_ISO_MSG);
    } else {
      constructCckRspMsg(response, resIsoMsg);
    }
    return response;
  }

  private void constructCckRspMsg(BaseDTO response, ISOMsg resIsoMsg) {
    Object value = null;
    for (int i=2; i<resIsoMsg.getMaxField(); i++) {
      if (!resIsoMsg.hasField(i)) continue;

      value = resIsoMsg.getValue(i);
      if (value instanceof String) {
        response.putBodyValue(String.valueOf(i), (String) value);
      } else if(value instanceof byte[]) {
        response.putBodyValue(String.valueOf(i), ISOUtil.hexString((byte[])value));
      }
    }
    response.setErrorCode(resIsoMsg.getString(39));
  }

}
