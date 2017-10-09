package com.techner.tau.server.core.provider;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.google.inject.Provider;

/**
 * Provider para el pool de trabajos de calculos de medidas
 * 
 * @author juan
 * 
 */
public class TweetsQueueProvider implements Provider<ThreadPoolExecutor> {

	@Override
	public ThreadPoolExecutor get() {
		return new ThreadPoolExecutor(1, 10, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
	}

}
