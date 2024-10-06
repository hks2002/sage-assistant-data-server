/**********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                             *
 * @CreatedDate           : 2024-06-02 17:10:30                                                                       *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                             *
 * @LastEditDate          : 2024-12-09 19:27:50                                                                       *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                           *
 *********************************************************************************************************************/

package com.da.sageassistantserver.model;

import java.sql.Timestamp;

import com.alibaba.fastjson2.annotation.JSONType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import lombok.Data;

/**
 * Raw Log, Combined with LogTemplate definition, it can be transformed to
 * LogFinal
 */
@Data
@JSONType(alphabetic = false)
public class LogRaw {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String TCode;
    private String TGroup;
    private String TDefinition;
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
