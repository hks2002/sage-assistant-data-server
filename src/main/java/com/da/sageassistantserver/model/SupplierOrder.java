/**********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                             *
 * @CreatedDate           : 2022-03-26 17:01:00                                                                       *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                             *
 * @LastEditDate          : 2024-12-25 14:41:17                                                                       *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                           *
 *********************************************************************************************************************/

package com.da.sageassistantserver.model;

import com.alibaba.fastjson2.annotation.JSONType;
import java.util.Date;
import lombok.Data;

@Data
@JSONType(alphabetic = false)
public class SupplierOrder {

  private Integer ItemNO;
  private String Site;
  private String SupplierCode;
  private String PurchaseNO;
  private String PurchaseLine;
  private String ProjectNO;
  private String PN;
  private String Description;
  private Integer Qty;
  private Float Price;
  private String Currency;
  private Float LocalPrice;
  private String LocalCurrency;
  private Date OrderDate;
  private Date AckDate;
  private Date ExpectDate;
  private Date LastReceiveDate;
  private Integer TotalReceiveQty;
  private Integer DaysDelay;
}
