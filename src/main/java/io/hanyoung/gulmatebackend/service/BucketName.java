package io.hanyoung.gulmatebackend.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BucketName {

    FAMILY_PROFILE("gulmate-image-upload-123");

    private final String bucketName;

}
