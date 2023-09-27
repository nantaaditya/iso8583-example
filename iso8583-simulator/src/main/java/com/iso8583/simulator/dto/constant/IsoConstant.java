package com.iso8583.simulator.dto.constant;

public interface IsoConstant {

  String REQUEST = "request";
  String RESPONSE = "response";
  String SOURCE = "source";
  String SPACE_NAME = "space";
  String QUEUE_NAME = "queue";
  String SPACE_TIME_OUT = "spaceTimeout";

  String SUCCESS_ISO_MSG = "00";
  String NOT_CONNECTED_ISO_MSG = "G1";
  String ERROR_ON_PROCESS_ISO_MSG = "G6";
  String NO_RESPONSE_ISO_MSG = "G8";
  String SYSTEM_ERROR_ISO_MSG = "96";
}
