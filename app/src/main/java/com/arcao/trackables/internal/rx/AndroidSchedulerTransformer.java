package com.arcao.trackables.internal.rx;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AndroidSchedulerTransformer {
	public static <T> Observable.Transformer<T, T> get() {
		return observable -> observable.subscribeOn(Schedulers.io())
						.observeOn(AndroidSchedulers.mainThread());
	}
}
