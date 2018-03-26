package win.techflowing.rxjava;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * @author techflowing
 * @since 17/2/6 下午4:23
 */

public class Test {
    public static void main(String[] args) {
//        test();
//        test01();
        test02();
        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void test01() {
        Observable.interval(2, TimeUnit.SECONDS).limit(5).
                repeatWhen(new Func1<Observable<? extends Void>, Observable<?>>() {
                    @Override
                    public Observable<?> call(Observable<? extends Void> observable) {
                        return observable.delay(3, TimeUnit.SECONDS);
                    }
                }).subscribe(new Observer<Long>() {
            @Override
            public void onCompleted() {
                System.out.println("------------->onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("------------->onError:");
            }

            @Override
            public void onNext(Long integer) {
                System.out.println("------------->onNext:" + integer);
            }
        });
    }

    public static void test02() {
        Observable.interval(0, 1, TimeUnit.SECONDS).limit(5).
                repeatWhen(new Func1<Observable<? extends Void>, Observable<?>>() {
                    @Override
                    public Observable<?> call(Observable<? extends Void> observable) {
                        return observable.delay(2, TimeUnit.SECONDS).limit(2);
                    }
                }).subscribe(new Observer<Long>() {
            @Override
            public void onCompleted() {
                System.out.println("------------->onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("------------->onError:");
            }

            @Override
            public void onNext(Long integer) {
                System.out.println("------------->onNext:" + integer);
            }
        });
    }


    public static void test() {
        Observable.interval(0, 2, TimeUnit.SECONDS).limit(5)
                .repeatWhen(new Func1<Observable<? extends Void>, Observable<?>>() {
                    @Override
                    public Observable<?> call(Observable<? extends Void> observable) {
                        return observable.delay(2, TimeUnit.SECONDS);
                    }
                }).subscribe(mLongSubscriber());
    }

    private static Subscriber<Long> mLongSubscriber() {
        return new Subscriber<Long>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Long aLong) {
                System.out.println(aLong);
                System.out.println(this.toString());
            }
        };
    }
}
