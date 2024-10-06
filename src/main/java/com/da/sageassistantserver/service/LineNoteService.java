/**********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                             *
 * @CreatedDate           : 2024-06-02 21:34:24                                                                       *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                             *
 * @LastEditDate          : 2024-12-09 19:49:05                                                                       *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                           *
 *********************************************************************************************************************/

package com.da.sageassistantserver.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.da.sageassistantserver.dao.LineNoteMapper;
import com.da.sageassistantserver.model.LineNote;
import java.sql.Timestamp;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LineNoteService {

  @Autowired
  LineNoteMapper lineNoteMapper;

  public int addLineNote(LineNote lineNote) {
    lineNote.setCreate_at(new Timestamp(System.currentTimeMillis()));
    lineNote.setUpdate_at(new Timestamp(System.currentTimeMillis()));
    return lineNoteMapper.insert(lineNote);
  }

  public boolean updateLineNote(LineNote lineNote) {
    lineNote.setUpdate_at(new Timestamp(System.currentTimeMillis()));
    if (lineNote.getId() == null) {
      lineNote.setCreate_at(new Timestamp(System.currentTimeMillis()));
    }
    return lineNoteMapper.insertOrUpdate(lineNote);
  }

  public Integer deleteLineNoteById(Long id) {
    return lineNoteMapper.deleteById(id);
  }

  public Integer deleteLineNote(String project, String line) {
    LambdaQueryWrapper<LineNote> queryWrapper = new LambdaQueryWrapper<>();
    if (!project.isEmpty()) {
      queryWrapper.eq(LineNote::getProject, project);
    }
    if (!line.isEmpty()) {
      queryWrapper.eq(LineNote::getLine, line);
    }

    return lineNoteMapper.delete(queryWrapper);
  }

  public LineNote getLineNoteById(Long id) {
    return lineNoteMapper.selectById(id);
  }

  public List<LineNote> getLineNote(String id, String project, String line) {
    LambdaQueryWrapper<LineNote> queryWrapper = new LambdaQueryWrapper<>();
    if (!id.equals("-1")) {
      queryWrapper.eq(LineNote::getId, id);
    }
    if (!project.isEmpty()) {
      queryWrapper.eq(LineNote::getProject, project);
    }
    if (!line.isEmpty()) {
      queryWrapper.eq(LineNote::getLine, line);
    }
    queryWrapper.orderBy(true, false, LineNote::getId);

    return lineNoteMapper.selectList(queryWrapper);
  }

  public List<LineNote> getLineNoteByProjectJsonList(String ProjectJsonList) {
    return getLineNoteByProjectJsonList(ProjectJsonList);
  }

  public List<LineNote> getLineNoteByLineJsonList(String LineJsonList) {
    return lineNoteMapper.getLineNoteByLineJsonList(LineJsonList);
  }
}
