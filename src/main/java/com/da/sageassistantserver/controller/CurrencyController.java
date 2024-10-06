/*********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                            *
 * @CreatedDate           : 2022-03-26 20:19:00                                                                      *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                            *
 * @LastEditDate          : 2024-12-25 14:29:30                                                                      *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                          *
 ********************************************************************************************************************/

package com.da.sageassistantserver.controller;

import com.da.sageassistantserver.service.CurrencyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@CrossOrigin
@RestController
public class CurrencyController {

  @Autowired
  CurrencyService currencyService;

  @GetMapping("/Data/CurrencyRate")
  public String getCurrencyRate(
    @RequestParam(value = "Sour", required = true) String Sour,
    @RequestParam(value = "Dest", required = true) String Dest,
    @RequestParam(value = "Date", required = true) String Date
  ) {
    log.debug(Sour + Dest + Date);
    return currencyService.getFromSage(Sour + Dest + Date);
  }

  @GetMapping("/Data/CurrencyRateBatch")
  public String getCurrencyRateBatch(
    @RequestParam(value = "Query", required = true) String Query
  ) {
    log.debug(Query);
    return currencyService.getCurrencyRateBatch(Query);
  }
}
