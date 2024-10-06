/******************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                     *
 * @CreatedDate           : 2022-09-21 12:36:00                               *
 * @LastEditors           : Robert Huang<56649783@qq.com>                     *
 * @LastEditDate          : 2024-10-12 15:30:39                               *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                   *
 *****************************************************************************/

package com.da.sageassistantserver.model;

import com.alibaba.fastjson2.annotation.JSONType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.sql.Timestamp;
import lombok.Data;

@Data
@JSONType(alphabetic = false)
@TableName("line_note")
public class LineNote {

  @TableId(type = IdType.AUTO)
  private Long id;

  private String line;
  private String project;
  private String note_type;
  private String note;
  private String note_user;
  private Timestamp create_at;
  private Long create_by;
  private Timestamp update_at;
  private Long update_by;
}
