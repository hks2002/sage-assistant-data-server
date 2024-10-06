/**********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                             *
 * @CreatedDate           : 2024-06-02 17:50:39                                                                       *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                             *
 * @LastEditDate          : 2024-12-25 14:33:35                                                                       *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                           *
 *********************************************************************************************************************/

package com.da.sageassistantserver.dao;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.da.sageassistantserver.model.LineNote;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
@DS("slave")
public interface LineNoteMapper extends BaseMapper<LineNote> {
  List<LineNote> getLineNoteByProjectJsonList(
    @Param("ProjectJsonList") String ProjectJsonList
  );

  List<LineNote> getLineNoteByLineJsonList(
    @Param("LineJsonList") String LineJsonList
  );
}
