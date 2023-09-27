package com.iso8583.core.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IsoLogUtil {

  public static String formatIsoMsg(ISOMsg msg) {
    StringBuilder sb = new StringBuilder();
    int maxField = msg.getMaxField();

    Object value = null;
    sb.append("\n{\n");
    sb.append(getDirection(msg));
    sb.append("\t\"0\":\"");
    sb.append(msg.getValue(0));
    sb.append("\"");

    for (int i=2; i<=maxField; i++) {
      value = msg.getValue(i);
      if (value != null) {
        sb.append(",\n");
        sb.append(String.format("\t\"%s\":\"", i));
        if (value instanceof byte[]) {
          sb.append(ISOUtil.hexString((byte[]) value));
        } else {
          sb.append(value.toString());
        }
        sb.append("\"");
      }
    }
    sb.append("\n}");
    return sb.toString();
  }

  private static String getDirection(ISOMsg msg) {
    StringBuilder sb = new StringBuilder();
    sb.append("\t\"direction\":\"");
    if (msg.isIncoming()) {
      sb.append("incoming");
    } else if(msg.isOutgoing()) {
      sb.append("outgoing");
    } else {
      sb.append("");
    }

    sb.append("\",\n");
    return sb.toString();
  }
}
