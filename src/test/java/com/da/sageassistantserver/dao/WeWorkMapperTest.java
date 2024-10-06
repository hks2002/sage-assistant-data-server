/**********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                             *
 * @CreatedDate           : 2023-03-12 22:37:34                                                                       *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                             *
 * @LastEditDate          : 2024-12-25 14:51:39                                                                       *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                           *
 *********************************************************************************************************************/

package com.da.sageassistantserver.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.da.sageassistantserver.model.WeworkRobot;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class WeWorkMapperTest {

  @Autowired
  WeworkRobotMapper mapper;

  @Test
  void test() {
    mapper
      .selectList(
        (new LambdaQueryWrapper<WeworkRobot>()).eq(
            WeworkRobot::getNotice_code,
            "INACTIVE_PN"
          )
          .like(WeworkRobot::getSites, "ZHU")
          .eq(WeworkRobot::getEnable, 1)
      )
      .forEach(r -> {
        log.info(r.getRobot_uuid());
      });
  }
}
