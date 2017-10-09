package com.techner.tau.server.data.dao.interfaces;

import java.util.List;

import com.techner.tau.server.domain.model.SMS;
import com.techner.tau.server.domain.model.SMSCustomerRequest;
import com.techner.tau.server.domain.model.SMSServerOperation;

public interface SmsDao extends ObjectDao<SMS> {
	public SMSCustomerRequest findLastSmsInWaitingForResponse(Integer id, Integer token);

	public SMS findFirstSMSWaiting();

	public Long countSMSRequestSentByCustomer(Integer customerId);

	public SMSServerOperation getOperationInWaitingResponse(String originator, Integer tokenNumber);

	public SMS getSMStInWaitingResponse(String originator, Integer tokenNumber);

	public SMS getSMSByRefNo(int refNo, String modemId);

	public int getRetriesQty(int smsId);
	
	public List<SMS> getAllSMSWaitingResponse();
}
