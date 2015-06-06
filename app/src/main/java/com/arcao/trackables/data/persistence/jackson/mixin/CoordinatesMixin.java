package com.arcao.trackables.data.persistence.jackson.mixin;

import com.arcao.geocaching.api.data.coordinates.Coordinates;
import com.fasterxml.jackson.annotation.JsonProperty;

abstract class CoordinatesMixin extends Coordinates {
	CoordinatesMixin(
					@JsonProperty("latitude") double latitude,
					@JsonProperty("longitude") double longitude) {
		super(latitude, longitude);
	}
}
