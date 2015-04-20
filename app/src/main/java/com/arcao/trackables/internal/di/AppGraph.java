package com.arcao.trackables.internal.di;

import com.arcao.trackables.data.service.AccountService;
import com.arcao.trackables.data.service.GeocacheService;
import com.arcao.trackables.data.service.TrackableService;
import com.arcao.trackables.exception.ExceptionHandler;
import com.arcao.trackables.preference.PreferenceHelper;
import com.squareup.picasso.Picasso;

public interface AppGraph {
	// from DataModule
	Picasso picasso();

	// from ExceptionModule
	ExceptionHandler exceptionHandler();

	// from PreferenceModule
	PreferenceHelper preferenceHelper();

	// from data/service
	AccountService accountService();
	TrackableService trackableService();
	GeocacheService geocacheService();
}
