/*********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                            *
 * @CreatedDate           : 2022-06-27 14:02:00                                                                      *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                            *
 * @LastEditDate          : 2023-03-12 13:19:45                                                                      *
 * @FilePath              : src/main/java/sageassistant/dataSrv/model/CurrencyHistory.java                           *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                          *
 ********************************************************************************************************************/

package com.da.sageassistantserver.model;

import com.da.sageassistantserver.model.base.ModelTemplate;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CurrencyHistory extends ModelTemplate {

    private static final long serialVersionUID = 1L;

    private String Sour;
    private String Dest;
    private Float Rate;
    private Date StartDate;
    private Date EndDate;
}
