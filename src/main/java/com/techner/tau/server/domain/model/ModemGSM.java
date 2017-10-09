package com.techner.tau.server.domain.model;

public class ModemGSM {

	/**
     */
	private Integer id;
	/**
     */
	private Boolean enable;
	/**
     */
	private String branch;
	/**
     */
	private String model;
	/**
     */
	private String port;
	/**
     */
	private Integer baudios;
	/**
     */
	private String protocol;
	/**
     */
	private String pinSim;
	/**
     */
	private SimCard simCard;
	/**
     */
	private boolean outbound;
	/**
     */
	private boolean inbound;

	/**
	 * @return
	 */
	public String getProtocol() {
		return protocol;
	}

	/**
	 * @param protocol
	 */
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	/**
	 * @return
	 */
	public boolean isOutbound() {
		return outbound;
	}

	/**
	 * @param outbound
	 */
	public void setOutbound(boolean outbound) {
		this.outbound = outbound;
	}

	/**
	 * @return
	 */
	public boolean isInbound() {
		return inbound;
	}

	/**
	 * @param inbound
	 */
	public void setInbound(boolean inbound) {
		this.inbound = inbound;
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

	/**
	 * @return
	 */
	public String getBranch() {
		return branch;
	}

	/**
	 * @param branch
	 */
	public void setBranch(String branch) {
		this.branch = branch;
	}

	/**
	 * @return
	 */
	public Integer getBaudios() {
		return baudios;
	}

	/**
	 * @param baudios
	 */
	public void setBaudios(Integer baudios) {
		this.baudios = baudios;
	}

	/**
	 * @return
	 */
	public String getModel() {
		return model;
	}

	/**
	 * @param model
	 */
	public void setModel(String model) {
		this.model = model;
	}

	@Override
	public String toString() {
		return "<<GSM MODEM>> ID:" + id + " Branch: " + getBranch() + " Model: " + getModel() + " Port: " + getPort();
	}

	/**
	 * @return
	 */
	public String getPinSim() {
		return pinSim;
	}

	/**
	 * @param pinSim
	 */
	public void setPinSim(String pinSim) {
		this.pinSim = pinSim;
	}

	/**
	 * @return
	 */
	public String getPort() {
		return port;
	}

	/**
	 * @param port
	 */
	public void setPort(String port) {
		this.port = port;
	}

	public Boolean getEnable() {
		return enable;
	}

	/**
	 * @param enable
	 */
	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

	/**
	 * @return
	 */
	public SimCard getSimCard() {
		return simCard;
	}

	/**
	 * @param simCard
	 */
	public void setSimCard(SimCard simCard) {
		this.simCard = simCard;
	}

}
