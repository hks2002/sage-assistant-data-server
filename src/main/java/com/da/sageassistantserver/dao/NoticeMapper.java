/**********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                             *
 * @CreatedDate           : 2024-06-02 17:50:39                                                                       *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                             *
 * @LastEditDate          : 2024-12-25 14:34:02                                                                       *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                           *
 *********************************************************************************************************************/

package com.da.sageassistantserver.dao;

import com.da.sageassistantserver.model.DeadPurchaseLine;
import com.da.sageassistantserver.model.LongTimeNC;
import com.da.sageassistantserver.model.LongTimeNoQC;
import com.da.sageassistantserver.model.MixPJTInPO;
import com.da.sageassistantserver.model.MixPJTInWO;
import com.da.sageassistantserver.model.ProjectProfit;
import com.da.sageassistantserver.model.SuspectDuplicatedPO;
import com.da.sageassistantserver.model.SuspectDuplicatedRA;
import com.da.sageassistantserver.model.TobeDealWithOrderLine;
import com.da.sageassistantserver.model.TobeDelivery;
import com.da.sageassistantserver.model.TobeInvoice;
import com.da.sageassistantserver.model.TobePurchaseBom;
import com.da.sageassistantserver.model.TobeReceive;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface NoticeMapper {
  List<TobeDelivery> findNewSOSince(
    @Param("Site") String Site,
    @Param("Since") String Since
  );

  List<TobeDealWithOrderLine> findTobeDealWithOrderLines(
    @Param("Site") String Site,
    @Param("DiffDaysStart") Integer DiffDaysStart,
    @Param("DiffDaysEnd") Integer DiffDaysEnd
  );

  List<TobePurchaseBom> findTobePurchaseBom(
    @Param("Site") String Site,
    @Param("DiffDaysStart") Integer DiffDaysStart,
    @Param("DiffDaysEnd") Integer DiffDaysEnd
  );

  List<TobeReceive> findNoAckDatePO(@Param("Site") String Site);

  List<TobeReceive> findLongTimeNoReceive(
    @Param("Site") String Site,
    @Param("DiffDaysStart") Integer DiffDaysStart,
    @Param("DiffDaysEnd") Integer DiffDaysEnd
  );

  List<LongTimeNC> findLongTimeNC(
    @Param("Site") String Site,
    @Param("DiffDaysStart") Integer DiffDaysStart,
    @Param("DiffDaysEnd") Integer DiffDaysEnd
  );

  List<LongTimeNoQC> findLongTimeNoQC(
    @Param("Site") String Site,
    @Param("DiffDaysStart") Integer DiffDaysStart,
    @Param("DiffDaysEnd") Integer DiffDaysEnd
  );

  List<TobeReceive> findWrongProjectPO(@Param("Site") String Site);

  List<TobeInvoice> findLongTimeNoInvoice(
    @Param("Site") String Site,
    @Param("DiffDaysStart") Integer DiffDaysStart,
    @Param("DiffDaysEnd") Integer DiffDaysEnd
  );

  List<MixPJTInPO> mixPOProjectBetweenZHUAndYSH();

  List<MixPJTInWO> mixWOProjectBetweenZHUAndYSH();

  List<String> duplicatedWO(@Param("Site") String Site);

  List<SuspectDuplicatedRA> findNewRASince(
    @Param("Site") String Site,
    @Param("Since") String Since
  );

  List<TobeDelivery> findLongTimeNoDelivery(
    @Param("Site") String Site,
    @Param("DiffDaysStart") Integer DiffDaysStart,
    @Param("DiffDaysEnd") Integer DiffDaysEnd
  );

  List<TobeDealWithOrderLine> findNOBomServiceOrder(@Param("Site") String Site);

  List<DeadPurchaseLine> findDeadPurchaseLine(@Param("Site") String Site);

  List<ProjectProfit> findPreAnalysesProjectProfit(
    @Param("Site") String Site,
    @Param("Since") String Since,
    @Param("TargetRate") Float TargetRate
  );

  List<SuspectDuplicatedPO> findDuplicatedPOBySite(@Param("Site") String Site);

  List<SuspectDuplicatedRA> findDuplicatedRABySite(@Param("Site") String Site);
}
