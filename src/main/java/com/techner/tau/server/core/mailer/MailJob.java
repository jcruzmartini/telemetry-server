package com.techner.tau.server.core.mailer;

import java.util.Set;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.techner.tau.server.core.config.Config;

/**
 * Clase soporte para el envio de mail, unidad de envio
 * 
 * @author juan
 * 
 */
public class MailJob implements Runnable {

	/** slf4j logger */
	private static final Logger logger = LoggerFactory.getLogger(MailJob.class);
	/** Lista de emails receptores a ser utilizados para el envio */
	private final Set<String> emails;
	/** contenido del mail */
	private final String content;
	/** asunto del mail */
	private final String subject;
	/** Clase de configuracion */
	private final Config config;

	@Inject
	public MailJob(@Assisted Set<String> emails, @Assisted("content") String content,
			@Assisted("subject") String subject, Config config) {
		this.emails = emails;
		this.content = content;
		this.subject = subject;
		this.config = config;
	}

	@Override
	public void run() {
		try {
			HtmlEmail email = getConfiguredEmptyEmail();
			email.setHtmlMsg(content);
			email.setSubject(subject);
			for (String emailAdd : emails) {
				email.addTo(emailAdd);
			}
			email.send();
		} catch (Exception e) {
			logger.error("Error creando email", e);
		}

	}

	/**
	 * Crea un HTML email con la informacion necesaria para ser enviado
	 * 
	 * @return HTML email
	 * @throws EmailException
	 */
	private final HtmlEmail getConfiguredEmptyEmail() throws EmailException {
		HtmlEmail email = new HtmlEmail();
		email.setAuthentication(config.getEmailUsername(), config.getEmailPassword());
		email.setFrom(config.getEmailFrom(), config.getEmailName());
		email.setHostName(config.getEmailHostName());
		email.setSmtpPort(config.getEmailPort());
		email.setSSL(config.getEmailSSL());
		email.setTLS(config.getEmailTLS());
		return email;
	}

}
