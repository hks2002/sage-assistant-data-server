/*********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                            *
 * @CreatedDate           : 2024-06-06 16:39:59                                                                      *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                            *
 * @LastEditDate          : 2024-12-25 14:33:48                                                                      *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                          *
 ********************************************************************************************************************/

package com.da.sageassistantserver.dao;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.da.sageassistantserver.model.LogRaw;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
@DS("slave")
public interface LogRawMapper {
  List<LogRaw> findLogs(
    @Param("TCode") String TCode,
    @Param("TGroup") String TGroup,
    @Param("DateFrom") String DateFrom,
    @Param("DateTo") String DateTo,
    @Param("Language") String Language
  );
  List<LogRaw> findUserLogs(
    @Param("TCode") String TCode,
    @Param("User") String TGroup,
    @Param("Offset") Integer Offset,
    @Param("Limit") Integer Limit
  );
}
