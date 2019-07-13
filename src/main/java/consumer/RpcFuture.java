package consumer;


import java.util.Objects;
import java.util.concurrent.*;

/**
 * @Author: neo
 * @FirstInitial: 2019/7/13
 * @Description: ~
 */
public class RpcFuture<T> implements Future<T> {
    private T result;
    private Throwable error;
    private CountDownLatch latch = new CountDownLatch(1);

    public void success(T result) {
        this.result = result;
        latch.countDown();
    }

    public void fail(Throwable error) {
        this.error = error;
        latch.countDown();
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return false;
    }

    @Override
    public T get() throws InterruptedException, ExecutionException {
        // sync fetch data
        latch.await();
        if (error != null) {
            throw new ExecutionException(error);
        }
        return result;
    }

    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        latch.await(timeout, unit);
        if (error != null) {
            throw new ExecutionException(error);
        }
        return result;
    }
}
