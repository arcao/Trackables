package com.arcao.trackables.data.persistence;

import android.content.Context;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import au.com.gridstone.grex.GRexPersister;
import au.com.gridstone.grex.converter.Converter;
import rx.Observable;
import timber.log.Timber;

public class ClassPersister extends GRexPersister {
	private final File path;

	/**
	 * Create a new instances using a provided converter.
	 *
	 * @param context   Context used to determine file directory.
	 * @param dirName   The sub directory name to perform all read/write operations to.
	 * @param converter Converter used to serialize/deserialize objects.
	 */
	public ClassPersister(Context context, String dirName, Converter converter) {
		super(context, dirName, converter);

		path = context.getDir(dirName, Context.MODE_PRIVATE);
	}

	public Observable<List<String>> get() {
		return Observable.defer(() -> Observable.just(Arrays.asList(path.list())));
	}

	public void clean() {
		try {
			FileUtils.cleanDirectory(path);
		} catch (Throwable t) {
			Timber.e(t.getMessage(), t);
		}
	}

	public long size() {
		return path.length();
	}

	public long sizeInBytes() {
		return FileUtils.sizeOfDirectory(path);
	}
}
