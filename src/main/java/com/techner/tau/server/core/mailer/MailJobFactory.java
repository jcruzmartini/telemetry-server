package com.techner.tau.server.core.mailer;

import java.util.Set;

import com.google.inject.assistedinject.Assisted;

/**
 * Factoria para crear mail jobs
 * 
 * @author juan
 * 
 */
public interface MailJobFactory {
	/**
	 * Crea un nuevo objeto MailJob
	 * 
	 * @param emails
	 *            lista de emails destinatarios
	 * @param content
	 *            contenido del mail
	 * @param subject
	 *            asunto del mail
	 * @return objeto mailJob
	 */
	MailJob create(Set<String> emails, @Assisted("content") String content, @Assisted("subject") String subject);
}
