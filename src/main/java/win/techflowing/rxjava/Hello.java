package win.techflowing.rxjava;


import rx.Observable;
import rx.functions.Action1;

/**
 * @author techflowing
 * @since 16/12/28 下午2:03
 */

public class Hello {
    public static void main(String[] args) throws InterruptedException {
        sayHello("hello", "world");
        Thread.sleep(Long.MAX_VALUE);
    }

    private static void sayHello(String... names) {
        Observable.from(names).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                System.out.println(s);
            }
        });
    }
}
