package com.ghl.manage.service.agreement;

import com.ghl.manage.entity.agreement.AgreementLogRequestParam;
import com.ghl.manage.entity.agreement.AgreementSideRequestParam;
import com.ghl.manage.entity.agreement.MainFirstRequestParam;
import com.ghl.manage.entity.common.CommonResponse;

public interface AgreementServiceSide {

	CommonResponse addAgreementSide(AgreementSideRequestParam request);

	CommonResponse addAgreementLog(AgreementLogRequestParam request);

	CommonResponse getAgreementSide(String no);

	CommonResponse updateAgreementSide(AgreementSideRequestParam request);

	CommonResponse flowerFirstPage(MainFirstRequestParam requeste);

	CommonResponse masterFlowerInfo(String no);

}
