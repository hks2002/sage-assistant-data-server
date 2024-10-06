/**********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                             *
 * @CreatedDate           : 2022-03-26 17:01:00                                                                       *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                             *
 * @LastEditDate          : 2024-12-09 19:46:04                                                                       *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                           *
 *********************************************************************************************************************/

package com.da.sageassistantserver.model;

import com.alibaba.fastjson2.annotation.JSONType;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

@Data
@JSONType(alphabetic = false)
public class TobeDealWithOrderLine {

  private String SalesOrderNO;
  private String ProjectNO;
  private String OrderType;
  private String OrderCategory;
  private String PN;
  private String Description;
  private Integer Qty;
  private String Unit;
  private BigDecimal NetPrice;
  private BigDecimal SalesPrice;
  private BigDecimal USD;
  private String CustomerCode;
  private String CustomerName;
  private Date OrderDate;
  private Date DemandDate;
}
