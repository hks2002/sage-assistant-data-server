/**********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                             *
 * @CreatedDate           : 2023-03-12 19:34:33                                                                       *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                             *
 * @LastEditDate          : 2024-12-25 14:51:01                                                                       *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                           *
 *********************************************************************************************************************/

package com.da.sageassistantserver.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AnalysesMapperTest {

  @Autowired
  AnalysesMapper analysesMapper;

  @Test
  void testAnalysesQuoteSalesCost() {
    analysesMapper.analysesQuoteSalesCost(
      "ZHU",
      "C0002",
      "956A1001G01",
      "2023-01-01",
      "2023-01-02",
      1
    );
  }

  @Test
  void testAnalysesPurchase() {
    analysesMapper.analysesPurchase("ZHU", "956A1001G01", "RMB", "1");
  }

  @Test
  void testAnalysesQuote() {
    analysesMapper.analysesQuote("ZHU", "956A1001G01", "RMB", "1");
  }

  @Test
  void testAnalysesSales() {
    analysesMapper.analysesSales("ZHU", "956A1001G01", "RMB", "1");
  }
}
