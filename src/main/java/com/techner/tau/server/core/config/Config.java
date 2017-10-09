package com.techner.tau.server.core.config;

import java.io.File;
import java.net.InetAddress;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;

/**
 * Clase base para todas las configuraciones necesarias
 * 
 */
public class Config {

    /**
     * Configuration
     */
    protected Configuration config;
    /**
     * Reload
     */
    private static final int RELOAD = 60000;

    /**
     * Expresion Regular : Caracter General para splitear strings
     */
    public static final String SPLIT_CHARACTER_GENERAL = "[|]";
    /**
     * Expresion Regular : Caracter de Variables para splitear strings
     */
    public static final String SPLIT_CHARACTER_VARIABLES = "[*]";
    /**
     * Expresion Regular : Caracter de Minutos/Horas/Segundos para splitear
     * strings
     */
    public static final String SPLIT_HOUR_MINUTES_VALUE = "[#]";
    /**
     * Expresion Regular : Caracter de Medidas para splitear strings
     */
    public static final String SPLIT_CHARACTER_MEASURE_OR_HOURSMINS_VALUE = "[:]";
    /**
     * Variable de entorno con ubicacion del archivo de configuracion
     */
    public static final String CONFIG_PATH_PROPERTY = "config.path";
    /**
     * Clave para a substituir por las diferentes variables meteorologicas
     */
    public static final String REGEXP_METEOROLOGIC_VARIABLES_KEY = "meteovar";
    /**
     * Clave para a substituir por las diferentes eventos que puede emitir la
     * estacion
     */
    public static final String REGEXP_EVENT_VARIABLES_KEY = "events";
    /**
     * Clave para a substituir por las diferentes tipos de alertas
     */
    public static final String REGEXP_ALERT_TYPES_KEY = "alerttypes";
    /**
     * Caracter General para splitear strings
     */
    public static final String GENERIC_SEPARATOR = "|";
    /**
     * Caracter General para indicar que una respuesta ha sido SATISFACTORIA
     */
    public static final String RESPONSE_SUCCESS = "1";
    /**
     * Nombre del hostname donde esta corriendo la app
     */
    private final String hostname;

    /**
     * Constructor
     * 
     * @param file
     *            File to read configuration settings from
     * @throws ConfigurationException
     *             in case of failure
     */
    public Config(String file) throws ConfigurationException {
        this(new File(file));
    }

    /**
     * Constructor
     * 
     * @param file
     *            File to read config settings from.
     * @throws ConfigurationException
     *             in case of failure
     */
    public Config(File file) throws ConfigurationException {
        if (!file.exists()) {
            throw new IllegalArgumentException("Archivo especificado inexistente");
        }
        XMLConfiguration xmlConfig = new XMLConfiguration(file);
        FileChangedReloadingStrategy reload = new FileChangedReloadingStrategy();
        reload.setRefreshDelay(RELOAD); // Check at most once every minute.
        xmlConfig.setReloadingStrategy(reload);
        config = new CompositeConfiguration(xmlConfig);
        hostname = getHostName();
    }

    /**
     * Retorna el tiempo de sleep para el demonio receptor
     * 
     * @return tiempo
     */
    public Long getDaemonReceptorSleepTime() {
        return config.getLong("daemon.receptor.sleeptime");
    }

    /**
     * Retorna el tiempo de sleep para el demonio enviador
     * 
     * @return tiempo
     */
    public Long getDaemonSenderSleepTime() {
        return config.getLong("daemon.sender.sleeptime");
    }

    /**
     * Retorna el tiempo de sleep para los hilos receptor
     * 
     * @return tiempo
     */
    public Long getThreadReceptorSleepTime() {
        return config.getLong("thread.receptor.sleeptime");
    }

    /**
     * Retorna el tiempo de sleep para los hilos enviadores
     * 
     * @return tiempo
     */
    public Long getThreadSenderSleepTime() {
        return config.getLong("thread.sender.sleeptime");
    }

    /**
     * Retorna el texto de consulta a enviar a la estacion
     * 
     * @return tiempo
     */
    public String getQueryText() {
        return config.getString("sms.query");
    }

    /**
     * Retorna el Endpoint de los servicios
     * 
     * @return endpoint
     */
    public String getEndpointServices() {
        return config.getString("services.url");
    }

    /**
     * Retorna el token pàra consultar los servicios
     * 
     * @return token
     */
    public String getTokenForServices() {
        return config.getString("services.token");
    }

    /**
     * Retorna la expresion regular para validar el envio de medidas diarias de
     * la estacion o respuestas a consultas del cliente o por operaciones
     * 
     * @return exp reg
     */
    public String getRegExpDailySummary() {
        return config.getString("regexp.dailysummary");
    }

    /**
     * Retorna la expresion regular para validar el envio de alertas por parte
     * de la estacion
     * 
     * @return exp reg
     */
    public String getRegExpAlert() {
        return config.getString("regexp.alert");
    }

    /**
     * Retorna la expresion regular para validar el envio de estado de alerta
     * por parte de la estacion
     * 
     * @return exp reg
     */
    public String getRegExpStatus() {
        return config.getString("regexp.status");
    }

    /**
     * Retorna la expresion regular para validar el envio de errores por parte
     * de la estacion
     * 
     * @return exp reg
     */
    public String getRegExpError() {
        return config.getString("regexp.error");
    }

