/**********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                             *
 * @CreatedDate           : 2024-06-02 21:34:24                                                                       *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                             *
 * @LastEditDate          : 2024-12-09 19:51:19                                                                       *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                           *
 *********************************************************************************************************************/

package com.da.sageassistantserver.service;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.da.sageassistantserver.dao.UserFuncMapper;
import com.da.sageassistantserver.model.User;
import com.da.sageassistantserver.model.UserFunc;
import com.da.sageassistantserver.utils.ResponseJsonHelper;
import com.da.sageassistantserver.utils.ResponseJsonHelper.MsgTyp;
import com.da.sageassistantserver.utils.Utils;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserFuncService {

  @Autowired
  UserFuncMapper userFuncMapper;

  @Autowired
  UserService userService;

  /**
   * Adds a new user function to the database and returns a JSONObject indicating
   * the success or failure of the operation.
   *
   * @param userFunc the UserFunc object containing the user function's
   *                 information
   * @return a JSONObject with a boolean "success" field indicating the success or
   *         failure of the operation,
   *         and a "type" field indicating the type of message (ERROR or RESULT)
   */
  public JSONObject addUserFunc(UserFunc userFunc) {
    userFunc.setCreate_at(new Timestamp(System.currentTimeMillis()));
    userFunc.setUpdate_at(new Timestamp(System.currentTimeMillis()));
    userFunc.setCreate_by(0L);
    userFunc.setUpdate_by(0L);

    if (userFuncMapper.insert(userFunc) == 0) {
      return ResponseJsonHelper.rtnObj(
        false,
        MsgTyp.ERROR,
        "User Func add failed."
      );
    } else {
      return ResponseJsonHelper.rtnObj(true, MsgTyp.RESULT, "success");
    }
  }

  /**
   * Adds a new user function to the database for the given sage_id, func_system,
   * func_code,
   * func_name, and enable status.
   *
   * @param sage_id      the sage_id of the user
   * @param func_system  the system of the user function
   * @param func_code    the code of the user function
   * @param func_name    the name of the user function
   * @param func_details the details of the user function
   * @param enable       the enable status of the user function
   * @return a JSONObject indicating the success or failure of the operation
   */
  public JSONObject addUserFuncBySid(
    String sage_id,
    String func_system,
    String func_code,
    String func_name,
    String func_details,
    Boolean enable
  ) {
    User user = userService.getUserBySid(sage_id);

    if (user == null) {
      return ResponseJsonHelper.rtnObj(
        false,
        MsgTyp.ERROR,
        "User not found when add user func by sid."
      );
    }

    UserFunc userFunc = new UserFunc();
    userFunc.setUser_id(user.getId());
    userFunc.setSage_id(sage_id);
    userFunc.setFunc_system(func_system);
    userFunc.setFunc_code(func_code);
    userFunc.setFunc_name(func_name);
    userFunc.setFunc_details(func_details);
    userFunc.setEnable(enable);

    return addUserFunc(userFunc);
  }

  /**
   * Adds a new user function to the system.
   *
   * @param id           the ID of the user
   * @param func_system  the system of the function
   * @param func_code    the code of the function
   * @param func_name    the name of the function
   * @param func_details the details of the user function
   * @param enable       indicates whether the function is enabled or not
   * @return a JSON object indicating the success or failure of the operation
   */
  public JSONObject addUserFuncByUid(
    Long id,
    String func_system,
    String func_code,
    String func_name,
    String func_details,
    Boolean enable
  ) {
    User user = userService.getUserByUid(id);

    if (user == null) {
      return ResponseJsonHelper.rtnObj(
        false,
        MsgTyp.ERROR,
        "User not found when add user func by uid."
      );
    }

    UserFunc userFunc = new UserFunc();
    userFunc.setUser_id(id);
    userFunc.setSage_id(user.getSage_id());
    userFunc.setFunc_system(func_system);
    userFunc.setFunc_code(func_code);
    userFunc.setFunc_name(func_name);
    userFunc.setFunc_details(func_details);
    userFunc.setEnable(enable);

    return addUserFunc(userFunc);
  }

  /**
   * Deletes a user function by its ID and returns a JSON object indicating the
   * success or failure of the operation.
   *
   * @param id the ID of the user function to be deleted
   * @return a JSON object indicating the success or failure of the operation
   */
  public JSONObject deleteUserFuncById(Long id) {
    return userFuncMapper.deleteById(id) == 0
      ? ResponseJsonHelper.rtnObj(
        false,
        MsgTyp.ERROR,
        "User function delete failed."
      )
      : ResponseJsonHelper.rtnObj(true, MsgTyp.RESULT, "success");
  }

  public JSONObject deleteUserFuncBySid(String sid) {
    LambdaQueryWrapper<UserFunc> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(UserFunc::getSage_id, sid);

    return userFuncMapper.delete(queryWrapper) == 0
      ? ResponseJsonHelper.rtnObj(
        false,
        MsgTyp.ERROR,
        "User function delete failed."
      )
      : ResponseJsonHelper.rtnObj(true, MsgTyp.RESULT, "success");
  }

  public JSONObject deleteUserFuncByUid(Long uid) {
    LambdaQueryWrapper<UserFunc> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(UserFunc::getUser_id, uid);

    return userFuncMapper.delete(queryWrapper) == 0
      ? ResponseJsonHelper.rtnObj(
        false,
        MsgTyp.ERROR,
        "User function delete failed."
      )
      : ResponseJsonHelper.rtnObj(true, MsgTyp.RESULT, "success");
  }

  public JSONObject updateUserFunc(
    UserFunc updateUserFunc,
    LambdaUpdateWrapper<UserFunc> updateWrapper
  ) {
    updateUserFunc.setUpdate_at(new Timestamp(System.currentTimeMillis()));
    updateUserFunc.setUpdate_by(0L);
    return userFuncMapper.update(updateUserFunc, updateWrapper) <= 0
      ? ResponseJsonHelper.rtnObj(
        false,
        MsgTyp.ERROR,
        "User function update failed."
      )
      : ResponseJsonHelper.rtnObj(true, MsgTyp.RESULT, "success");
  }

  public JSONObject updateSageActionsBySid(String sid, String functions) {
    LambdaUpdateWrapper<UserFunc> updateWrapper = new LambdaUpdateWrapper<>();
    updateWrapper.eq(UserFunc::getSage_id, sid);

    UserFunc userFunc = new UserFunc();
    userFunc.setSage_id(sid);
    userFunc.setFunc_system("SAGE");
    userFunc.setFunc_code("ACTIONS");
    userFunc.setFunc_details(functions);
    userFunc.setFunc_name("functions in sage");
    userFunc.setEnable(true);

    return updateUserFunc(userFunc, updateWrapper);
  }

  public JSONObject updateSageActionsByAuth(String auth, String functions) {
    User user = userService.getUserByAuth(auth);

    if (user == null) {
      return ResponseJsonHelper.rtnObj(
        false,
        MsgTyp.ERROR,
        "User not found when update Sage actions."
      );
    }

    String sid = user.getSage_id();
    LambdaQueryWrapper<UserFunc> queryWrapper2 = new LambdaQueryWrapper<>();
    queryWrapper2.eq(UserFunc::getSage_id, sid);
    UserFunc userFunc2 = userFuncMapper.selectOne(queryWrapper2);

    if (userFunc2 == null) {
      return addUserFuncBySid(
        sid,
        "SAGE",
        "ACTIONS",
        "functions in sage",
        functions,
        true
      );
    }

    return updateSageActionsBySid(sid, functions);
  }

  /**
   * Retrieves the profile information for a given authentication token.
   *
   * @param auth the authentication token
   * @return a JSON object containing the profile information, including the list
   *         of functions
   */
  public JSONObject getSageActionsByAuth(String auth) {
    User user = userService.getUserByAuth(auth);

    if (user == null) {
      return ResponseJsonHelper.rtnObj(
        false,
        MsgTyp.ERROR,
        "User not found when get Sage actions."
      );
    }

    return getSageActionsBySid(user.getSage_id());
  }

  /**
   * Retrieves the functions associated with a given sid.
   *
   * @param sid the sage id of the user
   * @return a JSON object containing the list of functions
   */
  public JSONObject getSageActionsBySid(String sid) {
    List<String> functionsList = userFuncMapper.findSageActionsBySid(sid);
    String[] functions = Utils.ListToString(functionsList).split(";");
    List<String> finalFunctions = new ArrayList<>();
    for (int i = 0; i < functions.length; i++) {
      finalFunctions.add(functions[i]);
    }

    JSONObject rtn = ResponseJsonHelper.rtnObj(true, MsgTyp.RESULT, "success");
    rtn.put("functions", finalFunctions);
    return rtn;
  }

  public List<UserFunc> getWebDavAccess(String login_name) {
    User user = userService.getUserByLoginName(login_name);

    if (user == null) {
      return new ArrayList<>();
    }

    LambdaQueryWrapper<UserFunc> queryWrapper2 = new LambdaQueryWrapper<>();
    queryWrapper2.eq(UserFunc::getUser_id, user.getId());
    queryWrapper2.eq(UserFunc::getFunc_system, "WEBDAV");
    return userFuncMapper.selectList(queryWrapper2);
  }

  public boolean initWebDavAccessByLoginName(String login_name) {
    User user = userService.getUserByLoginName(login_name);

    if (user == null) {
      return false;
    }

    LambdaQueryWrapper<UserFunc> queryWrapper2 = new LambdaQueryWrapper<>();
    queryWrapper2.eq(UserFunc::getFunc_system, "WEBDAV");
    queryWrapper2.eq(UserFunc::getUser_id, user.getId());
    List<UserFunc> userFuncs = userFuncMapper.selectList(queryWrapper2);
    if (userFuncs.size() > 0) {
      return true;
    } else {
      UserFunc userFunc = new UserFunc();

      userFunc.setUser_id(user.getId());
      userFunc.setFunc_system("WEBDAV");
      userFunc.setFunc_code("WRITE");
      userFunc.setFunc_name("write webdav");
      userFunc.setEnable(false);
      userFuncMapper.insert(userFunc);

      userFunc = new UserFunc();
      userFunc.setUser_id(user.getId());
      userFunc.setFunc_system("WEBDAV");
      userFunc.setFunc_code("READ");
      userFunc.setFunc_name("read webdav");
      userFunc.setEnable(true);
      userFuncMapper.insert(userFunc);

      userFuncs = userFuncMapper.selectList(queryWrapper2);
      return userFuncs.size() > 0;
    }
  }

  public List<UserFunc> getFunctions(String system, String code) {
    LambdaQueryWrapper<UserFunc> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(UserFunc::getFunc_system, system);
    queryWrapper.eq(UserFunc::getFunc_code, code);
    return userFuncMapper.selectList(queryWrapper);
  }

  public List<UserFunc> getFunctions(String system) {
    LambdaQueryWrapper<UserFunc> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(UserFunc::getFunc_system, system);
    return userFuncMapper.selectList(queryWrapper);
  }
}
