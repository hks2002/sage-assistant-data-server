/**********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                             *
 * @CreatedDate           : 2022-03-26 17:01:00                                                                       *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                             *
 * @LastEditDate          : 2024-12-25 14:41:56                                                                       *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                           *
 *********************************************************************************************************************/

package com.da.sageassistantserver.model;

import com.alibaba.fastjson2.annotation.JSONType;
import lombok.Data;

@Data
@JSONType(alphabetic = false)
public class SupplierSummaryAmountTop {

  private String Site;
  private String SupplierCode;
  private String SupplierName;
  private Float SiteAllUSD;
  private Float SumUSD;
  private Float SumLocal;
  private String LocalCurrency;
}
