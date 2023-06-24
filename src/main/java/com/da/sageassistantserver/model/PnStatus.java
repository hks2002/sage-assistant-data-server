/*********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                            *
 * @CreatedDate           : 2022-03-26 17:01:00                                                                      *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                            *
 * @LastEditDate          : 2023-03-12 13:21:29                                                                      *
 * @FilePath              : src/main/java/sageassistant/dataSrv/model/PnStatus.java                                  *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                          *
 ********************************************************************************************************************/

package com.da.sageassistantserver.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PnStatus {

    private String PN;
    private String Desc1;
    private String Desc2;
    private String Desc3;
    private String PNStatus;
    private String WC;
    private String ProjectNO;
    private String CustomerCode;
    private String CustomerName;
}
