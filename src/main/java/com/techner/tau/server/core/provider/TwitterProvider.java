package com.techner.tau.server.core.provider;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.techner.tau.server.core.config.Config;

public class TwitterProvider implements Provider<Twitter> {

	private final Config config;

	@Inject
	public TwitterProvider(Config config) {
		this.config = config;
	}

	@Override
	public Twitter get() {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true).setOAuthConsumerKey("S16PUuqqlhEVryOcpjqw")
				.setOAuthConsumerSecret("j99DYLMcsuemADSD7JpEAtPGjeZXY4ImFpSmofviaQ")
				.setOAuthAccessToken("1119307429-S8thKmqEtLjGLTPdypjHEf0A3XQdy4DB42RCU4j")
				.setOAuthAccessTokenSecret("q2T4xX5EkyUZA4QzxTBtMMNFFiStxOQnQRDPMGJJtY");
		TwitterFactory tf = new TwitterFactory(cb.build());
		return tf.getInstance();
	}

}
