/*****************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                    *
 * @CreatedDate           : 2023-11-24 16:53:52                              *
 * @LastEditors           : Robert Huang<56649783@qq.com>                    *
 * @LastEditDate          : 2023-11-30 10:23:35                              *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                  *
 ****************************************************************************/

package com.da.sageassistantserver.model;

import com.alibaba.fastjson2.annotation.JSONType;
import java.sql.Date;
import lombok.Data;

@Data
@JSONType(alphabetic = false)
public class LinksDeliveryLine {

  private String DeliveryNO;
  private String DeliveryLine;
  private String DeliveryProjectNO;
  private Date DeliveryDate;
  private String DeliveryPN;
  private String DeliveryPNDescription;
  private Float DeliveryQty;
  private String DeliveryUnit;
}
