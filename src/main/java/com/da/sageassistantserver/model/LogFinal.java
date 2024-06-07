/******************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                     *
 * @CreatedDate           : 2024-06-02 17:10:30                               *
 * @LastEditors           : Robert Huang<56649783@qq.com>                     *
 * @LastEditDate          : 2024-06-07 23:41:28                               *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                   *
 *****************************************************************************/

package com.da.sageassistantserver.model;

import java.sql.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import lombok.Data;

/**
 * Final Log, it is transformed from LogRaw, value is combined with TDefinition
 */
@Data
public class LogFinal {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String TCode;
    private String TGroup;
    private String log;
    private Date log_at;

}
