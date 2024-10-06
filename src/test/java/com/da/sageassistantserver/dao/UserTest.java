/******************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                     *
 * @CreatedDate           : 2024-06-02 21:40:29                               *
 * @LastEditors           : Robert Huang<56649783@qq.com>                     *
 * @LastEditDate          : 2024-06-24 23:27:20                               *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                   *
 *****************************************************************************/

package com.da.sageassistantserver.dao;

import com.da.sageassistantserver.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * This test class is used to test the UserMapper interface.
 * and check if the user is created successfully.
 * and check if multiple database operations are successful.
 */
@SpringBootTest
public class UserTest {

  @Autowired
  UserMapper userMapper;

  @Test
  void testCreateUser() {
    User user = new User();
    user.setSage_id("sage_id");
    user.setFirst_name("first_name");
    user.setLast_name("last_name");
    user.setLogin_name("login_name");
    user.setEmail("email");
    user.setLanguage("En-US");
    Assertions.assertEquals(1, userMapper.insert(user));
  }
}
