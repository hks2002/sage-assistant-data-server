/**********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                             *
 * @CreatedDate           : 2023-03-12 21:46:07                                                                       *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                             *
 * @LastEditDate          : 2024-12-25 14:52:13                                                                       *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                           *
 *********************************************************************************************************************/

package com.da.sageassistantserver.dao;

import com.da.sageassistantserver.model.SuspectDuplicatedPO;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class SuspectDuplicatedDataMapperTest {

  @Autowired
  private SuspectDuplicateDataMapper suspectDuplicateDataMapper;

  @Test
  void test1() {
    suspectDuplicateDataMapper.findDuplicatedRABySite("ZHU", "2024-01-01");
  }

  @Test
  void test2() {
    List<SuspectDuplicatedPO> list = suspectDuplicateDataMapper.findDuplicatedPOBySite(
      "ZHU",
      "2024-01-01"
    );
    log.info(list.toString());
  }
}
