/******************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                     *
 * @CreatedDate           : 2024-06-02 17:10:30                               *
 * @LastEditors           : Robert Huang<56649783@qq.com>                     *
 * @LastEditDate          : 2024-06-27 18:49:58                               *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                   *
 *****************************************************************************/

package com.da.sageassistantserver.model;

import com.alibaba.fastjson2.annotation.JSONType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.sql.Timestamp;
import lombok.Data;

/**
 * Final Log, it is transformed from LogRaw, value is combined with TDefinition
 */
@Data
@JSONType(alphabetic = false)
public class LogFinal {

  @TableId(type = IdType.AUTO)
  private Long id;

  private String TCode;
  private String TGroup;
  private String log;
  private Timestamp log_at;
}
