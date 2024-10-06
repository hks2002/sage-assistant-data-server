/**********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                             *
 * @CreatedDate           : 2022-03-26 17:01:00                                                                       *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                             *
 * @LastEditDate          : 2024-12-25 14:38:12                                                                       *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                           *
 *********************************************************************************************************************/

package com.da.sageassistantserver.model;

import com.alibaba.fastjson2.annotation.JSONType;
import java.math.BigDecimal;
import lombok.Data;

@Data
@JSONType(alphabetic = false)
public class InvoiceHeader {

  private String Facility;
  private String Currency;
  private String InvoiceNO;
  private String CreateDate;
  private String CreateUser;
  private String Note;
  private String InvoiceStatus;
  private String FaPiao;
  private String Customer;
  private String Address;
  private BigDecimal AmountTaxInclude;
  private BigDecimal AmountTaxNotInclude;
  private BigDecimal AmountTax;
  private Float CurrRate;
}
