package com.techner.tau.server.core.provider;

import org.smslib.Service;

import com.google.inject.Provider;

/**
 * SMSLib service provider
 * 
 * @author juan
 * 
 */
public class ServiceProvider implements Provider<Service> {

	@Override
	public Service get() {
		return Service.getInstance();
	}

}
