
package com.leyton.service.imple;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.leyton.model.CustomObserver;
import com.leyton.service.inter.ReactiveService;

import io.reactivex.Observable;
import io.reactivex.observables.GroupedObservable;
import io.reactivex.schedulers.Schedulers;

@Primary
@Service
public class CouldReactiveService implements ReactiveService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CouldReactiveService.class);

    @Override
    public void process(List<String> stream) {
        Observable<String> source =
                Observable.fromIterable(stream).subscribeOn(Schedulers.computation());
        source.subscribe(
                data -> LOGGER.info("COULD DATA: {}, THREAD-ID: {}", data,
                        Thread.currentThread().getName()),
                error -> LOGGER.error("COULD ERROR: ", error), () -> LOGGER.info("COMPLETE!"));
    }

    @Override
    public void parallelProcess() {
        Observable<Integer> source = Observable.range(1, 50);
        source.flatMap(data -> Observable.just(data).subscribeOn(Schedulers.io()).map(item -> {
            sleep(100);
            return item;
        })).subscribe(new CustomObserver<Integer>());

        // another way to do it using the core of the machine.
        AtomicInteger count = new AtomicInteger(0);
        int sizeCore = Runtime.getRuntime().availableProcessors();
        Observable<GroupedObservable<Integer, Integer>> sourceGroup =
                source.groupBy(i -> count.incrementAndGet() % sizeCore);
        sourceGroup.flatMap(group -> group.observeOn(Schedulers.io()).map(item -> {
            sleep(100);
            return item;
        })).subscribe(new CustomObserver<Integer>());
    }
}
