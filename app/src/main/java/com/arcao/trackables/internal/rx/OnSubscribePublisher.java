package com.arcao.trackables.internal.rx;

import rx.Observable;
import rx.Subscriber;

public class OnSubscribePublisher<T> implements Observable.OnSubscribe<T> {
	private boolean complete = false;
	private T result;
	private Throwable resultException;

	@Override
	public synchronized void call(Subscriber<? super T> subscriber) {
		if (subscriber.isUnsubscribed()) {
			return;
		}

		try {
			while (!complete) {
				wait();
			}
		} catch (Exception e) {
			resultException = e;
		}

		if (resultException != null) {
			if (!subscriber.isUnsubscribed()) {
				subscriber.onError(resultException);
			}
		}

		if (result != null) {
			subscriber.onNext(result);
		}
		subscriber.onCompleted();
	}

	public synchronized void publish(T result) {
		complete = true;
		this.result = result;
		notifyAll();
	}

	public synchronized void publishException(Throwable t) {
		complete = true;
		resultException = t;
		notifyAll();
	}
}