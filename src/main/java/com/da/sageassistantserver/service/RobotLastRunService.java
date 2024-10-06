/**********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                             *
 * @CreatedDate           : 2024-06-02 21:34:24                                                                       *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                             *
 * @LastEditDate          : 2024-12-09 19:50:22                                                                       *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                           *
 *********************************************************************************************************************/

package com.da.sageassistantserver.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.da.sageassistantserver.dao.RobotLastRunMapper;
import com.da.sageassistantserver.model.RobotLastRun;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RobotLastRunService {

  @Autowired
  RobotLastRunMapper robotLastRunMapper;

  public void addLastRun(RobotLastRun robotLastRun) {
    robotLastRun.setLast_run(new Timestamp(System.currentTimeMillis()));
    robotLastRunMapper.insert(robotLastRun);
  }

  public void addLastRun(String msg) {
    RobotLastRun robotLastRun = new RobotLastRun();
    robotLastRun.setMsg(msg);
    addLastRun(robotLastRun);
  }

  public void updateLastRun(
    RobotLastRun robotLastRun,
    LambdaUpdateWrapper<RobotLastRun> wrapper
  ) {
    robotLastRun.setLast_run(new Timestamp(System.currentTimeMillis()));
    robotLastRunMapper.update(robotLastRun, wrapper);
  }

  public void updateLastRun(String msg) {
    LambdaUpdateWrapper<RobotLastRun> updateWrapper = new LambdaUpdateWrapper<>();
    updateWrapper.eq(RobotLastRun::getMsg, msg);

    RobotLastRun robotLastRun = new RobotLastRun();
    robotLastRun.setMsg(msg);
    updateLastRun(robotLastRun, updateWrapper);
  }

  /**
   * Retrieves the last run time for a given message. If the message has not been
   * run before, the current time is used.
   *
   * @param msg the message to retrieve the last run time for
   * @return the last run time for the message in the format "yyyy-MM-dd HH:mm:ss"
   *         in UTC time zone
   */
  public String getLastRun(String msg) {
    LambdaQueryWrapper<RobotLastRun> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(RobotLastRun::getMsg, msg);
    queryWrapper.orderBy(true, false, RobotLastRun::getId);

    List<RobotLastRun> robotLastRuns = robotLastRunMapper.selectList(
      queryWrapper
    );
    long unixTimestamp = 0L; // Unix timestamp in milliseconds

    if (robotLastRuns.size() > 0) {
      RobotLastRun robotLastRun = robotLastRuns.get(0);
      unixTimestamp = robotLastRun.getLast_run().getTime();
      updateLastRun(msg);
    } else {
      unixTimestamp = System.currentTimeMillis();
      addLastRun(msg);
    }

    ZonedDateTime utcDateTime = Instant
      .ofEpochMilli(unixTimestamp)
      .atZone(ZoneId.of("Asia/Shanghai"))
      .withZoneSameInstant(ZoneId.of("UTC"));
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
      "yyyy-MM-dd HH:mm:ss"
    );
    return utcDateTime.format(formatter);
  }
}
