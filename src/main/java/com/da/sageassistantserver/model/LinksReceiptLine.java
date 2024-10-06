/*****************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                    *
 * @CreatedDate           : 2023-11-24 16:53:52                              *
 * @LastEditors           : Robert Huang<56649783@qq.com>                    *
 * @LastEditDate          : 2023-12-07 15:24:29                              *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                  *
 ****************************************************************************/

package com.da.sageassistantserver.model;

import com.alibaba.fastjson2.annotation.JSONType;
import java.sql.Date;
import lombok.Data;

@Data
@JSONType(alphabetic = false)
public class LinksReceiptLine {

  private String ReceiptNO;
  private String ReceiptLine;
  private String ReceiptProjectNO;
  private String ReceiptVendorCode;
  private String ReceiptVendorName;
  private Date ReceiptDate;
  private String ReceiptPN;
  private String ReceiptPNDescription;
  private Float ReceiptQty;
  private String ReceiptUnit;
  private Double ReceiptAmount;
  private String ReceiptCurrency;
}
