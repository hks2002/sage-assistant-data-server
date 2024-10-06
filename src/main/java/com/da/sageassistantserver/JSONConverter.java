/**********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                             *
 * @CreatedDate           : 2023-11-09 18:52:05                                                                       *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                             *
 * @LastEditDate          : 2024-12-25 14:50:16                                                                       *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                           *
 *********************************************************************************************************************/

package com.da.sageassistantserver;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.support.config.FastJsonConfig;
import com.alibaba.fastjson2.support.spring6.http.converter.FastJsonHttpMessageConverter;
import java.util.Collections;
import org.springframework.http.MediaType;

public class JSONConverter extends FastJsonHttpMessageConverter {

  public JSONConverter() {
    super();
    JSON.config(JSONWriter.Feature.FieldBased);

    FastJsonConfig jsonConfig = new FastJsonConfig();
    jsonConfig.setDateFormat("yyyy-MM-dd");

    setFastJsonConfig(jsonConfig);
    setSupportedMediaTypes(
      Collections.singletonList(MediaType.APPLICATION_JSON)
    );
  }
}
