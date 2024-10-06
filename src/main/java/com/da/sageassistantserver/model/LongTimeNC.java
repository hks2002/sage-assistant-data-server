/******************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                     *
 * @CreatedDate           : 2022-03-26 17:01:00                               *
 * @LastEditors           : Robert Huang<56649783@qq.com>                     *
 * @LastEditDate          : 2024-09-18 18:17:57                               *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                   *
 *****************************************************************************/

package com.da.sageassistantserver.model;

import com.alibaba.fastjson2.annotation.JSONType;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

@Data
@JSONType(alphabetic = false)
public class LongTimeNC {

  private String ProjectNO;
  private String PurchaseNO;
  private String Line;
  private String VendorCode;
  private String VendorName;
  private String PN;
  private Integer Qty;
  private String Unit;
  private String Description;
  private BigDecimal NetPrice;
  private String Currency;
  private String CreateUser;
  private Date OrderDate;
  private Date FirstNCDate;
}
