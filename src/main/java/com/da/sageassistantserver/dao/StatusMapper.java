/*********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                            *
 * @CreatedDate           : 2022-03-26 17:55:00                                                                      *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                            *
 * @LastEditDate          : 2024-12-25 14:34:25                                                                      *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                          *
 ********************************************************************************************************************/

package com.da.sageassistantserver.dao;

import com.da.sageassistantserver.model.DeadPurchaseLine;
import com.da.sageassistantserver.model.TobeClosedWO;
import com.da.sageassistantserver.model.TobeDealWithOrderLine;
import com.da.sageassistantserver.model.TobeDelivery;
import com.da.sageassistantserver.model.TobeInvoice;
import com.da.sageassistantserver.model.TobePurchaseBom;
import com.da.sageassistantserver.model.TobeReceive;
import com.da.sageassistantserver.model.TobeTrackingBOMLine;
import com.da.sageassistantserver.model.TobeTrackingNCLine;
import com.da.sageassistantserver.model.TobeTrackingPurchaseOrderLine;
import com.da.sageassistantserver.model.TobeTrackingReceiptLine;
import com.da.sageassistantserver.model.TobeTrackingSalesOrderLine;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface StatusMapper {
  List<TobeDelivery> findTobeDeliveryBySite(@Param("Site") String Site);

  List<TobeReceive> findTobeReceiveBySite(@Param("Site") String Site);

  List<TobeDealWithOrderLine> findTobeDealWithOrderLineBySite(
    @Param("Site") String site
  );

  List<TobePurchaseBom> findTobePurchaseBomBySite(@Param("Site") String Site);

  List<TobeClosedWO> findTobeClosedWOBySite(@Param("Site") String Site);

  List<TobeInvoice> findNoInvoicePOBySite(@Param("Site") String Site);

  List<DeadPurchaseLine> findDeadPurchaseLineBySite(@Param("Site") String Site);

  Integer findTobeTrackingSalesOrderLineCntBySite(
    @Param("Sites") List<String> Sites,
    @Param("OrderType") String OrderType,
    @Param("CustomerCode") String CustomerCode,
    @Param("VendorCode") String VendorCode,
    @Param("ProjectNO") String ProjectNO
  );

  List<TobeTrackingSalesOrderLine> findTobeTrackingSalesOrderLineBySite(
    @Param("Sites") List<String> Sites,
    @Param("OrderType") String OrderType,
    @Param("CustomerCode") String CustomerCode,
    @Param("VendorCode") String VendorCode,
    @Param("ProjectNO") String ProjectNO,
    @Param("OrderBy") String OrderBy,
    @Param("Descending") String Descending,
    @Param("Offset") Integer Offset,
    @Param("Limit") Integer Limit
  );

  List<TobeTrackingBOMLine> findTobeTrackingBOMLineBySite(
    @Param("Sites") List<String> Sites,
    @Param("OrderType") String OrderType,
    @Param("CustomerCode") String CustomerCode,
    @Param("VendorCode") String VendorCode,
    @Param("ProjectNO") String ProjectNO,
    @Param("OrderBy") String OrderBy,
    @Param("Descending") String Descending,
    @Param("Offset") Integer Offset,
    @Param("Limit") Integer Limit
  );

  List<TobeTrackingPurchaseOrderLine> findTobeTrackingPurchaseOrderLineBySite(
    @Param("Sites") List<String> Sites,
    @Param("OrderType") String OrderType,
    @Param("CustomerCode") String CustomerCode,
    @Param("VendorCode") String VendorCode,
    @Param("ProjectNO") String ProjectNO,
    @Param("OrderBy") String OrderBy,
    @Param("Descending") String Descending,
    @Param("Offset") Integer Offset,
    @Param("Limit") Integer Limit
  );

  List<TobeTrackingReceiptLine> findTobeTrackingReceiptLineBySite(
    @Param("Sites") List<String> Sites,
    @Param("OrderType") String OrderType,
    @Param("CustomerCode") String CustomerCode,
    @Param("VendorCode") String VendorCode,
    @Param("ProjectNO") String ProjectNO,
    @Param("OrderBy") String OrderBy,
    @Param("Descending") String Descending,
    @Param("Offset") Integer Offset,
    @Param("Limit") Integer Limit
  );

  List<TobeTrackingNCLine> findTobeTrackingNCLineBySite(
    @Param("Sites") List<String> Sites,
    @Param("OrderType") String OrderType,
    @Param("CustomerCode") String CustomerCode,
    @Param("VendorCode") String VendorCode,
    @Param("ProjectNO") String ProjectNO,
    @Param("OrderBy") String OrderBy,
    @Param("Descending") String Descending,
    @Param("Offset") Integer Offset,
    @Param("Limit") Integer Limit
  );
}
