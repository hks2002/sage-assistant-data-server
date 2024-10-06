/**********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                             *
 * @CreatedDate           : 2022-03-26 17:01:00                                                                       *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                             *
 * @LastEditDate          : 2024-12-25 14:36:02                                                                       *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                           *
 *********************************************************************************************************************/

package com.da.sageassistantserver.model;

import com.alibaba.fastjson2.annotation.JSONType;
import lombok.Data;

@Data
@JSONType(alphabetic = false)
public class AnalysesQuoteSalesCost {

  private String PN;
  private String Description;
  private String Site;
  private Integer QCnt;
  private Integer QQty;
  private Double MinQPrice;
  private Double AvgQPrice;
  private Double MaxQPrice;
  private Double LastQPrice1;
  private Double LastQPrice2;
  private Double LastQPrice3;
  private Double LastQPrice4;
  private Double LastQPrice5;
  private Double LastQPrice6;
  private Double LastQPrice7;
  private Double LastQPrice8;
  private Double LastQPrice9;
  private Double LastQPrice10;
  private Integer SCnt;
  private Integer SQty;
  private Double MinSPrice;
  private Double AvgSPrice;
  private Double MaxSPrice;
  private Double LastSPrice1;
  private Double LastSPrice2;
  private Double LastSPrice3;
  private Double LastSPrice4;
  private Double LastSPrice5;
  private Double LastSPrice6;
  private Double LastSPrice7;
  private Double LastSPrice8;
  private Double LastSPrice9;
  private Double LastSPrice10;
  private String LastPJT1;
  private String LastPJT2;
  private String LastPJT3;
  private String LastPJT4;
  private String LastPJT5;
  private String LastPJT6;
  private String LastPJT7;
  private String LastPJT8;
  private String LastPJT9;
  private String LastPJT10;
  private Double LastCost1;
  private Double LastCost2;
  private Double LastCost3;
  private Double LastCost4;
  private Double LastCost5;
  private Double LastCost6;
  private Double LastCost7;
  private Double LastCost8;
  private Double LastCost9;
  private Double LastCost10;
}
