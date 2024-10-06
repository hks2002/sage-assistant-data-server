/*********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                            *
 * @CreatedDate           : 2022-06-27 14:02:00                                                                      *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                            *
 * @LastEditDate          : 2023-06-23 20:28:18                                                                      *
 * @FilePath              : src/main/java/com/da/sageassistantserver/model/CurrencyHistory.java                      *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                          *
 ********************************************************************************************************************/

package com.da.sageassistantserver.model;

import com.alibaba.fastjson2.annotation.JSONType;
import java.util.Date;
import lombok.Data;

@Data
@JSONType(alphabetic = false)
public class CurrencyHistory {

  private String Sour;
  private String Dest;
  private Float Rate;
  private Date StartDate;
  private Date EndDate;
}
