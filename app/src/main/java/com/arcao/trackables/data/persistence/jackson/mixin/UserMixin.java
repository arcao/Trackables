package com.arcao.trackables.data.persistence.jackson.mixin;

import com.arcao.geocaching.api.data.coordinates.Coordinates;
import com.arcao.geocaching.api.data.type.MemberType;
import com.fasterxml.jackson.annotation.JsonProperty;

abstract class UserMixin {
	UserMixin(
					@JsonProperty("id") long id,
					@JsonProperty("publicGuid") String publicGuid,
					@JsonProperty("userName") String userName,
					@JsonProperty("avatarUrl") String avatarUrl,
					@JsonProperty("homeCoordinates") Coordinates homeCoordinates,
					@JsonProperty("admin") boolean admin,
					@JsonProperty("memberType") MemberType memberType,
					@JsonProperty("findCount") int findCount,
					@JsonProperty("hideCount") int hideCount,
					@JsonProperty("galleryImageCount") int galleryImageCount) {
	}
}
