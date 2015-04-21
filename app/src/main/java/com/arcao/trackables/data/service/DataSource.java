package com.arcao.trackables.data.service;

public enum DataSource {
	/** From local disk cache only */
	LOCAL,
	/** From remote location including store result to local disk cache */
	REMOTE,
	/** From local disk cache, but if not present load from remote location with storing back to local cache */
	REMOTE_IF_NECESSARY,
	/** Retrieve from both local and remote, but local first if present. Result from remote stored back to local cache. */
	REFRESH,
}
