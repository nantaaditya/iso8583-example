package com.iso8583.core.service;

import org.jpos.transaction.Context;

public interface IService {
  void processIsoMessage(Context context);
}
