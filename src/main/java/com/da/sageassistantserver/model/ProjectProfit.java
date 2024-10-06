/**********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                             *
 * @CreatedDate           : 2022-03-26 17:01:00                                                                       *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                             *
 * @LastEditDate          : 2024-12-25 14:40:09                                                                       *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                           *
 *********************************************************************************************************************/

package com.da.sageassistantserver.model;

import com.alibaba.fastjson2.annotation.JSONType;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

@Data
@JSONType(alphabetic = false)
public class ProjectProfit {

  private String ProjectNO;
  private String OrderNO;
  private String PN;
  private String Description;
  private String CategoryCode;
  private Integer Qty;
  private String SalesCurrency;
  private String LocalCurrency;
  private Float Rate;
  private BigDecimal ProjectSalesPrice;
  private BigDecimal ProjectSalesLocalPrice;
  private BigDecimal ProjectLocalCost;
  private Date OrderDate;
  private BigDecimal Profit;
}
