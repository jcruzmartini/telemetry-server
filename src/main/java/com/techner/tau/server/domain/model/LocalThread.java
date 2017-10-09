package com.techner.tau.server.domain.model;

public class LocalThread {

	/**
     */
	private Integer id;
	/**
     */
	private String name;
	/**
     */
	private ModemGSM modem;
	/**
     */
	private boolean enable;
	/**
     */
	private String type;

	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return
	 */
	public boolean isEnable() {
		return enable;
	}

	/**
	 * @param enable
	 */
	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	/**
	 * @return
	 */
	public ModemGSM getModem() {
		return modem;
	}

	/**
	 * @param modem
	 */
	public void setModem(ModemGSM modem) {
		this.modem = modem;
	}

	/**
	 * @return
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	public static enum Type {

		RECEPTOR("R"), SENDER("S");
		/**
         */
		private String type;

		private Type(String type) {
			this.type = type;
		}

		/**
		 * @return
		 */
		public String getType() {
			return type;
		}
	}
}
