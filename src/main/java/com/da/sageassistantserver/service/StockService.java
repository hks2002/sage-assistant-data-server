/*********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                            *
 * @CreatedDate           : 2022-03-26 17:57:00                                                                      *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                            *
 * @LastEditDate          : 2024-12-25 14:47:37                                                                      *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                          *
 ********************************************************************************************************************/

package com.da.sageassistantserver.service;

import com.da.sageassistantserver.dao.StockMapper;
import com.da.sageassistantserver.model.StockHistory;
import com.da.sageassistantserver.model.StockSummary;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StockService {

  @Autowired
  private StockMapper stockMapper;

  public String checkPN(@Param("pnRoot") String pnRoot) {
    return stockMapper.checkPN(pnRoot);
  }

  public String getStockOptionPN(@Param("pnRoot") String pnRoot) {
    // remove special character, pnRoot must be url encoded,
    pnRoot = pnRoot.replaceAll(",|\\.| |-|_|/|\\\\", "");
    return stockMapper.findStockOptionPN(pnRoot);
  }

  public String getStockQty(
    @Param("Site") String Site,
    @Param("PnRoot") String pnRoot
  ) {
    if (stockMapper.findStockQty(Site, pnRoot) == null) {
      return "0";
    } else {
      return stockMapper.findStockQty(Site, pnRoot);
    }
  }

  public List<StockSummary> getStockSummary(@Param("Site") String Site) {
    return stockMapper.findStockSummaryBySite(Site);
  }

  public List<StockHistory> getStockHistory(
    @Param("Site") String Site,
    @Param("PnOrName") String PnOrName,
    @Param("DateFrom") String DateFrom,
    @Param("DateTo") String DateTo
  ) {
    return stockMapper.findStockHistoryBySite(Site, PnOrName, DateFrom, DateTo);
  }
}
