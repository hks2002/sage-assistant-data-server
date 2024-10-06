/**********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                             *
 * @CreatedDate           : 2023-02-19 20:31:38                                                                       *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                             *
 * @LastEditDate          : 2024-12-25 14:49:14                                                                       *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                           *
 *********************************************************************************************************************/

package com.da.sageassistantserver.utils;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ResponseJsonHelper {

  public enum MsgTyp {
    RESULT(0),
    INFO(1),
    QUESTION(2),
    WARN(3),
    ERROR(4);

    private Integer value;

    MsgTyp(Integer value) {
      this.value = value;
    }

    public Integer getValue() {
      return value;
    }
  }

  public static JSONObject rtnObj(Boolean success, MsgTyp msgTyp, String msg) {
    switch (msgTyp) {
      case RESULT:
        break;
      case INFO:
        log.info(msg);
        break;
      case WARN:
        log.warn(msg);
        break;
      case ERROR:
        log.error(msg);
        break;
      default:
        break;
    }
    JSONObject rtn = new JSONObject();
    rtn.put("success", success);
    rtn.put("msgTyp", msgTyp);
    rtn.put("msg", msg);
    return rtn;
  }

  public static JSONObject missingAuth() {
    return rtnObj(false, MsgTyp.WARN, "Authorization is required.");
  }

  public static JSONObject paraRequired(String name) {
    return rtnObj(false, MsgTyp.ERROR, name + " is required.");
  }
}
