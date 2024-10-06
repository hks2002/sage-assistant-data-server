/*********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                            *
 * @CreatedDate           : 2024-06-06 12:40:24                                                                      *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                            *
 * @LastEditDate          : 2024-12-25 14:53:54                                                                      *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                          *
 ********************************************************************************************************************/

package com.da.sageassistantserver;

import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class AsyncFutureTest {

  @Test
  public void testRun() throws InterruptedException {
    CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
      log.info("Run Second");
      return "Return in Future";
    });

    future.thenAccept(s -> log.info(s));

    log.info("Run first");

    CompletableFuture.supplyAsync(() -> {
      log.info("Run ....");
      return null;
    });
    Thread.sleep(3000);

    CompletableFuture.runAsync(() -> {
      log.info("Run third");
    });
  }
}
