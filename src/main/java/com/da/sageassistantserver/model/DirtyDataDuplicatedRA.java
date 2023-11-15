/******************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                     *
 * @CreatedDate           : 2022-09-21 09:17:00                               *
 * @LastEditors           : Robert Huang<56649783@qq.com>                     *
 * @LastEditDate          : 2023-11-14 19:28:24                               *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                   *
 *****************************************************************************/

package com.da.sageassistantserver.model;

import lombok.Data;

@Data
public class DirtyDataDuplicatedRA {

    private String ProjectNO;
    private String PN;
    private String ReceiptNO;
    private String ReceiptLine;
    private String PurchaseNO;
    private String PurchaseLine;
    private Integer ReceiptQty;
    private Float ReceiptAmount;
    private String Currency;
    private Integer Req;
}
