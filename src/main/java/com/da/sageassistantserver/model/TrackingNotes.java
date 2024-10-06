/**********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                             *
 * @CreatedDate           : 2022-06-06 15:47:00                                                                       *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                             *
 * @LastEditDate          : 2024-12-25 14:43:04                                                                       *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                           *
 *********************************************************************************************************************/

package com.da.sageassistantserver.model;

import com.alibaba.fastjson2.annotation.JSONType;
import java.util.Date;
import lombok.Data;

@Data
@JSONType(alphabetic = false)
public class TrackingNotes {

  private String TrackCode;
  private String Note;
  private String NoteBy;
  private Date NoteDate;
}
