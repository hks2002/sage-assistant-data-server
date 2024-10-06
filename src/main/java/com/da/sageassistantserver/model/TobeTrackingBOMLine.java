/**********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                             *
 * @CreatedDate           : 2022-11-10 14:18:00                                                                       *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                             *
 * @LastEditDate          : 2024-12-09 19:45:44                                                                       *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                           *
 *********************************************************************************************************************/

package com.da.sageassistantserver.model;

import java.util.Date;

import com.alibaba.fastjson2.annotation.JSONType;

import lombok.Data;

@Data
@JSONType(alphabetic = false)
public class TobeTrackingBOMLine {

    private String Site;
    private String BomProjectNO;
    private String WorkOrderNO;
    private String BomLine;
    private String BomPN;
    private String StockPN;
    private String BomDesc;
    private Integer BomQty;
    private String BomUnit;
    private Integer AvaQty;
    private Integer AllQty;
    private Integer ShortQty;
    private String Creator;
    private Date BomRequestDate;
    private LineNote LineNote;
}
