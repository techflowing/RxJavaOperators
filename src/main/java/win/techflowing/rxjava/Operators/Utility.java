package win.techflowing.rxjava.Operators;

import java.util.concurrent.TimeUnit;

import rx.Notification;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.schedulers.TimeInterval;
import rx.schedulers.Timestamped;

/**
 * 辅助类 操作符
 *
 * @author techflowing
 * @since 16/12/29 下午7:55
 */

public class Utility {

    public static void main(String[] args) throws InterruptedException {
//        materialize();
//        deMaterialize();
//        timestamp();
//        serialize();
//        cache();
//        observerOn();
//        subscriberOn();
//        doOnEach();
//        doOnComplete();
//        doOnError();
//        doOnTerminate();
//        doOnSubscriber();
//        doOnUnsubscribe();
//        finallyDo();
//        delay();
//        delaySubscription();
//        timeInterval();
//        using();
//        single();
//        singleOrDefault();
//        repeat();
        repeatWhen();

        Thread.sleep(Long.MAX_VALUE);
    }

    /**
     * Materialize操作符将OnNext/OnError/OnComplete都转化为一个Notification对象并按照原来的顺序发射出来
     */
    private static void materialize() {
        Observable.just(1, 2, 3)
                .materialize()
                .subscribe(new Subscriber<Notification<Integer>>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("materialize() complete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("materialize() error:" + e.getMessage());
                    }

                    @Override
                    public void onNext(Notification<Integer> integerNotification) {
                        System.out.println("materialize() onNext: kind:" + integerNotification.getKind()
                                + " value:" + integerNotification.getValue());
                    }
                });
    }

    /**
     * Dematerialize操作符是Materialize的逆向过程，它将Materialize转换的结果还原成它原本的形式
     */
    private static void deMaterialize() {
        Observable.just(1, 2, 3)
                .materialize()
                .dematerialize()
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("deMaterialize() complete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("deMaterialize() error:" + e.getMessage());
                    }

                    @Override
                    public void onNext(Object o) {
                        System.out.println("deMaterialize() onNext:" + o);
                    }
                });
    }

    /**
     * timestamp操作符，会将每个数据项给重新包装一下，加上了一个时间戳来标明每次发射的时间
     */
    private static void timestamp() {
        Observable.just(1, 2, 3, 4, 5, 6, 7)
                .timestamp()
                .subscribe(new Subscriber<Timestamped<Integer>>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("timestamp() complete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("timestamp() error:" + e.getMessage());
                    }

                    @Override
                    public void onNext(Timestamped<Integer> integerTimestamped) {
                        System.out.println("timestamp() onNext: timestamp: " + integerTimestamped.getTimestampMillis()
                                + ",value:" + integerTimestamped.getValue());
                    }
                });
    }

    /**
     * 一个Observable可以异步调用它的观察者的方法，可能是从不同的线程调用。这可能会让Observable行为不正确，
     * 它可能会在某一个onNext调用之前尝试调用onCompleted或onError方法，或者从两个不同的线程同时调用onNext方法。
     * 使用Serialize操作符，你可以纠正这个Observable的行为，保证它的行为是正确的且是同步的
     */
    private static void serialize() {
        Observable.just(1, 2, 3, 4, 5, 6)
                .serialize()
                .subscribe(getIntegerSubscriber("serialize()"));
    }

    /**
     * cache 操作符，记住Observable发射的数据序列并发射相同的数据序列给后续的订阅者
     */
    private static void cache() {
        // TODO: 16/12/30
    }

    /**
     * observerOn 操作符，指定一个观察者在哪个调度器上观察这个Observable，也就是指发射出的数据在哪个线程上使用
     */
    public static void observerOn() {
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                System.out.println("onSubscriber Thread: " + Thread.currentThread().getName());
                subscriber.onNext(1);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.computation())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("observerOn() complete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("observerOn() error:" + e.getMessage());
                    }

                    @Override
                    public void onNext(Integer integer) {
                        System.out.println("observerOn onNext:" + integer + "  ,Thread: "
                                + Thread.currentThread().getName());
                    }
                });
    }

    /**
     * SubscribeOn 用来指定Observable在哪个线程上运行,指定被观察者的线程，也就是指在哪个线程发送出数据
     */
    private static void subscriberOn() {
        //        observerOn();
    }

    /**
     * Do操作符就是给Observable的生命周期的各个阶段加上一系列的回调监听，
     * 当Observable执行到这个阶段的时候，这些回调就会被触发
     *
     */

    /**
     * DoOnEach可以给Observable加上这样的样一个回调：
     * Observable每发射一个数据的时候就会触发这个回调，不仅包括onNext还包括onError和onCompleted
     */
    private static void doOnEach() {
        Observable.just(1, 2, 3, 4, 5)
                .doOnEach(new Action1<Notification<? super Integer>>() {
                    @Override
                    public void call(Notification<? super Integer> notification) {
                        System.out.println("kind: " + notification.getKind() + ", value: " + notification.getValue());
                    }
                })
                .subscribe(getIntegerSubscriber("doOnEach()"));
    }

    /**
     * DoOnComplete会在OnCompleted发生的时候触发回调
     */
    private static void doOnComplete() {
        Observable.just(1, 2, 3, 4, 5)
                .doOnCompleted(new Action0() {
                    @Override
                    public void call() {
                        System.out.println("收到 doOnComplete() 方法的回调");
                    }
                })
                .subscribe(getIntegerSubscriber("doOnComplete()"));
    }

    /**
     * DoOnError会在OnError发生的时候触发回调，并将Throwable对象作为参数传进回调函数里
     */
    private static void doOnError() {
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                for (int i = 0; i < 5; i++) {
                    if (i == 4) {
                        subscriber.onError(new Throwable("doOnError()  错误"));
                    }
                    subscriber.onNext(i);
                }
                subscriber.onCompleted();
            }
        }).doOnError(new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                System.out.println("收到错误回调，message: " + throwable.getMessage());
            }
        }).subscribe(getIntegerSubscriber("doOnError()"));
    }

    /**
     * DoOnTerminate会在Observable结束前触发回调，无论是正常还是异常终止
     */
    private static void doOnTerminate() {
        Observable.just(1, 2, 3, 4, 5)
                .doOnTerminate(new Action0() {
                    @Override
                    public void call() {
                        System.out.println("doOnTerminate 回调");
                    }
                }).subscribe(getIntegerSubscriber("doOnTerminate()"));
    }

    /**
     * doOnSubscribe会在Subscriber进行订阅的时候触发回调
     */
    private static void doOnSubscriber() {
        Observable.just(1, 2, 3, 4, 5)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        System.out.println("doOnSubscriber() 回调");
                    }
                }).subscribe(getIntegerSubscriber("doOnSubscriber()"));
    }

    /**
     * doOnUnSubscribe则会在Subscriber进行反订阅的时候触发回调。
     * 当一个Observable通过OnError或者OnCompleted结束的时候，会反订阅所有的Subscriber。
     */
    private static void doOnUnsubscribe() {
        Observable.just(1, 2, 3, 4)
                .doOnUnsubscribe(new Action0() {
                    @Override
                    public void call() {
                        System.out.println("收到 doOnUnsubscribe 回调");
                    }
                })
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("doOnUnsubscriber() onComplete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("doOnUnsubscriber() onError:" + e.getMessage());
                    }

                    @Override
                    public void onNext(Integer integer) {
                        System.out.println("doOnUnsubscriber() onNext:" + integer);
                        if (integer == 3) {
                            this.unsubscribe();
                        }
                    }
                });

    }

    /**
     * finallyDo会在Observable结束后触发回调，无论是正常还是异常终止
     */
    private static void finallyDo() {
        Observable.just(1, 2, 3, 4, 5)
                .finallyDo(new Action0() {
                    @Override
                    public void call() {
                        System.out.println("收到 finallyDo 回调");
                    }
                })
                .subscribe(getIntegerSubscriber("finallyDo()"));
    }

    /**
     * Delay操作符就是让发射数据的时机延后一段时间，这样所有的数据都会依次延后一段时间发射。
     * 在Rxjava中将其实现为Delay和DelaySubscription。
     * 不同之处在于Delay是延时数据的发射，而DelaySubscription是延时注册Subscriber
     */
    private static void delay() {
        Observable.just(1, 2, 3, 4, 5)
                .delay(2, TimeUnit.SECONDS)
                .subscribe(getIntegerSubscriber("delay()"));
    }

    /**
     * DelaySubscription是延时注册Subscriber
     */
    private static void delaySubscription() {
        Observable.just(1, 2, 3, 4, 5)
                .delaySubscription(2, TimeUnit.SECONDS)
                .subscribe(getIntegerSubscriber("delaySubscription()"));
    }

    /**
     * TimeInterval会拦截发射出来的数据，取代为前后两个发射两个数据的间隔时间。
     * 对于第一个发射的数据，其时间间隔为订阅后到首次发射的间隔
     */
    private static void timeInterval() {
        Observable.just(1, 2, 3, 4, 5, 6)
                .timeInterval()
                .subscribe(new Subscriber<TimeInterval<Integer>>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("timeInterval() complete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("timeInterval() error:" + e.getMessage());
                    }

                    @Override
                    public void onNext(TimeInterval<Integer> integerTimeInterval) {
                        System.out.println("timeInterval() onNext:" + integerTimeInterval.getValue() +
                                ",time: " + integerTimeInterval.getIntervalInMilliseconds());
                    }
                });
    }

    /**
     * Using操作符,创建一个在Observable生命周期内存活的资源，
     * 也可以这样理解：我们创建一个资源并使用它，用一个Observable来限制这个资源的使用时间，
     * 当这个Observable终止的时候，这个资源就会被销毁
     * <p>
     * Using需要使用三个参数，分别是：
     * 创建这个一次性资源的函数
     * 创建Observable的函数
     * 释放资源的函数
     * <p>
     */
    private static void using() {
        Observable.using(new Func0<Model>() {
            @Override
            public Model call() {
                return new Model();
            }
        }, new Func1<Model, Observable<?>>() {
            @Override
            public Observable<?> call(Model model) {
                //                return Observable.just(1, 2, 3, 4, 5);
                return Observable.timer(1, 1, TimeUnit.SECONDS).take(5);
            }
        }, new Action1<Model>() {
            @Override
            public void call(Model model) {
                model.release();
            }
        }).subscribe(new Subscriber<Object>() {
            @Override
            public void onCompleted() {
                System.out.println("using() complete");
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("using() error:" + e.getMessage());
            }

            @Override
            public void onNext(Object o) {
                System.out.println("using() onNext:" + o.toString());
            }
        });
    }

    private static class Model {
        public Model() {
            System.out.println("创建了Model");
        }

        public void release() {
            System.out.println("释放了Model");
        }
    }

    /**
     * 强制返回单个数据，否则抛出异常
     * 1. 返回单个数据，有多个抛出异常
     * 2. 返回满足条件的单个数据，有多个抛出异常
     */
    private static void single() {
        Observable.just(1, 2, 3, 4, 5)
                //        Observable.just(1)
                //                .single()
                .single(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        return integer > 2;
                    }
                })
                .subscribe(getIntegerSubscriber("single()"));
    }

    /**
     * singleOrDefault 操作符，与single相似，不同的是发出的数据为空时返回defaultValue
     * 1. 当源Observer 发出的数据为空时，才返回默认值
     * 2. 当发出的数据 满足条件的个数多余一个，抛出异常，没有满足条件时，返回默认值，
     */
    private static void singleOrDefault() {
        //        Integer[] items = new Integer[0];
        Integer[] items = new Integer[]{1, 2, 3, 4, 5};
        Observable.from(items)
                .singleOrDefault(0, new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        return integer > 10;
                    }
                })
                //                .singleOrDefault(0)
                .subscribe(getIntegerSubscriber("singleOrDefault()"));
    }

    /**
     * repeat 操作符，重复执行，连续发送5次1
     */
    private static void repeat() {
        Observable.just(1).repeat(5).subscribe(getIntegerSubscriber("repeat()"));
    }

    /**
     * repeatWhen操作符是对某一个Observable，有条件地重新订阅从而产生多次结果
     * <p>
     * .repeatWhen() 响应onComplete作为重试条件
     * 因为onCompleted没有类型，所有输入变为Observable<Void>。
     */
    // TODO: 16/12/30 不是很懂
    private static void repeatWhen() {
        Observable.interval(2, TimeUnit.SECONDS)
                .limit(5)
                .repeatWhen(new Func1<Observable<? extends Void>, Observable<?>>() {
                    @Override
                    public Observable<?> call(Observable<? extends Void> observable) {
                        return observable.zipWith(Observable.range(1, 3), new Func2<Void, Integer, Integer>() {
                            @Override
                            public Integer call(Void aVoid, Integer integer) {
                                System.out.println("重复订阅:" + integer);
                                return integer;
                            }
                        });
                    }
                }).subscribe(getLongSubscriber("repeatWhen()"));
    }

    /**
     * 获取 Long 类型的 Subscribe
     *
     * @param funcName
     * @return
     */
    static Subscriber<Long> getLongSubscriber(final String funcName) {
        return new Subscriber<Long>() {
            @Override
            public void onCompleted() {
                System.out.println(funcName + " complete");
            }

            @Override
            public void onError(Throwable e) {
                System.out.println(funcName + " error:" + e.getMessage());
            }

            @Override
            public void onNext(Long aLong) {
                System.out.println(funcName + System.currentTimeMillis() + " onNext:" + aLong);
            }
        };
    }

    /**
     * 获取 Subscriber
     *
     * @param funcName 调用的函数名称
     * @return Subscriber
     */
    private static Subscriber<Integer> getIntegerSubscriber(final String funcName) {
        return new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                System.out.println(funcName + " complete");
            }

            @Override
            public void onError(Throwable e) {
                System.out.println(funcName + " error:" + e.getMessage());
            }

            @Override
            public void onNext(Integer integer) {
                System.out.println(funcName + " onNext:" + integer);
            }
        };
    }
}
