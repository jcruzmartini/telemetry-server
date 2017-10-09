package com.techner.tau.server.data.dao.interfaces;

import java.util.List;

import com.techner.tau.server.domain.model.Variable;

public interface VariableDao extends ObjectDao<Variable> {
	public Variable findVariableByCode(String code);

	public List<String> getAllMeteorologicalVariableCodes();

	public List<String> getAllNonMeteorologicalVariableCodes();

	public List<String> getMeteorologicalVariablesEnableByStation(Integer idStation);
}
