package com.techner.tau.server.core.guice;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.velocity.app.VelocityEngine;
import org.smslib.Service;

import twitter4j.Twitter;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.sun.jersey.api.client.Client;
import com.techner.tau.server.AplicationControl;
import com.techner.tau.server.core.annotation.EndpointURL;
import com.techner.tau.server.core.annotation.TokenForServices;
import com.techner.tau.server.core.calculator.alert.AlertCheckerTask;
import com.techner.tau.server.core.calculator.alert.AlertCheckerTaskImpl;
import com.techner.tau.server.core.calculator.measure.daily.loader.DailyCalculatedMeasureLoader;
import com.techner.tau.server.core.calculator.measure.daily.loader.DailyCalculatedMeasureLoaderTask;
import com.techner.tau.server.core.calculator.sunset.SunriseSunsetCalulatorLoader;
import com.techner.tau.server.core.calculator.sunset.SunriseSunsetCalulatorLoaderTask;
import com.techner.tau.server.core.config.Config;
import com.techner.tau.server.core.daemons.DaemonReceptor;
import com.techner.tau.server.core.daemons.DaemonSender;
import com.techner.tau.server.core.monitor.MonitoringStationChecker;
import com.techner.tau.server.core.monitor.MonitoringStationCheckerTask;
import com.techner.tau.server.core.monitor.MonitoringStationCreditChecker;
import com.techner.tau.server.core.monitor.MonitoringStationCreditCheckerTask;
import com.techner.tau.server.core.periodicalnotifications.PeriodicalNotificationsChecker;
import com.techner.tau.server.core.periodicalnotifications.PeriodicalNotificationsCheckerTask;
import com.techner.tau.server.core.periodicalnotifications.PeriodicalNotificationsSender;
import com.techner.tau.server.core.periodicalnotifications.PeriodicalNotificationsSenderTask;
import com.techner.tau.server.core.provider.JerseyClientProvider;
import com.techner.tau.server.core.provider.ServiceProvider;
import com.techner.tau.server.core.provider.TwitterProvider;
import com.techner.tau.server.core.provider.VelocityEngineProvider;
import com.techner.tau.server.core.threads.ThreadReceptorFactory;
import com.techner.tau.server.core.threads.ThreadSenderFactory;
import com.techner.tau.server.core.twitter.TwitterUpdateStatusTask;
import com.techner.tau.server.core.twitter.TwitterUpdateStatusTaskImpl;
import com.techner.tau.server.data.module.DaoModule;

/**
 * Modulo Guice de la aplicacion
 * 
 * @author juan
 * 
 */
public class SMSModule extends AbstractModule {

    private Config config;

    @Override
    protected void configure() {
        install(new ProcessorReceptorModule());
        install(new ProcessorSenderModule());
        install(new PostProcessorOperationModule());
        install(new DaoModule());
        install(new QueuesModule());
        install(new CalculatorsMeasureModule());
        install(new CalculatorsNotificationModule());
        install(new ErrorModule());
        install(new MailModule());
        bind(AplicationControl.class).in(Scopes.SINGLETON);
        bind(DaemonSender.class).in(Scopes.SINGLETON);
        bind(DaemonReceptor.class).in(Scopes.SINGLETON);

        // Config
        String pathFicCfg = System.getProperty(Config.CONFIG_PATH_PROPERTY);
        try {
            config = new Config(pathFicCfg);
        } catch (ConfigurationException ex) {
            throw new IllegalArgumentException("Archivo de configuracion no encontrado", ex);
        }
        bind(Config.class).toInstance(config);
        bindConstant().annotatedWith(EndpointURL.class).to(config.getEndpointServices());
        bindConstant().annotatedWith(TokenForServices.class).to(config.getTokenForServices());

        bind(Client.class).toProvider(JerseyClientProvider.class).in(Scopes.SINGLETON);
        bind(Twitter.class).toProvider(TwitterProvider.class);
        bind(VelocityEngine.class).toProvider(VelocityEngineProvider.class).in(Scopes.SINGLETON);
        bind(Service.class).toProvider(ServiceProvider.class).in(Scopes.SINGLETON);

        install(new FactoryModuleBuilder().build(ThreadSenderFactory.class));
        install(new FactoryModuleBuilder().build(ThreadReceptorFactory.class));
        install(new FactoryModuleBuilder().implement(SunriseSunsetCalulatorLoader.class,
                SunriseSunsetCalulatorLoaderTask.class).build(SunriseSunsetCalulatorLoader.Factory.class));
        install(new FactoryModuleBuilder().implement(TwitterUpdateStatusTask.class, TwitterUpdateStatusTaskImpl.class)
                .build(TwitterUpdateStatusTask.Factory.class));
        install(new FactoryModuleBuilder().implement(PeriodicalNotificationsChecker.class,
                PeriodicalNotificationsCheckerTask.class).build(PeriodicalNotificationsChecker.Factory.class));
        install(new FactoryModuleBuilder()
                .implement(MonitoringStationChecker.class, MonitoringStationCheckerTask.class).build(
                        MonitoringStationChecker.Factory.class));
        install(new FactoryModuleBuilder()
                .implement(MonitoringStationCreditChecker.class, MonitoringStationCreditCheckerTask.class).build(
                        MonitoringStationCreditChecker.Factory.class));
        install(new FactoryModuleBuilder().implement(AlertCheckerTask.class, AlertCheckerTaskImpl.class).build(
                AlertCheckerTask.Factory.class));
        install(new FactoryModuleBuilder().implement(PeriodicalNotificationsSender.class,
                PeriodicalNotificationsSenderTask.class).build(PeriodicalNotificationsSender.Factory.class));
        install(new FactoryModuleBuilder().implement(DailyCalculatedMeasureLoader.class,
                DailyCalculatedMeasureLoaderTask.class).build(DailyCalculatedMeasureLoader.Factory.class));
    }

}
