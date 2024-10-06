/*********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                            *
 * @CreatedDate           : 2024-06-30 00:19:48                                                                      *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                            *
 * @LastEditDate          : 2024-12-25 14:37:14                                                                      *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                          *
 ********************************************************************************************************************/

package com.da.sageassistantserver.model;

import com.alibaba.fastjson2.annotation.JSONField;
import com.alibaba.fastjson2.annotation.JSONType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.sql.Timestamp;
import lombok.Data;

@Data
@JSONType(alphabetic = false)
@TableName("docs")
public class Docs {

  @TableId(type = IdType.AUTO)
  private Long id;

  private String file_name;
  private String location;
  private Long size;
  private Timestamp doc_create_at;

  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  private Timestamp doc_modified_at;

  private String md5;
  private Timestamp create_at;
  private Long create_by;
  private Timestamp update_at;
  private Long update_by;
}
