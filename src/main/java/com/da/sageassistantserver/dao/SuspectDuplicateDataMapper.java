/**********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                             *
 * @CreatedDate           : 2022-03-26 17:55:00                                                                       *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                             *
 * @LastEditDate          : 2024-12-25 14:34:38                                                                       *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                           *
 *********************************************************************************************************************/

package com.da.sageassistantserver.dao;

import com.da.sageassistantserver.model.SuspectDuplicatedPO;
import com.da.sageassistantserver.model.SuspectDuplicatedRA;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SuspectDuplicateDataMapper {
  List<SuspectDuplicatedPO> findDuplicatedPOBySite(
    @Param("Site") String Site,
    @Param("DateFrom") String DateFrom
  );

  List<SuspectDuplicatedRA> findDuplicatedRABySite(
    @Param("Site") String Site,
    @Param("DateFrom") String DateFrom
  );
}
