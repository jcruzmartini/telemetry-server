package com.techner.tau.server.core.guice;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.MapBinder;
import com.techner.tau.server.core.processor.sender.SenderProcessor;
import com.techner.tau.server.domain.interfaces.Message;

/**
 * Modulo para registrar los procesadores de envio para cada tipo de SMS
 * 
 * @author juan
 * 
 */
public class ProcessorSenderModule extends AbstractModule {

	@Override
	protected void configure() {
		MapBinder<String, SenderProcessor> mapbinder = MapBinder.newMapBinder(binder(), String.class,
				SenderProcessor.class);
		for (Message.Type type : Message.Type.values()) {
			// Algunos tipos de mensajes no tienen procesadores de envio
			if (null != type.getSenderProcessor()) {
				mapbinder.addBinding(type.getCode()).to(type.getSenderProcessor());
			}
		}
	}

}
