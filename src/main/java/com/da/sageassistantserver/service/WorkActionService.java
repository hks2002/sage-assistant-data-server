/**********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                             *
 * @CreatedDate           : 2024-06-02 21:34:24                                                                       *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                             *
 * @LastEditDate          : 2024-12-25 14:48:08                                                                       *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                           *
 *********************************************************************************************************************/

package com.da.sageassistantserver.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.da.sageassistantserver.dao.WorkActionMapper;
import com.da.sageassistantserver.model.WorkAction;
import com.da.sageassistantserver.model.WorkActionCnt;
import java.sql.Timestamp;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WorkActionService {

  @Autowired
  WorkActionMapper workActionMapper;

  public int addWorkAction(WorkAction workAction) {
    workAction.setCreate_at(new Timestamp(System.currentTimeMillis()));
    workAction.setUpdate_at(new Timestamp(System.currentTimeMillis()));
    return workActionMapper.insert(workAction);
  }

  public boolean updateWorkAction(WorkAction workAction) {
    workAction.setUpdate_at(new Timestamp(System.currentTimeMillis()));
    if (workAction.getId() == null) {
      workAction.setCreate_at(new Timestamp(System.currentTimeMillis()));
    }
    return workActionMapper.insertOrUpdate(workAction);
  }

  public Integer deleteWorkAction(Long id) {
    return workActionMapper.deleteById(id);
  }

  public Integer deleteWorkActionByProject(String project) {
    LambdaQueryWrapper<WorkAction> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(WorkAction::getProject, project);

    return workActionMapper.delete(queryWrapper);
  }

  public WorkAction getWorkActionById(Long id) {
    return workActionMapper.selectById(id);
  }

  public List<WorkAction> getWorkAction(
    String id,
    String site,
    String type,
    String project,
    String user,
    String dateFrom,
    String dateTo
  ) {
    LambdaQueryWrapper<WorkAction> queryWrapper = new LambdaQueryWrapper<>();
    if (!id.equals("-1")) {
      queryWrapper.eq(WorkAction::getId, id);
    }
    if (!site.isEmpty()) {
      queryWrapper.eq(WorkAction::getSite, site);
    }
    if (!type.isEmpty()) {
      queryWrapper.eq(WorkAction::getAct, type);
    }
    if (!project.isEmpty()) {
      queryWrapper.eq(WorkAction::getProject, project);
    }
    if (!user.isEmpty()) {
      queryWrapper.like(WorkAction::getAct_user, user);
    }
    if (dateFrom != null) {
      queryWrapper.ge(WorkAction::getAct_date, dateFrom);
    }
    if (dateTo != null) {
      queryWrapper.le(WorkAction::getAct_date, dateTo);
    }
    queryWrapper.orderBy(true, false, WorkAction::getId);

    return workActionMapper.selectList(queryWrapper);
  }

  public List<WorkAction> getWorkActionByProjectJsonList(
    String ProjectJsonList
  ) {
    return getWorkActionByProjectJsonList(ProjectJsonList);
  }

  public List<WorkActionCnt> getWorkActionCntByProjectJsonList(
    String ProjectJsonList,
    String Site
  ) {
    List<WorkActionCnt> list = workActionMapper.getWorkActionCntByProjectJsonList(
      ProjectJsonList,
      Site
    );

    for (WorkActionCnt cnt : list) {
      cnt.setQty(cnt.getQty() == null ? 0 : cnt.getQty());
    }
    return list;
  }
}
