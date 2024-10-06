/*********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                            *
 * @CreatedDate           : 2024-12-25 10:46:20                                                                      *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                            *
 * @LastEditDate          : 2024-12-25 14:53:46                                                                      *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                          *
 ********************************************************************************************************************/

package com.da.sageassistantserver;

import java.util.Arrays;
import java.util.List;
import lombok.Data;
import org.junit.jupiter.api.Test;

public class StreamTest {

  @Data
  class DaysDiffRange {

    int start;
    int end;

    DaysDiffRange(int start, int end) {
      this.start = start;
      this.end = end;
    }
  }

  @Test
  public void Test1() {
    List<DaysDiffRange> list = Arrays.asList(
      new DaysDiffRange(1, 2),
      new DaysDiffRange(2, 3)
    );
    list.stream().forEach(o -> System.out.println(o.start));
    list.forEach(o -> System.out.println(o.start));
  }
}
