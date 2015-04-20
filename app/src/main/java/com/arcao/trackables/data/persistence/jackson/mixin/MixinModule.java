package com.arcao.trackables.data.persistence.jackson.mixin;

import com.arcao.geocaching.api.data.ImageData;
import com.arcao.geocaching.api.data.Trackable;
import com.arcao.geocaching.api.data.TrackableLog;
import com.arcao.geocaching.api.data.User;
import com.arcao.geocaching.api.data.coordinates.Coordinates;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class MixinModule extends SimpleModule {
	public MixinModule() {
		super("MixinModule", Version.unknownVersion());
	}

	@Override
	public void setupModule(SetupContext context)
	{
		context.setMixInAnnotations(Coordinates.class, CoordinatesMixin.class);
		context.setMixInAnnotations(ImageData.class, ImageDataMixin.class);
		context.setMixInAnnotations(TrackableLog.class, TrackableLogMixin.class);
		context.setMixInAnnotations(Trackable.class, TrackableMixin.class);
		context.setMixInAnnotations(User.class, UserMixin.class);
	}}
