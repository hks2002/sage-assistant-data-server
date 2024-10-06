/*****************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                    *
 * @CreatedDate           : 2023-11-24 16:53:52                              *
 * @LastEditors           : Robert Huang<56649783@qq.com>                    *
 * @LastEditDate          : 2023-12-13 09:37:35                              *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                  *
 ****************************************************************************/

package com.da.sageassistantserver.model;

import com.alibaba.fastjson2.annotation.JSONType;
import java.sql.Date;
import lombok.Data;

@Data
@JSONType(alphabetic = false)
public class LinksSalesLine {

  private String OrderNO;
  private String OrderLine;
  private String OrderProjectNO;
  private String OrderTrackingNO;
  private String OrderCustomerCode;
  private String OrderCustomerName;
  private Date OrderDate;
  private String OrderPN;
  private String OrderPNDescription;
  private Float OrderQty;
  private String OrderUnit;
  private Double OrderAmount;
  private String OrderCurrency;
  private Integer OrderStatus;
}
