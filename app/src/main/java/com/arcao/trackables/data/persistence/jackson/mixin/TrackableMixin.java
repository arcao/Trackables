package com.arcao.trackables.data.persistence.jackson.mixin;

import com.arcao.geocaching.api.data.ImageData;
import com.arcao.geocaching.api.data.Trackable;
import com.arcao.geocaching.api.data.TrackableLog;
import com.arcao.geocaching.api.data.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;

abstract class TrackableMixin extends Trackable {
	@JsonIgnore
	@Override
	public String getTrackablePage() {
		return super.getTrackablePage();
	}

	TrackableMixin(
					@JsonProperty("id") long id,
					@JsonProperty("guid") String guid,
					@JsonProperty("name") String name,
					@JsonProperty("goal") String goal,
					@JsonProperty("description") String description,
					@JsonProperty("trackableTypeName") String trackableTypeName,
					@JsonProperty("trackableTypeImage") String trackableTypeImage,
					@JsonProperty("owner") User owner,
					@JsonProperty("currentCacheCode") String currentCacheCode,
					@JsonProperty("currentOwner") User currentOwner,
					@JsonProperty("trackingNumber") String trackingNumber,
					@JsonProperty("created") Date created,
					@JsonProperty("allowedToBeCollected") boolean allowedToBeCollected,
					@JsonProperty("inCollection") boolean inCollection,
					@JsonProperty("archived") boolean archived,
					@JsonProperty("trackableLogs") List<TrackableLog> trackableLogs,
					@JsonProperty("images") List<ImageData> images) {
		super(id, guid, name, goal, description, trackableTypeName, trackableTypeImage, owner, currentCacheCode, currentOwner, trackingNumber, created, allowedToBeCollected, inCollection, archived, trackableLogs, images);
	}
}
