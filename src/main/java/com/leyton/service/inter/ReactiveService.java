
package com.leyton.service.inter;

import java.util.List;

public interface ReactiveService {

    void process(List<String> stream);

    void parallelProcess();

    default void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
