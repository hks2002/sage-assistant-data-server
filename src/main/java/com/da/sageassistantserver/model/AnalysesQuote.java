/**********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                             *
 * @CreatedDate           : 2022-09-21 12:32:00                                                                       *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                             *
 * @LastEditDate          : 2024-12-25 14:35:47                                                                       *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                           *
 *********************************************************************************************************************/

package com.da.sageassistantserver.model;

import com.alibaba.fastjson2.annotation.JSONType;
import java.util.Date;
import lombok.Data;

@Data
@JSONType(alphabetic = false)
public class AnalysesQuote {

  private String QuoteNO;
  private Date QuoteDate;
  private String CustomerCode;
  private String CustomerName;
  private Double NetPrice;
  private String OrderNO;
  private Integer OrderFlag;
  private Integer QTY;
  private Integer ROWNUM;
}
