/*********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                            *
 * @CreatedDate           : 2022-03-26 17:01:00                                                                      *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                            *
 * @LastEditDate          : 2023-03-12 13:22:04                                                                      *
 * @FilePath              : src/main/java/sageassistant/dataSrv/model/SupplierName.java                              *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                          *
 ********************************************************************************************************************/

package com.da.sageassistantserver.model;

import com.da.sageassistantserver.model.base.ModelTemplate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupplierName extends ModelTemplate {

    private static final long serialVersionUID = 1L;

    private String SupplierCode;
    private String SupplierName;
}
