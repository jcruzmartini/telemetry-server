package com.techner.tau.server.domain.interfaces;

/**
 * Interfaz para marcar SMS que necesitan una respuesta de exito o fracaso por
 * parte de la estacion
 * 
 * @author juan
 * 
 */
public interface Responsable {
	/**
	 * Retorno si es necesario una respuesta por parte de la estacion
	 * 
	 * @return true en caso que necesite, false en otro caso
	 */
	boolean needsResponse();

	/**
	 * Setea el token de indentificacion de la operacion que se esta enviando,
	 * de manera que la estacion pueda responder adjuntando el token al final de
	 * la respuesta
	 * 
	 * @param tokenNumber
	 *            numero de identificacion
	 */
	void setTokenNumber(Integer tokenNumber);

	/**
	 * Setea el texto del mensaje con el token incluido en el mismo
	 * 
	 * @param textWithToken
	 *            texto + token
	 */
	void setText(String textWithToken);
}
