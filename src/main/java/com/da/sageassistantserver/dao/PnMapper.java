/*********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                            *
 * @CreatedDate           : 2022-03-26 17:55:00                                                                      *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                            *
 * @LastEditDate          : 2024-12-09 19:20:47                                                                      *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                          *
 ********************************************************************************************************************/

package com.da.sageassistantserver.dao;

import com.da.sageassistantserver.model.CostHistory;
import com.da.sageassistantserver.model.DeliveryDuration;
import com.da.sageassistantserver.model.PnDetails;
import com.da.sageassistantserver.model.PnRootPn;
import com.da.sageassistantserver.model.PnStatus;
import com.da.sageassistantserver.model.QuoteHistory;
import com.da.sageassistantserver.model.SalesHistory;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PnMapper {
  List<PnRootPn> findPnByLike(
    @Param("PnOrPnRoot") String PnRootOrPn,
    @Param("Count") Integer Count
  );

  List<String> findOptionPn(@Param("PnRoot") String PnRoot);

  List<PnDetails> findAllPnByPnRoot(@Param("PnRoot") String PnRoot);

  List<PnStatus> findObsoletePnBySite(@Param("Site") String Site);

  List<SalesHistory> findSalesHistoryByPnRoot(@Param("PnRoot") String PnRoot);

  List<QuoteHistory> findQuoteHistoryByPnRoot(@Param("PnRoot") String PnRoot);

  List<CostHistory> findCostHistoryByPnRoot(@Param("PnRoot") String PnRoot);

  List<DeliveryDuration> findDeliveryDurationByPnRoot(
    @Param("PnRoot") String PnRoot
  );
}
