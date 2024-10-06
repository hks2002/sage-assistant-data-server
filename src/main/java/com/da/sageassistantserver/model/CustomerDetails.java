/**********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                             *
 * @CreatedDate           : 2022-03-31 16:16:00                                                                       *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                             *
 * @LastEditDate          : 2024-12-09 19:28:41                                                                       *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                           *
 *********************************************************************************************************************/

package com.da.sageassistantserver.model;

import com.alibaba.fastjson2.annotation.JSONType;
import lombok.Data;

@Data
@JSONType(alphabetic = false)
public class CustomerDetails {

  private String CustomerCode;
  private String CustomerName0;
  private String CustomerName1;
  private String Address0;
  private String Address1;
  private String PostCode;
  private String Country;
  private String State;
  private String City;
  private String Tel0;
  private String Tel1;
  private String Tel2;
  private String Tel3;
  private String Tel4;
  private String Fax0;
  private String Mobile0;
  private String Email0;
  private String Email1;
  private String Email2;
  private String Email3;
  private String Email4;
  private String Website;
}
