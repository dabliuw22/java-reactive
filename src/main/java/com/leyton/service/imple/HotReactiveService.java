
package com.leyton.service.imple;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.leyton.model.CustomObserver;
import com.leyton.service.inter.ReactiveService;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.schedulers.Schedulers;

@Service(
        value = "hotReactiveService")
public class HotReactiveService implements ReactiveService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HotReactiveService.class);

    @Override
    public void process(List<String> stream) {
        CompositeDisposable disposables = new CompositeDisposable();
        ConnectableObservable<Long> source = Observable.interval(1, TimeUnit.SECONDS).publish();
        Disposable disponsableOne = source.subscribeOn(Schedulers.computation())
                .doOnDispose(() -> LOGGER.info("DISPOSING-1...")).unsubscribeOn(Schedulers.io())
                .subscribe(
                        data -> LOGGER.info("HOT-1 DATA: {}, THREAD-ID: {}", data,
                                Thread.currentThread().getId()),
                        error -> LOGGER.error("HOT-1 ERROR: ", error));
        source.connect();

        disposables.add(disponsableOne);

        sleep(5000);
        Disposable disponsableTwo = source.subscribeOn(Schedulers.computation())
                .doOnDispose(() -> LOGGER.info("DISPOSING-2...")).unsubscribeOn(Schedulers.io())
                .subscribe(
                        data -> LOGGER.info("HOT-2 DATA: {}, THREAD-ID: {}", data,
                                Thread.currentThread().getId()),
                        error -> LOGGER.error("HOT-2 ERROR: ", error));
        sleep(5000);

        disposables.add(disponsableTwo);
        if (!disposables.isDisposed()) {
            LOGGER.info("DONE!");
            disposables.dispose();
        }
    }

    @Override
    public void parallelProcess() {
        ConnectableObservable<Long> source = Observable.interval(1, TimeUnit.SECONDS).publish();
        source.flatMap(
                data -> Observable.just(data).subscribeOn(Schedulers.computation()).map(item -> {
                    sleep(100);
                    return item;
                })).subscribe(new CustomObserver<Long>());
        source.connect();
    }

}
