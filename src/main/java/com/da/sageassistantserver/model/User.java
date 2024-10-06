/**********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                             *
 * @CreatedDate           : 2024-06-02 17:10:30                                                                       *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                             *
 * @LastEditDate          : 2024-12-25 14:23:31                                                                       *
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
@TableName("user")
public class User {

  @TableId(type = IdType.AUTO)
  private Long id;

  private String sage_id;
  private String login_name;
  private String first_name;
  private String last_name;
  private String email;
  private String language;
  private Timestamp create_at;
  private Long create_by;
  private Timestamp update_at;
  private Long update_by;
}
