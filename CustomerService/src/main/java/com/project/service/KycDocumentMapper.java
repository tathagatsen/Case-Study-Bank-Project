package com.project.service;

import com.project.dto.KycDocumentDto;
import com.project.dto.KycDocumentDto2;
import com.project.model.KycDocument;

public final class KycDocumentMapper {

private KycDocumentMapper() {}

public static KycDocumentDto2 toDto(KycDocument k) {
if (k == null) return null;
// Prefer the scalar customerId field if you keep it, otherwise derive from the relation
Long customerId = k.getCustomerId();
if (customerId == null && k.getCustomer() != null) {
customerId = k.getCustomer().getCustomerId();
}

return KycDocumentDto2.builder()
.kycId(k.getKycId())
.customerId(customerId)
.documentType(k.getDocumentType())
.filePath(k.getFilePath())
.status(k.getStatus())
.uploadedAt(k.getUploadedAt())
.reviewedAt(k.getReviewedAt())
.reviewedBy(k.getReviewedBy())
.remarks(k.getRemarks())
.build();
}
}
