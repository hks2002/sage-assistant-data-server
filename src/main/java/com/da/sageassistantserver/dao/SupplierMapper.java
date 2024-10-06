/*********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                            *
 * @CreatedDate           : 2022-03-26 17:55:00                                                                      *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                            *
 * @LastEditDate          : 2024-12-25 14:35:14                                                                      *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                          *
 ********************************************************************************************************************/

package com.da.sageassistantserver.dao;

import com.da.sageassistantserver.model.BusinessPartnerName;
import com.da.sageassistantserver.model.SupplierDetails;
import com.da.sageassistantserver.model.SupplierNCHistory;
import com.da.sageassistantserver.model.SupplierNCSummary;
import com.da.sageassistantserver.model.SupplierName;
import com.da.sageassistantserver.model.SupplierOTD;
import com.da.sageassistantserver.model.SupplierOTDTop;
import com.da.sageassistantserver.model.SupplierOrder;
import com.da.sageassistantserver.model.SupplierQPY;
import com.da.sageassistantserver.model.SupplierQPYTop;
import com.da.sageassistantserver.model.SupplierSummaryAmount;
import com.da.sageassistantserver.model.SupplierSummaryAmountTop;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SupplierMapper {
  List<SupplierName> findSupplierByCodeOrName(
    @Param("SupplierCodeOrName") String SupplierCodeOrName,
    @Param("Count") Integer Count
  );
  List<BusinessPartnerName> findBusinessPartnerByCode(
    @Param("BPCode") String BPCode
  );

  List<SupplierDetails> findSupplierDetailsByCode(
    @Param("SupplierCode") String SupplierCode
  );

  List<SupplierSummaryAmount> findSupplierSumAmount(
    @Param("Site") String Site,
    @Param("SupplierType") String SupplierType,
    @Param("SupplierCode") String SupplierCode,
    @Param("DateFrom") String DateFrom,
    @Param("DateTo") String DateTo,
    @Param("Interval") String Interval
  );

  List<SupplierSummaryAmountTop> findSupplierSumAmountTop(
    @Param("Site") String Site,
    @Param("SupplierType") String SupplierType,
    @Param("DateFrom") String DateFrom,
    @Param("DateTo") String DateTo,
    @Param("Limit") Integer Limit
  );

  List<SupplierOTD> findSupplierOTD(
    @Param("Site") String Site,
    @Param("SupplierType") String SupplierType,
    @Param("SupplierCode") String SupplierCode,
    @Param("DateFrom") String DateFrom,
    @Param("DateTo") String DateTo,
    @Param("Interval") String Interval
  );

  List<SupplierOTDTop> findSupplierOTDTop(
    @Param("Site") String Site,
    @Param("SupplierType") String SupplierType,
    @Param("DateFrom") String DateFrom,
    @Param("DateTo") String DateTo,
    @Param("Limit") Integer Limit,
    @Param("Sort") String Sort
  );

  List<SupplierQPY> findSupplierQPY(
    @Param("Site") String Site,
    @Param("SupplierType") String SupplierType,
    @Param("SupplierCode") String SupplierCode,
    @Param("DateFrom") String DateFrom,
    @Param("DateTo") String DateTo,
    @Param("Interval") String Interval
  );

  List<SupplierQPYTop> findSupplierQPYTop(
    @Param("Site") String Site,
    @Param("SupplierType") String SupplierType,
    @Param("DateFrom") String DateFrom,
    @Param("DateTo") String DateTo,
    @Param("Limit") Integer Limit,
    @Param("Sort") String Sort
  );

  List<SupplierNCSummary> findSupplierNCSummary(
    @Param("Site") String Site,
    @Param("SupplierType") String SupplierType,
    @Param("SupplierCode") String SupplierCode,
    @Param("DateFrom") String DateFrom,
    @Param("DateTo") String DateTo,
    @Param("Interval") String Interval
  );

  Integer findSupplierOrdersCnt(
    @Param("Site") String Site,
    @Param("SupplierType") String SupplierType,
    @Param("SupplierCode") String SupplierCode,
    @Param("DateFrom") String DateFrom,
    @Param("DateTo") String DateTo,
    @Param("OrderStatus") String OrderStatus
  );

  List<SupplierOrder> findSupplierOrders(
    @Param("Site") String Site,
    @Param("SupplierType") String SupplierType,
    @Param("SupplierCode") String SupplierCode,
    @Param("DateFrom") String DateFrom,
    @Param("DateTo") String DateTo,
    @Param("OrderStatus") String OrderStatus,
    @Param("Offset") Integer Offset,
    @Param("Limit") Integer Limit
  );

  Integer findSupplierNCHistoryCnt(
    @Param("Site") String Site,
    @Param("SupplierType") String SupplierType,
    @Param("SupplierCode") String SupplierCode,
    @Param("DateFrom") String DateFrom,
    @Param("DateTo") String DateTo
  );

  List<SupplierNCHistory> findSupplierNCHistory(
    @Param("Site") String Site,
    @Param("SupplierType") String SupplierType,
    @Param("SupplierCode") String SupplierCode,
    @Param("DateFrom") String DateFrom,
    @Param("DateTo") String DateTo,
    @Param("Offset") Integer Offset,
    @Param("Limit") Integer Limit
  );
}
