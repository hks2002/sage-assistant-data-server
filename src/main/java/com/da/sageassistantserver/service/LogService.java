/**********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                             *
 * @CreatedDate           : 2024-06-02 21:34:24                                                                       *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                             *
 * @LastEditDate          : 2024-12-25 14:45:07                                                                       *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                           *
 *********************************************************************************************************************/

package com.da.sageassistantserver.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.da.sageassistantserver.dao.LogMapper;
import com.da.sageassistantserver.dao.LogRawMapper;
import com.da.sageassistantserver.dao.LogTemplateMapper;
import com.da.sageassistantserver.model.Log;
import com.da.sageassistantserver.model.LogFinal;
import com.da.sageassistantserver.model.LogRaw;
import com.da.sageassistantserver.model.LogTemplate;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogService {

  @Autowired
  LogTemplateMapper logTemplateMapper;

  @Autowired
  LogMapper logMapper;

  @Autowired
  LogRawMapper logRawMapper;

  public List<LogFinal> getLogs(
    @Param("TCode") String TCode,
    @Param("TGroup") String TGroup,
    @Param("DateFrom") String DateFrom,
    @Param("DateTo") String DateTo,
    @Param("Language") String Language
  ) {
    List<LogRaw> logs = logRawMapper.findLogs(
      TCode,
      TGroup,
      DateFrom,
      DateTo,
      Language
    );
    List<LogFinal> logFinals = new ArrayList<>();

    for (LogRaw log : logs) {
      LogFinal logFinal = new LogFinal();

      logFinal.setId(log.getId());
      logFinal.setTCode(log.getTCode());
      logFinal.setTGroup(log.getTGroup());
      // Use Optional to handle null values
      Optional<String> definition = Optional.ofNullable(log.getTDefinition());
      definition.ifPresentOrElse(
        def -> {
          String logMessage = def
            .replace("{0}", Optional.ofNullable(log.getV0()).orElse(""))
            .replace("{1}", Optional.ofNullable(log.getV1()).orElse(""))
            .replace("{2}", Optional.ofNullable(log.getV2()).orElse(""))
            .replace("{3}", Optional.ofNullable(log.getV3()).orElse(""))
            .replace("{4}", Optional.ofNullable(log.getV4()).orElse(""))
            .replace("{5}", Optional.ofNullable(log.getV5()).orElse(""))
            .replace("{6}", Optional.ofNullable(log.getV6()).orElse(""))
            .replace("{7}", Optional.ofNullable(log.getV7()).orElse(""))
            .replace("{8}", Optional.ofNullable(log.getV8()).orElse(""))
            .replace("{9}", Optional.ofNullable(log.getV9()).orElse(""));
          logFinal.setLog(logMessage);
        },
        () -> logFinal.setLog("")
      );
      logFinal.setLog_at(log.getLog_at());

      logFinals.add(logFinal);
    }

    return logFinals;
  }

  public List<LogRaw> getUserLogs(
    @Param("TCode") String TCode,
    @Param("User") String User,
    @Param("Offset") Integer Offset,
    @Param("Limit") Integer Limit
  ) {
    return logRawMapper.findUserLogs(TCode, User, Offset, Limit);
  }

  public boolean addLogTemplate(LogTemplate logTemplate) {
    return (logTemplateMapper.insert(logTemplate) == 0) ? false : true;
  }

  public boolean addLogTemplate(
    String TCode,
    String TGroup,
    String TDefinition,
    String TDefinition_en,
    String TDefinition_zh
  ) {
    LogTemplate logTemplate = new LogTemplate();
    logTemplate.setTemplate_code(TCode);
    logTemplate.setTemplate_group(TGroup);
    logTemplate.setTemplate_definition(TDefinition);
    logTemplate.setTemplate_definition_en(TDefinition_en);
    logTemplate.setTemplate_definition_zh(TDefinition_zh);
    return addLogTemplate(logTemplate);
  }

  public void addLog(Log log) {
    CompletableFuture.runAsync(() -> {
      log.setLog_at(new Timestamp(System.currentTimeMillis()));
      logMapper.insert(log);
    });
  }

  public void addLog(
    String TCode,
    String v0,
    String v1,
    String v2,
    String v3,
    String v4,
    String v5,
    String v6,
    String v7,
    String v8,
    String v9
  ) {
    LambdaQueryWrapper<LogTemplate> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(LogTemplate::getTemplate_code, TCode);

    LogTemplate logTemplate = logTemplateMapper.selectOne(queryWrapper);

    if (logTemplate == null) {
      return;
    }

    Log log = new Log();
    log.setTemplate_id(logTemplate.getId());
    log.setV0(v0);
    log.setV1(v1);
    log.setV2(v2);
    log.setV3(v3);
    log.setV4(v4);
    log.setV5(v5);
    log.setV6(v6);
    log.setV7(v7);
    log.setV8(v8);
    log.setV9(v9);
    log.setLog_at(new Timestamp(System.currentTimeMillis()));
    addLog(log);
  }

  public void addLog(
    String TCode,
    String v0,
    String v1,
    String v2,
    String v3,
    String v4,
    String v5,
    String v6,
    String v7,
    String v8
  ) {
    addLog(TCode, v0, v1, v2, v3, v4, v5, v6, v7, v8, null);
  }

  public void addLog(
    String TCode,
    String v0,
    String v1,
    String v2,
    String v3,
    String v4,
    String v5,
    String v6,
    String v7
  ) {
    addLog(TCode, v0, v1, v2, v3, v4, v5, v6, v7, null, null);
  }

  public void addLog(
    String TCode,
    String v0,
    String v1,
    String v2,
    String v3,
    String v4,
    String v5,
    String v6
  ) {
    addLog(TCode, v0, v1, v2, v3, v4, v5, v6, null, null, null);
  }

  public void addLog(
    String TCode,
    String v0,
    String v1,
    String v2,
    String v3,
    String v4,
    String v5
  ) {
    addLog(TCode, v0, v1, v2, v3, v4, v5, null, null, null, null);
  }

  public void addLog(
    String TCode,
    String v0,
    String v1,
    String v2,
    String v3,
    String v4
  ) {
    addLog(TCode, v0, v1, v2, v3, v4, null, null, null, null, null);
  }

  public void addLog(String TCode, String v0, String v1, String v2, String v3) {
    addLog(TCode, v0, v1, v2, v3, null, null, null, null, null, null);
  }

  public void addLog(String TCode, String v0, String v1, String v2) {
    addLog(TCode, v0, v1, v2, null, null, null, null, null, null, null);
  }

  public void addLog(String TCode, String v0, String v1) {
    addLog(TCode, v0, v1, null, null, null, null, null, null, null, null);
  }

  public void addLog(String TCode, String v0) {
    addLog(TCode, v0, null, null, null, null, null, null, null, null, null);
  }

  public void addLog(String TCode) {
    addLog(TCode, null, null, null, null, null, null, null, null, null, null);
  }
}
