/**********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                             *
 * @CreatedDate           : 2022-03-31 16:19:00                                                                       *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                             *
 * @LastEditDate          : 2024-12-25 14:36:44                                                                       *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                           *
 *********************************************************************************************************************/

package com.da.sageassistantserver.model;

import com.alibaba.fastjson2.annotation.JSONType;
import java.util.Date;
import lombok.Data;

@Data
@JSONType(alphabetic = false)
public class CustomerOrder {

  private String Site;
  private String CustomerCode;
  private String OrderNO;
  private String ProjectNO;
  private String PN;
  private String Description;
  private Integer Qty;
  private Date OrderDate;
  private Date OrderRequestDate;
  private Date OrderPlanedDate;
  private String LastDeliveryNO;
  private Integer TotalDeliveryQty;
  private Date LastShipDate;
  private Integer DaysDelay;
}
