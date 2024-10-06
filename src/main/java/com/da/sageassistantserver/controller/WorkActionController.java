/**********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                             *
 * @CreatedDate           : 2024-09-28 09:12:34                                                                       *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                             *
 * @LastEditDate          : 2024-12-25 14:32:24                                                                       *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                           *
 *********************************************************************************************************************/

package com.da.sageassistantserver.controller;

import com.alibaba.fastjson2.JSONObject;
import com.da.sageassistantserver.model.User;
import com.da.sageassistantserver.model.WorkAction;
import com.da.sageassistantserver.service.UserService;
import com.da.sageassistantserver.service.WorkActionService;
import com.da.sageassistantserver.utils.ResponseJsonHelper;
import com.da.sageassistantserver.utils.ResponseJsonHelper.MsgTyp;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class WorkActionController {

  @Autowired
  private WorkActionService workActionService;

  @Autowired
  private UserService userService;

  @GetMapping("/Data/WorkAction")
  public List<WorkAction> getWorkAction(
    @RequestParam(
      value = "Id",
      required = false,
      defaultValue = "-1"
    ) String Id,
    @RequestParam(
      value = "Site",
      required = false,
      defaultValue = ""
    ) String Site,
    @RequestParam(
      value = "Type",
      required = false,
      defaultValue = ""
    ) String Type,
    @RequestParam(
      value = "ProjectNO",
      required = false,
      defaultValue = ""
    ) String ProjectNO,
    @RequestParam(
      value = "User",
      required = false,
      defaultValue = ""
    ) String User,
    @RequestParam(
      value = "DateFrom",
      required = false,
      defaultValue = "2099-12-31"
    ) String DateFrom,
    @RequestParam(
      value = "DateTo",
      required = false,
      defaultValue = "1901-01-01"
    ) String DateTo
  ) {
    return workActionService.getWorkAction(
      Id,
      Site,
      Type,
      ProjectNO,
      User,
      DateFrom,
      DateTo
    );
  }

  @PutMapping("/Data/WorkAction")
  public JSONObject updateWorkAction(
    @RequestHeader(value = "authorization", required = false) String Auth,
    @RequestBody WorkAction workAction
  ) {
    User user = userService.getUserByAuth(Auth);
    if (user == null) {
      return ResponseJsonHelper.missingAuth();
    }

    workAction.setUpdate_by(user.getId());
    if (workAction.getId() == null) {
      workAction.setCreate_by(user.getId());
    }
    if (workAction.getCreate_by() == null) {
      workAction.setCreate_by(user.getId());
    }

    if (!workAction.getCreate_by().equals(user.getId())) {
      return ResponseJsonHelper.rtnObj(
        false,
        MsgTyp.RESULT,
        "You are not the owner of this Work Action."
      );
    }

    boolean rtn = workActionService.updateWorkAction(workAction);

    if (rtn) {
      return ResponseJsonHelper.rtnObj(
        true,
        MsgTyp.RESULT,
        "Update work action success"
      );
    } else {
      return ResponseJsonHelper.rtnObj(
        false,
        MsgTyp.ERROR,
        "Update work action failed."
      );
    }
  }

  @DeleteMapping("/Data/WorkActionById")
  public JSONObject deleteWorkAction(
    @RequestHeader(value = "authorization", required = false) String Auth,
    @RequestParam(
      value = "ActionId",
      required = false,
      defaultValue = ""
    ) String ActionId
  ) {
    User user = userService.getUserByAuth(Auth);
    if (user == null) {
      return ResponseJsonHelper.missingAuth();
    }
    WorkAction workAction = workActionService.getWorkActionById(
      Long.valueOf(ActionId)
    );

    if (workAction == null) {
      return ResponseJsonHelper.rtnObj(
        false,
        MsgTyp.RESULT,
        "Work Action not found."
      );
    }
    if (
      workAction.getCreate_by() != null &&
      !workAction.getCreate_by().equals(user.getId())
    ) {
      return ResponseJsonHelper.rtnObj(
        false,
        MsgTyp.RESULT,
        "You are not the owner of this Work Action."
      );
    }

    Integer rtn = workActionService.deleteWorkAction(Long.valueOf(ActionId));
    if (rtn.equals(0)) {
      return ResponseJsonHelper.rtnObj(
        false,
        MsgTyp.RESULT,
        "Delete Work Action success"
      );
    } else {
      return ResponseJsonHelper.rtnObj(
        true,
        MsgTyp.ERROR,
        "Delete Work Action failed."
      );
    }
  }
}
