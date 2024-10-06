/**********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                             *
 * @CreatedDate           : 2022-03-25 15:19:00                                                                       *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                             *
 * @LastEditDate          : 2024-12-25 14:31:19                                                                       *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                           *
 *********************************************************************************************************************/

package com.da.sageassistantserver.controller;

import com.alibaba.fastjson2.JSONObject;
import com.da.sageassistantserver.service.SageActionService;
import com.da.sageassistantserver.utils.ResponseJsonHelper;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class SageRecorderController {

  @PutMapping("/Data/SageDeliveryPlanDate")
  public JSONObject updateSageDeliveryPlanDate(
    @RequestHeader(value = "authorization", required = false) String Auth,
    @RequestParam(value = "OrderNO", required = false) String OrderNO,
    @RequestParam(value = "Line", required = false) Integer Line,
    @RequestParam(value = "PlanDate", required = false) String PlanDate
  ) {
    if (Auth == null) {
      return ResponseJsonHelper.missingAuth();
    }
    if (OrderNO == null) {
      return ResponseJsonHelper.paraRequired("OrderNO");
    }
    if (Line == null) {
      return ResponseJsonHelper.paraRequired("Line");
    }
    return SageActionService.updateSageField(
      Auth,
      "SalesOrder",
      OrderNO,
      Line,
      "EA33",
      PlanDate
    );
  }

  @PutMapping("/Data/SageProjectStatus")
  public JSONObject updateSageProjectStatus(
    @RequestHeader(value = "authorization", required = false) String Auth,
    @RequestParam(value = "OrderNO", required = false) String OrderNO,
    @RequestParam(value = "Line", required = false) Integer Line,
    @RequestParam(value = "Status", required = false) String Status
  ) {
    if (Auth == null) {
      return ResponseJsonHelper.missingAuth();
    }
    if (OrderNO == null) {
      return ResponseJsonHelper.paraRequired("OrderNO");
    }
    if (Line == null) {
      return ResponseJsonHelper.paraRequired("Line");
    }
    return SageActionService.updateSageField(
      Auth,
      "SalesOrder",
      OrderNO,
      Line,
      "EA72",
      Status
    );
  }

  @PutMapping("/Data/SageProjectBlockReason")
  public JSONObject updateSageProjectBlockReason(
    @RequestHeader(value = "authorization", required = false) String Auth,
    @RequestParam(value = "OrderNO", required = false) String OrderNO,
    @RequestParam(value = "Line", required = false) Integer Line,
    @RequestParam(value = "Reason", required = false) String Reason
  ) {
    if (Auth == null) {
      return ResponseJsonHelper.missingAuth();
    }
    if (OrderNO == null) {
      return ResponseJsonHelper.paraRequired("OrderNO");
    }
    if (Line == null) {
      return ResponseJsonHelper.paraRequired("Line");
    }
    return SageActionService.updateSageField(
      Auth,
      "SalesOrder",
      OrderNO,
      Line,
      "EA73",
      Reason
    );
  }

  @PutMapping("/Data/SagePurchaseAckDate")
  public JSONObject updateSagePurchaseAckDate(
    @RequestHeader(value = "authorization", required = false) String Auth,
    @RequestParam(value = "OrderNO", required = false) String OrderNO,
    @RequestParam(value = "Line", required = false) Integer Line,
    @RequestParam(value = "AckDate", required = false) String AckDate
  ) {
    if (Auth == null) {
      return ResponseJsonHelper.missingAuth();
    }
    if (OrderNO == null) {
      return ResponseJsonHelper.paraRequired("OrderNO");
    }
    if (Line == null) {
      return ResponseJsonHelper.paraRequired("Line");
    }
    return SageActionService.updateSageField(
      Auth,
      "PurchaseOrder",
      OrderNO,
      Line,
      "CA66",
      AckDate
    );
  }
}
