/*********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                            *
 * @CreatedDate           : 2022-03-31 16:25:00                                                                      *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                            *
 * @LastEditDate          : 2024-11-03 18:43:20                                                                      *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                          *
 ********************************************************************************************************************/

package com.da.sageassistantserver.dao;

import com.da.sageassistantserver.model.CustomerDetails;
import com.da.sageassistantserver.model.CustomerName;
import com.da.sageassistantserver.model.CustomerOTD;
import com.da.sageassistantserver.model.CustomerOrder;
import com.da.sageassistantserver.model.CustomerSummaryAmount;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CustomerMapper {
  List<CustomerDetails> findCustomerDetailsByCode(
    @Param("CustomerCode") String CustomerCode
  );

  List<CustomerName> findCustomerByCodeOrName(
    @Param("CustomerCodeOrName") String CustomerCodeOrName,
    @Param("Count") Integer Count
  );

  Integer findCustomerOrdersCnt(
    @Param("Site") String Site,
    @Param("CustomerCode") String CustomerCode,
    @Param("DateFrom") String DateFrom,
    @Param("DateTo") String DateTo,
    @Param("OrderStatus") String OrderStatus
  );

  List<CustomerOrder> findCustomerOrders(
    @Param("Site") String Site,
    @Param("CustomerCode") String CustomerCode,
    @Param("DateFrom") String DateFrom,
    @Param("DateTo") String DateTo,
    @Param("OrderStatus") String OrderStatus,
    @Param("Offset") Integer Offset,
    @Param("Limit") Integer Limit
  );

  List<CustomerSummaryAmount> findCustomerSumAmount(
    @Param("Site") String Site,
    @Param("CustomerCode") String CustomerCode,
    @Param("DateFrom") String DateFrom,
    @Param("DateTo") String DateTo,
    @Param("Interval") String Interval
  );

  List<CustomerOTD> findCustomerOTD(
    @Param("Site") String Site,
    @Param("CustomerCode") String CustomerCode,
    @Param("DateFrom") String DateFrom,
    @Param("DateTo") String DateTo,
    @Param("Interval") String Interval
  );
}
