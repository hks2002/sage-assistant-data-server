/**********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                             *
 * @CreatedDate           : 2024-06-02 17:50:39                                                                       *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                             *
 * @LastEditDate          : 2024-12-25 14:34:52                                                                       *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                           *
 *********************************************************************************************************************/

package com.da.sageassistantserver.dao;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.da.sageassistantserver.model.WorkAction;
import com.da.sageassistantserver.model.WorkActionCnt;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
@DS("slave")
public interface WorkActionMapper extends BaseMapper<WorkAction> {
  List<WorkAction> getWorkActionByProjectJsonList(
    @Param("ProjectJsonList") String ProjectJsonList
  );

  List<WorkActionCnt> getWorkActionCntByProjectJsonList(
    @Param("ProjectJsonList") String ProjectJsonList,
    @Param("Site") String Site
  );
}
