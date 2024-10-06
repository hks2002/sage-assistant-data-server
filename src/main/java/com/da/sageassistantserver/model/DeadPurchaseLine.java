/**********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                             *
 * @CreatedDate           : 2022-03-26 17:01:00                                                                       *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                             *
 * @LastEditDate          : 2024-12-25 14:37:03                                                                       *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                           *
 *********************************************************************************************************************/

package com.da.sageassistantserver.model;

import com.alibaba.fastjson2.annotation.JSONType;
import java.util.Date;
import lombok.Data;

@Data
@JSONType(alphabetic = false)
public class DeadPurchaseLine {

  private String PurchaseNO;
  private String PurchaseLine;
  private String ProjectNO;
  private String PurchasePN;
  private String OrderNO;
  private String SalesPN;
  private Integer Qty;
  private Date OrderDate;
  private Date PurchaseDate;
  private String Purchaser;
}
