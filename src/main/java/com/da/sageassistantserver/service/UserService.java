/**********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                             *
 * @CreatedDate           : 2024-06-02 21:34:24                                                                       *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                             *
 * @LastEditDate          : 2024-12-25 14:49:40                                                                       *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                           *
 *********************************************************************************************************************/

package com.da.sageassistantserver.service;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.da.sageassistantserver.dao.UserMapper;
import com.da.sageassistantserver.model.User;
import com.da.sageassistantserver.utils.ResponseJsonHelper;
import com.da.sageassistantserver.utils.ResponseJsonHelper.MsgTyp;
import com.da.sageassistantserver.utils.Utils;
import java.sql.Timestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  @Autowired
  UserMapper userMapper;

  /**
   * Creates a new user in the database and returns a JSONObject indicating the
   * success or failure of the operation.
   *
   * @param user the User object containing the user's information
   * @return a JSONObject with a boolean "success" field indicating the success or
   *         failure of the operation,
   *         and a "message" field with a corresponding message
   */
  public JSONObject createUser(User user) {
    user.setCreate_at(new Timestamp(System.currentTimeMillis()));
    user.setUpdate_at(new Timestamp(System.currentTimeMillis()));
    user.setCreate_by(0L);
    user.setUpdate_by(0L);

    return userMapper.insert(user) == 0
      ? ResponseJsonHelper.rtnObj(false, MsgTyp.ERROR, "User create failed.")
      : ResponseJsonHelper.rtnObj(true, MsgTyp.RESULT, "success");
  }

  /**
   * Creates a new user with the given information and returns a JSON object
   * representing the result.
   *
   * @param sage_id    the unique identifier for the user
   * @param login_name the login name for the user
   * @param first_name the first name of the user
   * @param last_name  the last name of the user
   * @param email      the email address of the user
   * @param language   the preferred language of the user
   * @return a JSON object representing the result of creating the user
   */
  public JSONObject createUser(
    String sage_id,
    String login_name,
    String first_name,
    String last_name,
    String email,
    String language
  ) {
    User user = new User();
    user.setSage_id(sage_id);
    user.setLogin_name(login_name);
    user.setFirst_name(first_name);
    user.setLast_name(last_name);
    user.setEmail(email);
    user.setLanguage(language);

    return createUser(user);
  }

  /**
   * Deletes a user by their ID.
   *
   * @param uid the ID of the user to delete
   * @return a JSONObject indicating the success or failure of the deletion
   */
  public JSONObject deleteUserById(Long uid) {
    return userMapper.deleteById(uid) == 0
      ? ResponseJsonHelper.rtnObj(false, MsgTyp.ERROR, "User delete failed.")
      : ResponseJsonHelper.rtnObj(true, MsgTyp.RESULT, "success");
  }

  /**
   * Deletes a user from the database based on the provided Sage ID.
   *
   * @param sid the Sage ID of the user to be deleted
   * @return a JSON object indicating the success or failure of the deletion
   *         operation
   */
  public JSONObject deleteUserBySid(String sid) {
    LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(User::getSage_id, sid);

    return userMapper.delete(queryWrapper) == 0
      ? ResponseJsonHelper.rtnObj(false, MsgTyp.ERROR, "User delete failed.")
      : ResponseJsonHelper.rtnObj(true, MsgTyp.RESULT, "success");
  }

  public User getUserByUid(Long id) {
    LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(User::getId, id);
    return userMapper.selectOne(queryWrapper);
  }

  public User getUserBySid(String sid) {
    LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(User::getSage_id, sid);
    return userMapper.selectOne(queryWrapper);
  }

  public User getUserByLoginName(String login_name) {
    LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(User::getLogin_name, login_name);
    return userMapper.selectOne(queryWrapper);
  }

  public User getUserByAuth(String auth) {
    String[] loginInfo = Utils.decodeBasicAuth(auth).split(":");
    return (loginInfo.length > 1)
      ? userMapper.findUserByLoginName(loginInfo[0])
      : null;
  }

  public JSONObject updateUserByWrapper(
    User user,
    LambdaUpdateWrapper<User> wrapper
  ) {
    user.setUpdate_at(new Timestamp(System.currentTimeMillis()));
    user.setUpdate_by(0L);
    return userMapper.update(user, wrapper) <= 0
      ? ResponseJsonHelper.rtnObj(false, MsgTyp.ERROR, "User update failed.")
      : ResponseJsonHelper.rtnObj(true, MsgTyp.RESULT, "success");
  }

  public JSONObject updateUserBySid(
    String sage_id,
    String login_name,
    String first_name,
    String last_name,
    String email,
    String language
  ) {
    LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
    updateWrapper.eq(User::getSage_id, sage_id);

    User user = new User();
    user.setLogin_name(login_name);
    user.setFirst_name(first_name);
    user.setLast_name(last_name);
    user.setEmail(email);
    user.setLanguage(language);
    return updateUserByWrapper(user, updateWrapper);
  }

  public JSONObject updateUserByLoginName(
    String login_name,
    String sage_id,
    String first_name,
    String last_name,
    String email,
    String language
  ) {
    LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
    updateWrapper.eq(User::getLogin_name, login_name);

    User user = new User();
    user.setSage_id(sage_id);
    user.setFirst_name(first_name);
    user.setLast_name(last_name);
    user.setEmail(email);
    user.setLanguage(language);
    return updateUserByWrapper(user, updateWrapper);
  }

  /**
   * Retrieves the profile information of a user based on the provided
   * authentication token.
   *
   * @param auth the authentication token of the user
   * @return a JSON object containing the user's profile information, including
   *         user ID, first name, last name, full name, email, and language,
   *         or an error message if the user is not found
   */
  public JSONObject getProfileByAuth(String auth) {
    User user = getUserByAuth(auth);

    if (user == null) {
      return ResponseJsonHelper.rtnObj(
        false,
        MsgTyp.ERROR,
        "User not found when get profile."
      );
    }

    JSONObject profile = new JSONObject();
    profile.put("userId", user.getSage_id());
    profile.put("loginName", user.getLogin_name());
    profile.put("firstName", user.getFirst_name());
    profile.put("lastName", user.getLast_name());
    profile.put("userName", user.getFirst_name() + " " + user.getLast_name());
    profile.put("email", user.getEmail());
    profile.put("language", user.getLanguage());

    JSONObject rtn = ResponseJsonHelper.rtnObj(true, MsgTyp.RESULT, "success");
    rtn.put("profile", profile);
    return rtn;
  }
}
