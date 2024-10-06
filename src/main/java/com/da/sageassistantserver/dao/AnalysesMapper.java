/*********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                            *
 * @CreatedDate           : 2022-03-26 17:55:00                                                                      *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                            *
 * @LastEditDate          : 2024-12-25 14:32:48                                                                      *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                          *
 ********************************************************************************************************************/

package com.da.sageassistantserver.dao;

import com.da.sageassistantserver.model.AnalysesPurchase;
import com.da.sageassistantserver.model.AnalysesQuote;
import com.da.sageassistantserver.model.AnalysesQuoteSalesCost;
import com.da.sageassistantserver.model.AnalysesSales;
import com.da.sageassistantserver.model.ProjectProfit;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AnalysesMapper {
  List<AnalysesQuoteSalesCost> analysesQuoteSalesCost(
    @Param("Site") String Site,
    @Param("CategoryCode") String CategoryCode,
    @Param("PnRoot") String PnRoot,
    @Param("DateFrom") String DateFrom,
    @Param("DateTo") String DateTo,
    @Param("Limit") Integer Limit
  );

  List<AnalysesPurchase> analysesPurchase(
    @Param("Site") String Site,
    @Param("PnRoot") String PnRoot,
    @Param("Currency") String Currency,
    @Param("LastN") String LastN
  );

  List<AnalysesQuote> analysesQuote(
    @Param("Site") String Site,
    @Param("PnRoot") String PnRoot,
    @Param("Currency") String Currency,
    @Param("LastN") String LastN
  );

  List<AnalysesSales> analysesSales(
    @Param("Site") String Site,
    @Param("PnRoot") String PnRoot,
    @Param("Currency") String Currency,
    @Param("LastN") String LastN
  );

  List<ProjectProfit> preAnalysesProjectProfit(
    @Param("Site") String Site,
    @Param("DateFrom") String DateFrom
  );
}
