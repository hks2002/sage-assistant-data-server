/**********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                             *
 * @CreatedDate           : 2022-06-09 13:13:00                                                                       *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                             *
 * @LastEditDate          : 2024-12-25 14:42:59                                                                       *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                           *
 *********************************************************************************************************************/

package com.da.sageassistantserver.model;

import com.alibaba.fastjson2.annotation.JSONType;
import java.util.Date;
import java.util.List;
import lombok.Data;

@Data
@JSONType(alphabetic = false)
public class TobeTrackingSalesOrderLine {

  private Integer ItemNO;
  private String Site;
  private String OrderNO;
  private String OrderLine;
  private String TrackingNO;
  private String ProjectNO;
  private String OriProjectNO;
  private String OrderType;
  private String OrderPN;
  private String OrderPNVersion;
  private String OrderPNDesc;
  private String Mark;
  private String Paint;
  private String RequireManual;
  private String RequireTest;
  private String RequireCalibration;
  private Integer OrderQty;
  private Float OrderPrice;
  private String OrderCurrency;
  private String CustomerCode;
  private String CustomerName;
  private Date OrderDate;
  private String Priority;
  private String PriorityCode;
  private Date OrderRequestDate;
  private Date OrderPlanedDate;
  private String ProjectStatusCode;
  private String ProjectStatus;
  private String ProjectBlockReasonCode;
  private String ProjectBlockReason;
  private Integer DaysLeft;
  private Integer TQCCntPass;
  private Integer TQCCntNC;
  private Integer IQCCntPass;
  private Integer IQCCntNC;
  private Integer FQCCntPass;
  private Integer FQCCntNC;
  private Integer PCKCntPass;
  private Integer PCKCntNC;
  private List<TobeTrackingNCLine> NC;
  private LineNote AssignToLineNote;
  private LineNote StatusLineNote;
}
