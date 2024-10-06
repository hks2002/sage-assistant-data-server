/*********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                            *
 * @CreatedDate           : 2022-03-26 17:01:00                                                                      *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                            *
 * @LastEditDate          : 2023-06-23 22:03:52                                                                      *
 * @FilePath              : src/main/java/com/da/sageassistantserver/model/CostHistory.java                          *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                          *
 ********************************************************************************************************************/

package com.da.sageassistantserver.model;

import com.alibaba.fastjson2.annotation.JSONType;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

@Data
@JSONType(alphabetic = false)
public class CostHistory {

  private String PurchaseSite;
  private String ProjectNO;
  private String OrderPN;
  private Date OrderDate;
  private String PurchaseNO;
  private String Line;
  private String VendorCode;
  private String VendorName;
  private String PurchasePN;
  private String Description;
  private String Currency;
  private BigDecimal NetPrice;
  private Integer Qty;
  private BigDecimal USD;
  private Float Rate;
}
