/**********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                             *
 * @CreatedDate           : 2022-09-21 12:36:00                                                                       *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                             *
 * @LastEditDate          : 2024-12-25 14:43:49                                                                       *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                           *
 *********************************************************************************************************************/

package com.da.sageassistantserver.model;

import com.alibaba.fastjson2.annotation.JSONType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.sql.Timestamp;
import java.util.Date;
import lombok.Data;

@Data
@JSONType(alphabetic = false)
@TableName("work_action")
public class WorkAction {

  @TableId(type = IdType.AUTO)
  private Long id;

  private String site;
  private String act;
  private String project;
  private String pn;
  private String sn;
  private Integer qty;
  private String result;
  private String act_user;
  private Date act_date;
  private String note;
  private Timestamp create_at;
  private Long create_by;
  private Timestamp update_at;
  private Long update_by;
}
