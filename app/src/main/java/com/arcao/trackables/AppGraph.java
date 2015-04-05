package com.arcao.trackables;

import android.app.Application;

public interface AppGraph {
	void inject(App app);
	Application application();
}
