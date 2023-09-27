package com.iso8583.core.configuration;

import com.iso8583.core.utils.IsoLogUtil;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jpos.iso.ISOMsg;
import org.jpos.util.Log;
import org.jpos.util.LogEvent;
import org.jpos.util.LogListener;
import org.jpos.util.LogSource;
import org.jpos.util.Loggeable;

public class Log4JConfiguration implements LogListener {

  private static final String LOG_FORMAT = "realm: {}, tag: {}, body: {}";

  @Override
  public LogEvent log(LogEvent ev) {
    Logger logger = getLog4jLogger(ev);
    if (logger!=null) {
      doLog(logger, ev);
    }
    return ev;
  }

  private Logger getLog4jLogger(LogEvent ev) {
    LogSource source = ev.getSource();
    Class clasz = null;

    if (source == null) {
      clasz = Log.class;
    } else {
      clasz = source.getClass();
    }
    return LogManager.getLogger(clasz);
  }

  private void doLog(Logger logger, LogEvent ev) {
    Level level = null;
    String preMsg = "";

    try {
      level = Level.valueOf(ev.getTag());
    } catch(Exception e) {
      level = Level.INFO;
      preMsg = ev.getTag();
    }

    List<Object> payload =	ev.getPayLoad();

    if ((payload==null || payload.isEmpty()) && !preMsg.isEmpty()) {
      logger.log(level,LOG_FORMAT, ev.getRealm(), preMsg);
    } else {
      for (Object o : payload) {
        if (o instanceof ISOMsg) {
          logger.log(level,LOG_FORMAT, ev.getRealm(), preMsg, createMsg((ISOMsg) o));
        } else if (o instanceof Loggeable) {
          String flog = dumpLogable((Loggeable) o);
          logger.log(level,  LOG_FORMAT, ev.getRealm(), preMsg, flog);
        } else if(o instanceof Throwable) {
          logger.error("got an error {}, ", ((Throwable) o).getMessage(), (Throwable) o);
        } else {
          logger.log(level,LOG_FORMAT, ev.getRealm(), preMsg, o);
        }
      }
    }

  }

  private String createMsg(ISOMsg msg) {
    return IsoLogUtil.formatIsoMsg(msg);

  }

  private String dumpLogable(Loggeable loggeable) {
    try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
      PrintStream ps = new PrintStream(baos, true);
      loggeable.dump(ps,"");

      return new String(baos.toByteArray());
    } catch (IOException ex) {
      return "";
    }
  }

}
