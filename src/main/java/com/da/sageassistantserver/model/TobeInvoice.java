/**********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                             *
 * @CreatedDate           : 2022-03-26 17:01:00                                                                       *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                             *
 * @LastEditDate          : 2024-12-09 19:46:01                                                                       *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                           *
 *********************************************************************************************************************/

package com.da.sageassistantserver.model;

import com.alibaba.fastjson2.annotation.JSONType;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

@Data
@JSONType(alphabetic = false)
public class TobeInvoice {

  private String PurchaseNO;
  private String PurchaseLine;
  private String ProjectNO;
  private Date PurchaseDate;
  private String Purchaser;
  private String VendorCode;
  private String VendorName;
  private String PN;
  private String Description;
  private Integer Qty;
  private String Unit;
  private BigDecimal Price;
  private String Currency;
  private String ReceiveNO;
  private String ReceiveLine;
  private Date ReceiveDate;
  private String Receiptor;
}
