/*********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                            *
 * @CreatedDate           : 2022-03-26 17:55:00                                                                      *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                            *
 * @LastEditDate          : 2024-12-25 14:33:26                                                                      *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                          *
 ********************************************************************************************************************/

package com.da.sageassistantserver.dao;

import com.da.sageassistantserver.model.InvoiceBody;
import com.da.sageassistantserver.model.InvoiceHeader;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface InvoiceMapper {
  InvoiceHeader findInvoiceHeaderByInvoiceNO(
    @Param("InvoiceNO") String InvoiceNO
  );

  List<InvoiceHeader> findInvoiceHeaderByFaPiao(@Param("FaPiao") String FaPiao);

  List<String> findInvoiceNOByInvoiceNO(
    @Param("InvoiceNO") String InvoiceNO,
    @Param("Count") Integer Count
  );

  List<InvoiceBody> findInvoiceBodyByInvoiceNO(
    @Param("InvoiceNO") String InvoiceNO
  );

  List<InvoiceBody> findInvoiceBodyByFaPiao(@Param("FaPiao") String FaPiao);
}
