package com.arcao.trackables.exception;

import android.content.Context;
import com.arcao.trackables.App;
import com.arcao.trackables.AppComponent;
import dagger.Component;

@Component(dependencies = AppComponent.class, modules = ExceptionModule.class)
public interface ExceptionComponent {
	ExceptionHandler exceptionHandler();

	final class Initializer {
		public static ExceptionComponent init(Context context) {
			return DaggerExceptionComponent.builder()
							.appComponent(App.get(context).component())
							.build();
		}
		private Initializer() {} // No instances.
	}
}
