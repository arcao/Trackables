package com.arcao.trackables.data.persistence.jackson.mixin;

import com.arcao.geocaching.api.data.ImageData;
import com.arcao.geocaching.api.data.TrackableLog;
import com.arcao.geocaching.api.data.User;
import com.arcao.geocaching.api.data.coordinates.Coordinates;
import com.arcao.geocaching.api.data.type.TrackableLogType;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;

abstract class TrackableLogMixin extends TrackableLog {
	TrackableLogMixin(
					@JsonProperty("cacheID") int cacheID,
					@JsonProperty("code") String code,
					@JsonProperty("id") int id,
					@JsonProperty("images") List<ImageData> images,
					@JsonProperty("archived") boolean archived,
					@JsonProperty("guid") String guid,
					@JsonProperty("text") String text,
					@JsonProperty("type") TrackableLogType type,
					@JsonProperty("loggedBy") User loggedBy,
					@JsonProperty("created") Date created,
					@JsonProperty("updatedCoordinates") Coordinates updatedCoordinates,
					@JsonProperty("url") String url,
					@JsonProperty("visited") Date visited) {
		super(cacheID, code, id, images, archived, guid, text, type, loggedBy, created, updatedCoordinates, url, visited);
	}
}
