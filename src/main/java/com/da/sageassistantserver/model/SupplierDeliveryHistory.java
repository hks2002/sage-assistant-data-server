/**********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                             *
 * @CreatedDate           : 2022-03-26 17:01:00                                                                       *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                             *
 * @LastEditDate          : 2024-12-25 14:40:44                                                                       *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                           *
 *********************************************************************************************************************/

package com.da.sageassistantserver.model;

import com.alibaba.fastjson2.annotation.JSONType;
import java.util.Date;
import lombok.Data;

@Data
@JSONType(alphabetic = false)
public class SupplierDeliveryHistory {

  private String Site;
  private String SupplierCode;
  private String PurchaseNO;
  private String ProjectNO;
  private String PN;
  private String Description;
  private Date AckDate;
  private Date ExpectDate;
  private Date OrderDate;
  private String ReceiptNO;
  private Date ReceiptDate;
  private int DaysNeed;
}
