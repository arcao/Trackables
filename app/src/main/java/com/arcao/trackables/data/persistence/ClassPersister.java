package com.arcao.trackables.data.persistence;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import au.com.gridstone.grex.GRexPersister;
import au.com.gridstone.grex.converter.Converter;
import rx.Observable;
import timber.log.Timber;

public class ClassPersister extends GRexPersister {
	private final File directory;

	/**
	 * Create a new instance using a provided {@link Converter}.
	 *
	 * @param converter Converter used to serialize/deserialize objects, not
	 *                  null
	 * @param directory Directory to write/read files, not null. {@link
	 *                  File#isDirectory()} must return true on this parameter
	 */
	public ClassPersister(Converter converter, File directory) {
		super(converter, directory);
		this.directory = directory;
	}

	public Observable<List<String>> get() {
		return Observable.defer(() -> Observable.just(Arrays.asList(directory.list())));
	}

	public void clean() {
		try {
			FileUtils.cleanDirectory(directory);
		} catch (Throwable t) {
			Timber.e(t.getMessage(), t);
		}
	}

	public long size() {
		return directory.length();
	}

	public long sizeInBytes() {
		return FileUtils.sizeOfDirectory(directory);
	}
}
