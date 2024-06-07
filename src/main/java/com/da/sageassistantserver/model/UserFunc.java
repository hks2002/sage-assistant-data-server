/******************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                     *
 * @CreatedDate           : 2024-06-02 17:10:30                               *
 * @LastEditors           : Robert Huang<56649783@qq.com>                     *
 * @LastEditDate          : 2024-06-07 23:33:31                               *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                   *
 *****************************************************************************/

package com.da.sageassistantserver.model;

import java.sql.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("user_func")
public class UserFunc {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long user_id;
    private String sage_id;
    private String func_system;
    private String func_code;
    private String func_name;
    private boolean enable;
    private Date create_at;
    private Long create_by;
    private Date update_at;
    private Long update_by;

}
