
package com.leyton.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class CustomObserver<T> implements Observer<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomObserver.class);

    @Override
    public void onSubscribe(Disposable d) {
    }

    @Override
    public void onNext(T t) {
        LOGGER.info("RECIVE: {}, THREAD-NAME: {}", t, Thread.currentThread().getName());
    }

    @Override
    public void onError(Throwable e) {
        LOGGER.error("ERROR: ", e);
    }

    @Override
    public void onComplete() {
        LOGGER.info("COMPLETE!");
    }
}
