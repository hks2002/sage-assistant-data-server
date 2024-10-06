/**********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                             *
 * @CreatedDate           : 2024-09-28 09:12:34                                                                       *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                             *
 * @LastEditDate          : 2024-12-25 14:30:44                                                                       *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                           *
 *********************************************************************************************************************/

package com.da.sageassistantserver.controller;

import com.alibaba.fastjson2.JSONObject;
import com.da.sageassistantserver.model.LineNote;
import com.da.sageassistantserver.model.User;
import com.da.sageassistantserver.service.LineNoteService;
import com.da.sageassistantserver.service.UserService;
import com.da.sageassistantserver.utils.ResponseJsonHelper;
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
public class LineNoteController {

  @Autowired
  private LineNoteService lineNoteService;

  @Autowired
  private UserService userService;

  @GetMapping("/Data/LineNotes")
  public List<LineNote> getLineNote(
    @RequestParam(
      value = "Id",
      required = false,
      defaultValue = "-1"
    ) String Id,
    @RequestParam(
      value = "ProjectNO",
      required = false,
      defaultValue = ""
    ) String ProjectNO,
    @RequestParam(
      value = "Line",
      required = false,
      defaultValue = ""
    ) String Line
  ) {
    return lineNoteService.getLineNote(Id, ProjectNO, Line);
  }

  @PutMapping("/Data/LineNote")
  public JSONObject updateLineNote(
    @RequestHeader(value = "authorization", required = false) String Auth,
    @RequestBody LineNote lineNote
  ) {
    User user = userService.getUserByAuth(Auth);
    if (user == null) {
      return ResponseJsonHelper.missingAuth();
    }
    lineNote.setUpdate_by(user.getId());
    lineNote.setNote_user(user.getFirst_name() + " " + user.getLast_name());
    if (lineNote.getId() == null) {
      lineNote.setCreate_by(user.getId());
    }

    boolean rtn = lineNoteService.updateLineNote(lineNote);

    if (rtn) {
      return ResponseJsonHelper.rtnObj(
        true,
        ResponseJsonHelper.MsgTyp.RESULT,
        "Update line note success"
      );
    } else {
      return ResponseJsonHelper.rtnObj(
        false,
        ResponseJsonHelper.MsgTyp.ERROR,
        "Update line note failed."
      );
    }
  }

  @DeleteMapping("/Data/LineNoteById")
  public JSONObject deleteLineNote(
    @RequestHeader(value = "authorization", required = false) String Auth,
    @RequestParam(
      value = "NoteId",
      required = false,
      defaultValue = ""
    ) String NoteId
  ) {
    User user = userService.getUserByAuth(Auth);
    if (user == null) {
      return ResponseJsonHelper.missingAuth();
    }
    LineNote lineNote = lineNoteService.getLineNoteById(Long.valueOf(NoteId));

    if (lineNote == null) {
      return ResponseJsonHelper.rtnObj(
        false,
        ResponseJsonHelper.MsgTyp.RESULT,
        "Line note not found."
      );
    }
    if (!lineNote.getCreate_by().equals(user.getId())) {
      return ResponseJsonHelper.rtnObj(
        false,
        ResponseJsonHelper.MsgTyp.RESULT,
        "You are not the owner of this line note."
      );
    }

    Integer rtn = lineNoteService.deleteLineNoteById(Long.valueOf(NoteId));
    if (rtn == 0) {
      return ResponseJsonHelper.rtnObj(
        true,
        ResponseJsonHelper.MsgTyp.RESULT,
        "Delete line note failed."
      );
    } else {
      return ResponseJsonHelper.rtnObj(
        false,
        ResponseJsonHelper.MsgTyp.ERROR,
        "Delete line note success"
      );
    }
  }
}
