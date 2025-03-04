/**********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                             *
 * @CreatedDate           : 2022-03-26 17:57:00                                                                       *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                             *
 * @LastEditDate          : 2024-12-09 19:50:17                                                                       *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                           *
 *********************************************************************************************************************/

package com.da.sageassistantserver.service;

import com.da.sageassistantserver.dao.PnMapper;
import com.da.sageassistantserver.dao.StockMapper;
import com.da.sageassistantserver.model.CostHistory;
import com.da.sageassistantserver.model.DeliveryDuration;
import com.da.sageassistantserver.model.PnDetails;
import com.da.sageassistantserver.model.PnRootPn;
import com.da.sageassistantserver.model.PnStatus;
import com.da.sageassistantserver.model.QuoteHistory;
import com.da.sageassistantserver.model.SalesHistory;
import com.da.sageassistantserver.model.StockInfo;
import com.da.sageassistantserver.utils.Utils;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PnService {

  @Autowired
  private PnMapper pnMapper;

  @Autowired
  private StockMapper stockMapper;

  @Autowired
  private CurrencyService currencyService;

  public List<PnRootPn> findPnByStartWith(String cond, Integer Count) {
    List<PnRootPn> listPage = pnMapper.findPnByLike(cond + "%", Count);

    return listPage;
  }

  public List<PnRootPn> findPnByEndWith(String cond, Integer Count) {
    List<PnRootPn> listPage = pnMapper.findPnByLike("%" + cond, Count);

    return listPage;
  }

  public List<PnRootPn> findPnByContains(String cond, Integer Count) {
    List<PnRootPn> listPage = pnMapper.findPnByLike("%" + cond + "%", Count);

    return listPage;
  }

  public List<PnDetails> findAllPnByPnRoot(String PnRoot) {
    return pnMapper.findAllPnByPnRoot(PnRoot);
  }

  public List<SalesHistory> findSalesHistoryByPnRoot(String PnRoot) {
    List<SalesHistory> listPage = (ArrayList<SalesHistory>) pnMapper.findSalesHistoryByPnRoot(
      PnRoot
    );

    for (SalesHistory o : listPage) {
      String key = o.getCurrency() + "USD" + Utils.formatDate(o.getOrderDate());
      log.debug("key:" + key);
      try {
        o.setRate(Float.parseFloat(currencyService.cache.get(key)));
        log.debug("Rate:" + o.getRate());
      } catch (NumberFormatException e) {
        log.error(e.getMessage());
      }
      o.setUSD(o.getNetPrice().multiply(new BigDecimal(o.getRate())));
    }

    return listPage;
  }

  public List<QuoteHistory> findQuoteHistoryByPnRoot(String PnRoot) {
    List<QuoteHistory> listPage = pnMapper.findQuoteHistoryByPnRoot(PnRoot);

    for (QuoteHistory o : listPage) {
      String key = o.getCurrency() + "USD" + Utils.formatDate(o.getQuoteDate());
      log.debug("key:" + key);
      try {
        o.setRate(Float.parseFloat(currencyService.cache.get(key)));
        log.debug("Rate:" + o.getRate());
      } catch (NumberFormatException e) {
        log.error(e.getMessage());
      }
      o.setUSD(o.getNetPrice().multiply(new BigDecimal(o.getRate())));
    }

    return listPage;
  }

  public List<CostHistory> findCostHistoryByPnRoot(String PnRoot) {
    List<CostHistory> listPage = pnMapper.findCostHistoryByPnRoot(PnRoot);

    for (CostHistory o : listPage) {
      String key = o.getCurrency() + "USD" + Utils.formatDate(o.getOrderDate());
      log.debug("key:" + key);
      try {
        o.setRate(Float.parseFloat(currencyService.cache.get(key)));
        log.debug("Rate:" + o.getRate());
      } catch (NumberFormatException e) {
        log.error(e.getMessage());
      }
      o.setUSD(o.getNetPrice().multiply(new BigDecimal(o.getRate())));
    }
    // one project maybe purchase line with different currency

    return listPage;
  }

  public List<DeliveryDuration> findDeliveryDurationByPnRoot(String PnRoot) {
    return pnMapper.findDeliveryDurationByPnRoot(PnRoot);
  }

  public List<StockInfo> findStockInfoByPnRoot(String PnRoot) {
    return stockMapper.findStockInfoByPnRoot(PnRoot);
  }

  public List<PnStatus> findObsoletePnBySite(String Site) {
    return pnMapper.findObsoletePnBySite(Site);
  }
}
