package com.techner.tau.server.core.provider;

import java.util.Properties;

import org.apache.velocity.app.VelocityEngine;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.techner.tau.server.core.config.Config;

public class VelocityEngineProvider implements Provider<VelocityEngine> {

	private final Config config;

	@Inject
	public VelocityEngineProvider(Config config) {
		this.config = config;
	}

	@Override
	public VelocityEngine get() {
		Properties props = new Properties();
		props.setProperty("resource.loader", "file");
		props.setProperty("file.resource.loader.class",
				"org.apache.velocity.runtime.resource.loader.FileResourceLoader");
		props.setProperty("file.resource.loader.path", config.getTemplateLocation());
		props.setProperty("file.resource.loader.cache", "true");
		props.setProperty("file.resource.loader.modificationCheckInterval", "2");

		VelocityEngine ve = new VelocityEngine(props);
		ve.init();
		return ve;
	}

}
