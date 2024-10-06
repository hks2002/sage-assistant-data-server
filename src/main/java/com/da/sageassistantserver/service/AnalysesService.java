/*********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                            *
 * @CreatedDate           : 2022-03-26 17:57:00                                                                      *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                            *
 * @LastEditDate          : 2024-12-25 14:44:10                                                                      *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                          *
 ********************************************************************************************************************/

package com.da.sageassistantserver.service;

import com.da.sageassistantserver.dao.AnalysesMapper;
import com.da.sageassistantserver.dao.CommonMapper;
import com.da.sageassistantserver.model.AnalysesPurchase;
import com.da.sageassistantserver.model.AnalysesQuote;
import com.da.sageassistantserver.model.AnalysesQuoteSalesCost;
import com.da.sageassistantserver.model.AnalysesSales;
import com.da.sageassistantserver.utils.Utils;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AnalysesService {

  @Autowired
  private AnalysesMapper analysesMapper;

  @Autowired
  private CommonMapper commonMapper;

  public List<AnalysesQuoteSalesCost> analysesQuoteSalesCost(
    @Param("Site") String Site,
    @Param("CategoryCode") String CategoryCode,
    @Param("PnRoot") String PnRoot,
    @Param("DateFrom") String DateFrom,
    @Param("DateTo") String DateTo,
    @Param("Limit") Integer Limit
  ) {
    return analysesMapper.analysesQuoteSalesCost(
      Site,
      CategoryCode,
      PnRoot,
      DateFrom,
      DateTo,
      Limit
    );
  }

  public List<AnalysesQuoteSalesCost> analysesQuoteSalesCostAll(
    @Param("CategoryCode") String CategoryCode,
    @Param("PnRoot") String PnRoot,
    @Param("DateFrom") String DateFrom,
    @Param("DateTo") String DateTo,
    @Param("Limit") Integer Limit
  ) {
    List<AnalysesQuoteSalesCost> listAll = new ArrayList<>();
    for (String Site : commonMapper.getAllSites()) {
      listAll.addAll(
        analysesMapper.analysesQuoteSalesCost(
          Site,
          CategoryCode,
          PnRoot,
          DateFrom,
          DateTo,
          Limit
        )
      );
    }
    return listAll;
  }

  public AnalysesQuoteSalesCost analysesQuoteSalesCostByTarget(
    @Param("Site") String Site,
    @Param("PnRoot") String PnRoot,
    @Param("DateFrom") String DateFrom,
    @Param("DateTo") String DateTo,
    @Param("Limit") Integer Limit,
    @Param("Target") String Target
  ) {
    List<AnalysesQuoteSalesCost> result = analysesMapper.analysesQuoteSalesCost(
      Site,
      "",
      PnRoot,
      DateFrom,
      DateTo,
      Limit
    );
    if (result.size() > 0) {
      return result.get(0);
    } else {
      log.info(
        "Site {} PnRoot {} DateFrom {} DateTo {} Limit {} Target {} no result",
        Site,
        PnRoot,
        DateFrom,
        DateTo,
        Limit,
        Target
      );
      return new AnalysesQuoteSalesCost();
    }
  }

  public List<String> analysesPurchase(
    @Param("Site") String Site,
    @Param("PnRoot") String PnRoot,
    @Param("Currency") String Currency,
    @Param("Target") String Target,
    @Param("LastN") String LastN
  ) {
    List<AnalysesPurchase> result = analysesMapper.analysesPurchase(
      Site,
      PnRoot,
      Currency,
      LastN
    );
    List<String> rtn = new ArrayList<String>(); // maybe
    // more
    // than
    // one
    // result

    for (AnalysesPurchase o : result) {
      switch (Target) {
        case "ProjectNO":
          rtn.add(o.getProjectNO());
          break;
        case "PurchaseNO":
          rtn.add(o.getPurchaseNO());
          break;
        case "ProjectDate":
          rtn.add(Utils.formatDate(o.getProjectDate()));
          break;
        case "PurchaseDate":
          rtn.add(Utils.formatDate(o.getPurchaseDate()));
          break;
        case "NetPrice":
          rtn.add(o.getNetPrice().toString());
          break;
        default:
          break;
      }
    }

    return rtn;
  }

  public List<String> analysesQuote(
    @Param("Site") String Site,
    @Param("PnRoot") String PnRoot,
    @Param("Currency") String Currency,
    @Param("Target") String Target,
    @Param("LastN") String LastN
  ) {
    List<AnalysesQuote> result = analysesMapper.analysesQuote(
      Site,
      PnRoot,
      Currency,
      LastN
    );
    List<String> rtn = new ArrayList<String>(); // maybe
    // more
    // than
    // one
    // result

    for (AnalysesQuote o : result) {
      switch (Target) {
        case "QuoteNO":
          rtn.add(o.getQuoteNO());
          break;
        case "QuoteDate":
          rtn.add(Utils.formatDate(o.getQuoteDate()));
          break;
        case "CustomerCode":
          rtn.add(o.getCustomerCode());
          break;
        case "CustomerName":
          rtn.add(o.getCustomerName());
          break;
        case "NetPrice":
          rtn.add(o.getNetPrice().toString());
          break;
        case "OrderNO":
          rtn.add(o.getOrderNO());
          break;
        case "OrderFlag":
          rtn.add(o.getOrderFlag().toString());
          break;
        case "QTY":
          rtn.add(o.getQTY().toString());
          break;
        default:
          break;
      }
    }

    return rtn;
  }

  public List<String> analysesSales(
    @Param("Site") String Site,
    @Param("PnRoot") String PnRoot,
    @Param("Currency") String Currency,
    @Param("Target") String Target,
    @Param("LastN") String LastN
  ) {
    List<AnalysesSales> result = analysesMapper.analysesSales(
      Site,
      PnRoot,
      Currency,
      LastN
    );
    List<String> rtn = new ArrayList<String>(); // maybe
    // more
    // than
    // one
    // result

    for (AnalysesSales o : result) {
      switch (Target) {
        case "OrderNO":
          rtn.add(o.getOrderNO());
          break;
        case "OrderDate":
          rtn.add(Utils.formatDate(o.getOrderDate()));
          break;
        case "CustomerCode":
          rtn.add(o.getCustomerCode());
          break;
        case "CustomerName":
          rtn.add(o.getCustomerName());
          break;
        case "NetPrice":
          rtn.add(o.getNetPrice().toString());
          break;
        case "QTY":
          rtn.add(o.getQTY().toString());
          break;
        default:
          break;
      }
    }

    return rtn;
  }
}
