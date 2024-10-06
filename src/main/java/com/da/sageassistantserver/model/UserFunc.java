/**********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                             *
 * @CreatedDate           : 2024-06-02 17:10:30                                                                       *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                             *
 * @LastEditDate          : 2024-12-25 14:43:24                                                                       *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                           *
 *********************************************************************************************************************/

package com.da.sageassistantserver.model;

import com.alibaba.fastjson2.annotation.JSONType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.sql.Timestamp;
import lombok.Data;

@Data
@JSONType(alphabetic = false)
@TableName("user_func")
public class UserFunc {

  @TableId(type = IdType.AUTO)
  private Long id;

  private Long user_id;
  private String sage_id;
  private String func_system;
  private String func_code;
  private String func_name;
  private String func_details;
  private boolean enable;
  private Timestamp create_at;
  private Long create_by;
  private Timestamp update_at;
  private Long update_by;
}
