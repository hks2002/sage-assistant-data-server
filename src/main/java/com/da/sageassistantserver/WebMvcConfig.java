/**********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                             *
 * @CreatedDate           : 2023-11-09 18:52:05                                                                       *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                             *
 * @LastEditDate          : 2024-12-25 14:50:31                                                                       *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                           *
 *********************************************************************************************************************/

package com.da.sageassistantserver;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

  @Override
  @SuppressWarnings("null")
  public void configureMessageConverters(
    List<HttpMessageConverter<?>> converters
  ) {
    // fastJson support return List<Object>
    // Add to last, then it could use default string converter,
    // because fastJson converter will add quotes to string.
    converters.add(new JSONConverter());
  }
}
