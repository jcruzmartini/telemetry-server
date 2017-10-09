package com.techner.tau.server.services;

import java.util.List;

import com.google.inject.Inject;
import com.techner.tau.server.data.dao.interfaces.VariableDao;
import com.techner.tau.server.domain.model.Variable;

public class VariableService {

	private final VariableDao dao;

	@Inject
	public VariableService(VariableDao dao) {
		this.dao = dao;
	}

	public Variable getVariableByCode(String code) {
		return dao.findVariableByCode(code);
	}

	public List<String> getAllMeteorologicalVariableCodes() {
		return dao.getAllMeteorologicalVariableCodes();
	}

	public List<String> getAllNonMeteorologicalVariableCodes() {
		return dao.getAllNonMeteorologicalVariableCodes();
	}

	public List<String> getMeteorologicalVariablesEnableByStation(Integer idStation) {
		return dao.getMeteorologicalVariablesEnableByStation(idStation);
	}
}
