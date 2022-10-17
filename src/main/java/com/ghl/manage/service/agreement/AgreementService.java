package com.ghl.manage.service.agreement;

import com.ghl.manage.entity.agreement.AgreementLogRequestParam;
import com.ghl.manage.entity.agreement.AgreementRequestParam;
import com.ghl.manage.entity.agreement.MainFirstRequestParam;
import com.ghl.manage.entity.common.CommonResponse;

public interface AgreementService {

	CommonResponse addAgreement(AgreementRequestParam request);

	CommonResponse checkAgreeNum(AgreementRequestParam request);
	
	CommonResponse addAgreementLog(AgreementLogRequestParam request);

	CommonResponse getAgreement(String no);

	CommonResponse updateAgreement(AgreementRequestParam request);

	CommonResponse masterFirstPage(MainFirstRequestParam request);

	CommonResponse queryAgreement();

	CommonResponse getLog(String no);

}