    /**
     * Retorna la expresion regular para validar el envio de respuestas a una
     * operacion del servidor por parte de la estacion
     * 
     * @return exp reg
     */
    public String getRexExpReply() {
        return config.getString("regexp.reply");
    }

    /**
     * Retorna el codigo de error generico
     * 
     * @return codigo
     */
    public String getGenericErrorCode() {
        return config.getString("error.generic", "0");
    }

    /**
     * Retorna el template de los mails de informacion
     * 
     * @return nombre del template
     */
    public String getPeriodicalMesauresEmailTemplate() {
        return config.getString("email.template.periodicalmeasures.template");
    }

    /**
     * Retorna el template de los mails de errores
     * 
     * @return nombre del template
     */
    public String getErrorEmailTemplate() {
        return config.getString("email.template.error.template");
    }

    /**
     * Retorna el template de los mails de advertencia
     * 
     * @return nombre del template
     */
    public String getWarningEmailTemplate() {
        return config.getString("email.template.warning.template");
    }

    /**
     * Retorna el template de los mails de informacion
     * 
     * @return nombre del template
     */
    public String getInformationEmailTemplate() {
        return config.getString("email.template.information.template");
    }

    /**
     * Retorna el template de los mails de reporte de error
     * 
     * @return nombre del template
     */
    public String getErrorReportTempale() {
        return config.getString("email.template.reporterror.template");
    }

    /**
     * Retorna el asunto de los mails de error
     * 
     * @return asunto
     */
    public String getErrorEmailSubject() {
        return config.getString("email.template.error.subject");
    }

    /**
     * Retorna el asunto de los mails de informacion
     * 
     * @return asunto
     */
    public String getInformationEmailSubject() {
        return config.getString("email.template.information.subject");
    }

    /**
     * Retorna el asunto de los mails de advertencia
     * 
     * @return asunto
     */
    public String getWarningEmailSubject() {
        return config.getString("email.template.warning.subject");
    }

    /**
     * Retorna el asunto de los mails de informes periodicos de medidas
     * 
     * @return asunto
     */
    public String getPeriodicalMesauresEmailSubject() {
        return config.getString("email.template.periodicalmeasures.subject");
    }

    /**
     * Retorna el asunto de los mails de reporte de error
     * 
     * @return asunto
     */
    public String getErrorReportEmailSubject() {
        return config.getString("email.template.reporterror.subject");
    }

    /**
     * Retorna el hostname del servidor de mail
     * 
     * @return hostname de la cuenta de mail
     */
    public String getEmailHostName() {
        return config.getString("email.config.hostname");
    }

    /**
     * Retorna el nombre que aparecera en el mail enviado
     * 
     * @return nombre de la cuenta de mail
     */
    public String getEmailName() {
        return config.getString("email.config.name");
    }

    /**
     * Retorna si la cuenta de email necesita ssl
     * 
     * @return true si necesita ssl, false en otro caso
     */
    public boolean getEmailSSL() {
        return config.getBoolean("email.config.ssl");
    }

    /**
     * Retorna si la cuenta de email necesita tls
     * 
     * @return true si necesita tls, false en otro caso
     */
    public boolean getEmailTLS() {
        return config.getBoolean("email.config.tls");
    }

    /**
     * Retorna el puerto a utilizar en el servidor de mail
     * 
     * @return puerto
     */
    public int getEmailPort() {
        return config.getInt("email.config.port");
    }

    /**
     * Retorna la direccion de email de la cual saldran los mails
     * 
     * @return direccion de email de origen
     */
    public String getEmailFrom() {
        return config.getString("email.config.from");
    }

    /**
     * Retorna el password de la cuenta de correo
     * 
     * @return password
     */
    public String getEmailPassword() {
        return config.getString("email.config.password");
    }

    /**
     * Retorna el nombre de usuario de la cuenta de correo
     * 
     * @return username de la cuenta de correo
     */
    public String getEmailUsername() {
        return config.getString("email.config.username");
    }

    /**
     * Retorna la ubicacion donde se encontraran los templates de velocity
     * 
     * @return path
     */
    public String getTemplateLocation() {
        return config.getString("email.template.location");
    }

    /**
     * Determina que variables no se deben almacenar cuando el mensaje que llega
     * es una respuesta a una consulta por demanda, ya sea del tipo operaciòn o
     * del tipo consulta de cliente
     * 
     * @return set de identificadores de variables
     */
    public String[] getExcludedVariablesToSave() {
        return config.getStringArray("variables.excluded");
    }

    public String[] getAdminCels() {
        return config.getStringArray("admin.cel");
    }

    /**
     * Retorna el nombre del ambiente donde se esta corriendo la app
     * 
     * @return ambiente
     */
    public String getEnvironment() {
        return config.getString("environment");
    }

    /**
     * Retorna el email donde se debe reportar los errores
     * 
     * @return email
     */
    public String getEmailToReportError() {
        return config.getString("email.config.emailToReportError");
    }

    /**
     * Retorna el nombre de la maquina donde esta corriendo la app
     * 
     * @return hostname
     */
    public String getHostname() {
        return hostname;
    }

    /**
     * Metodo para obtener el nombre del hostname
     * 
     * @return hostname
     */
    private String getHostName() {
        String hostname;
        try {
            hostname = InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            hostname = "N/D";
        }
        return hostname;
    }
}
