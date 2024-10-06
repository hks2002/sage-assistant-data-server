/**********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                             *
 * @CreatedDate           : 2022-03-26 17:01:00                                                                       *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                             *
 * @LastEditDate          : 2024-12-25 14:41:07                                                                       *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                           *
 *********************************************************************************************************************/

package com.da.sageassistantserver.model;

import com.alibaba.fastjson2.annotation.JSONType;
import java.util.Date;
import lombok.Data;

@Data
@JSONType(alphabetic = false)
public class SupplierNCHistoryTiny {

  private String Site;
  private String SupplierCode;
  private String NCNo;
  private String ProjectCode;
  private Integer Req;
  private Date Date;
  private String Cat;
  private String Type;
  private String Cri;
  private String Des;
  private String CreatedBy;
}
