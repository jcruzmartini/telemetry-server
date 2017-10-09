package com.techner.tau.server.core.provider;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.google.inject.Provider;

/**
 * Provider para el pool de trabajos de envio de mails
 * 
 * @author juan
 * 
 */
public class MailerQueueProvider implements Provider<ThreadPoolExecutor> {

	@Override
	public ThreadPoolExecutor get() {
		return new ThreadPoolExecutor(10, 10, 1, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>());
	}

}
