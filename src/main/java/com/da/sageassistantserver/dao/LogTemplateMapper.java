/*********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                            *
 * @CreatedDate           : 2024-06-06 16:39:59                                                                      *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                            *
 * @LastEditDate          : 2024-12-25 14:33:52                                                                      *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                          *
 ********************************************************************************************************************/

package com.da.sageassistantserver.dao;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.da.sageassistantserver.model.LogTemplate;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@DS("slave")
public interface LogTemplateMapper extends BaseMapper<LogTemplate> {}
