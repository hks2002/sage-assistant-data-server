/*********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                            *
 * @CreatedDate           : 2024-12-16 11:27:55                                                                      *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                            *
 * @LastEditDate          : 2024-12-25 14:53:33                                                                      *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                          *
 ********************************************************************************************************************/

package com.da.sageassistantserver;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import java.time.Duration;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class RateLimiterTest {

  @Test
  public void testRateLimit() {
    // define the limit 1 time per 10 minute
    Bandwidth limit = Bandwidth
      .builder()
      .capacity(5)
      .refillGreedy(5, Duration.ofMinutes(1))
      .build();
    // construct the bucket
    Bucket bucket = Bucket.builder().addLimit(limit).build();
    IntStream
      .rangeClosed(1, 10)
      .forEach(i -> {
        if (bucket.tryConsume(1)) {
          log.info("acquired");
        } else {
          log.info("blocked");
        }
      });
  }
}
