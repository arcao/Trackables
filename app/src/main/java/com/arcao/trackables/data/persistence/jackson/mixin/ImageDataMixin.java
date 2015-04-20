package com.arcao.trackables.data.persistence.jackson.mixin;

import com.arcao.geocaching.api.data.ImageData;
import com.fasterxml.jackson.annotation.JsonProperty;

abstract class ImageDataMixin extends ImageData {
	ImageDataMixin(
					@JsonProperty("description") String description,
					@JsonProperty("mobileUrl") String mobileUrl,
					@JsonProperty("name") String name,
					@JsonProperty("thumbUrl") String thumbUrl,
					@JsonProperty("url") String url) {
		super(description, mobileUrl, name, thumbUrl, url);
	}
}
