package com.iso8583.core;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.jpos.iso.ISOChannel;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOPackager;
import org.jpos.iso.ISOUtil;
import org.jpos.iso.channel.ASCIIChannel;
import org.jpos.iso.packager.ISO87APackager;

public class ClientTest {
  public static void main(String[] args) {
    String hostname = "localhost";
    int portNumber = 2300;
    ISOPackager packager = new ISO87APackager();
    ISOChannel isoChannel = new ASCIIChannel(hostname, portNumber, packager);

    try {
      isoChannel.connect();
      ISOMsg networkReq = new ISOMsg();
      networkReq.setPackager(packager);
      networkReq.setMTI("0800");
      networkReq.set(3, "123456");
      networkReq.set(7, new SimpleDateFormat("yyyyMMdd").format(new Date()));
      networkReq.set(11, "000001");
      networkReq.set(12, new SimpleDateFormat("HHmmss").format(new Date()));
      networkReq.set(13, new SimpleDateFormat("MMdd").format(new Date()));
      networkReq.set(41, "00000001");
      networkReq.set(48, "Tutorial ISO 8583 Dengan Java");
      networkReq.set(70, "301");

      networkReq.dump(System.out, "");
      System.out.println(ISOUtil.hexdump(networkReq.pack()));
      isoChannel.send(networkReq);
      ISOMsg msg = isoChannel.receive();
      msg.dump(System.out, "");
      System.out.println(ISOUtil.hexdump(msg.pack()));
      isoChannel.disconnect();
    } catch(ISOException isoe) {
      System.out.println(isoe);
    } catch (IOException e) {
      System.out.println(e);
    }
  }
}
