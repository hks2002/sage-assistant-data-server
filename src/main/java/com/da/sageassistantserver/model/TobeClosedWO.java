/**********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                             *
 * @CreatedDate           : 2022-03-26 17:01:00                                                                       *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                             *
 * @LastEditDate          : 2024-12-25 14:42:20                                                                       *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                           *
 *********************************************************************************************************************/

package com.da.sageassistantserver.model;

import com.alibaba.fastjson2.annotation.JSONType;
import java.util.Date;
import lombok.Data;

@Data
@JSONType(alphabetic = false)
public class TobeClosedWO {

  private String ProjectNO;
  private String OrderNO;
  private String WorkOrderNO;
  private String WOStatus;
  private String ProductionStatus;
  private String OrderType;
  private String CustomerCode;
  private String CustomerName;
  private String PN;
  private Integer Qty;
  private Date OrderDate;
}
