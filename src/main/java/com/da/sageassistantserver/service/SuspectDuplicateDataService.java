/**********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                             *
 * @CreatedDate           : 2022-03-26 17:57:00                                                                       *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                             *
 * @LastEditDate          : 2024-12-09 19:51:13                                                                       *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                           *
 *********************************************************************************************************************/

package com.da.sageassistantserver.service;

import com.da.sageassistantserver.dao.SuspectDuplicateDataMapper;
import com.da.sageassistantserver.model.SuspectDuplicatedPO;
import com.da.sageassistantserver.model.SuspectDuplicatedRA;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SuspectDuplicateDataService {

  @Autowired
  private SuspectDuplicateDataMapper dirtyDataMapper;

  public List<SuspectDuplicatedPO> findDuplicatedPOBySite(
    String Site,
    String DateFrom
  ) {
    List<SuspectDuplicatedPO> listPage = dirtyDataMapper.findDuplicatedPOBySite(
      Site,
      DateFrom
    );

    return listPage;
  }

  public List<SuspectDuplicatedRA> findDuplicatedRABySite(
    String Site,
    String DateFrom
  ) {
    List<SuspectDuplicatedRA> listPage = dirtyDataMapper.findDuplicatedRABySite(
      Site,
      DateFrom
    );

    return listPage;
  }
}
