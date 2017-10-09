package com.techner.tau.server.core.provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.google.inject.Provider;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.techner.tau.common.module.JacksonModule;

/**
 * SMSLib service provider
 * 
 * @author juan
 * 
 */
public class JerseyClientProvider implements Provider<Client> {

	@Override
	public Client get() {
		JacksonJaxbJsonProvider provider = new JacksonJaxbJsonProvider();
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JacksonModule());
		provider.setMapper(mapper);
		ClientConfig config = new DefaultClientConfig();
		config.getClasses().add(provider.getClass());
		return Client.create(config);
	}

}
