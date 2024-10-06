/**********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                             *
 * @CreatedDate           : 2022-03-26 17:01:00                                                                       *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                             *
 * @LastEditDate          : 2024-12-25 14:37:35                                                                       *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                           *
 *********************************************************************************************************************/

package com.da.sageassistantserver.model;

import com.alibaba.fastjson2.annotation.JSONType;
import java.math.BigDecimal;
import lombok.Data;

@Data
@JSONType(alphabetic = false)
public class FinancialMonthBalance {

  private String AccountNO;
  private String Currency;
  private String Year;
  private String Month;
  private BigDecimal M;
  private BigDecimal B;
  private BigDecimal C;
  private BigDecimal D;
}
