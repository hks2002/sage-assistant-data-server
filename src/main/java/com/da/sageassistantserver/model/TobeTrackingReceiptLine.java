/**********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                             *
 * @CreatedDate           : 2022-11-10 14:18:00                                                                       *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                             *
 * @LastEditDate          : 2024-12-09 19:45:20                                                                       *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                           *
 *********************************************************************************************************************/

package com.da.sageassistantserver.model;

import com.alibaba.fastjson2.annotation.JSONType;
import java.util.Date;
import lombok.Data;

@Data
@JSONType(alphabetic = false)
public class TobeTrackingReceiptLine {

  private String Site;
  private String ReceiptNO;
  private String ReceiptLine;
  private String ReceiptPurchaseNO;
  private String ReceiptPurchaseLine;
  private Date ReceiptDate;
  private String Receiptor;
  private Integer ReceiptQty;
}
