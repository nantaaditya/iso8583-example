package com.iso8583.simulator.dto;

import java.beans.Transient;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jpos.iso.ISOMsg;
import org.springframework.beans.BeanUtils;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class BaseDTO {
  private String mti;
  private long timeout;
  private String errorCode;
  private String message;
  private Map<String, Object> body = new HashMap<>();

  @Transient
  public boolean isRequest()  {
    return Character.getNumericValue(this.mti.charAt (2)) % 2 == 0;
  }

  public void setResponseMti() {
    if (!isRequest()) return;

    char c1 = this.mti.charAt(3);
    char c2 = '0';
    switch (c1) {
      case '0', '1': c2='0';break;
      case '2', '3': c2='2';break;
      case '4', '5': c2='4';break;

    }
    this.mti = this.mti.substring(0,2) + (Character.getNumericValue(this.mti.charAt (2))+1) + c2;
  }

  public void putBodyValue(String key, Object value) {
    this.body.put(key, value);
  }

  @Transient
  public ISOMsg toIsoMsg() {
    ISOMsg reqIsoMsg = new ISOMsg();
    try {
      reqIsoMsg.setMTI(getMti());

      Iterator<String> iter = getBody().keySet().iterator();

      String key = null;
      while(iter.hasNext()) {
        key = iter.next();
        reqIsoMsg.set(key, (String) getBody().get(key));
      }
      return reqIsoMsg;
    } catch(Exception e) {
      log.error("Exception when create msg ", e);
    }
    return reqIsoMsg;
  }

  @Transient
  public BaseDTO clone() {
    BaseDTO response = new BaseDTO();
    BeanUtils.copyProperties(this, response);
    return response;
  }
}
