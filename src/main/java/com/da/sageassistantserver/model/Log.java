/******************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                     *
 * @CreatedDate           : 2024-06-02 17:10:30                               *
 * @LastEditors           : Robert Huang<56649783@qq.com>                     *
 * @LastEditDate          : 2024-06-30 00:13:54                               *
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
@TableName("log")
public class Log {

  @TableId(type = IdType.AUTO)
  private Long id;

  private Long template_id;
  private String v0;
  private String v1;
  private String v2;
  private String v3;
  private String v4;
  private String v5;
  private String v6;
  private String v7;
  private String v8;
  private String v9;
  private Timestamp log_at;
}
