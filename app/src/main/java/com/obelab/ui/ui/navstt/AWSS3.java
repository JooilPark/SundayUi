package com.obelab.ui.ui.navstt;


import android.util.Log;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.obelab.ui.MainActivity;

import java.io.ByteArrayInputStream;
import java.io.File;

/**
 * https://docs.ncloud.com/ko/storage/storage-8-1.html
 */
public class AWSS3 {
    private static final String TAG = AWSS3.class.getSimpleName();
    public String upload(File uploadData) {
        String resultMsg = "";
        final String endPoint = "https://kr.object.ncloudstorage.com";
        final String regionName = "kr-standard";
        final String accessKey = "Vf67HGnHdfU341twUemg";
        final String secretKey = "WuYZbtb1CmaSo5lc4LFfWIUUG73ZkLfzLKdpwzcU";

// S3 client
        /*final AmazonS3Client s3 = AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, regionName))
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                .build();*/
        AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey ,secretKey );
        ClientConfiguration clientConfig = new ClientConfiguration();
        clientConfig.setProtocol(Protocol.HTTP);

        AmazonS3 aS3 = new AmazonS3Client(awsCredentials, clientConfig);
        aS3.setEndpoint(endPoint);

        String bucketName = "obelab-stt";

// create folder
        String folderName = "obelab_stt/";

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(0L);
        objectMetadata.setContentType("application/x-directory");
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, folderName, new ByteArrayInputStream(new byte[0]), objectMetadata);

        try {
            aS3.putObject(putObjectRequest);
            resultMsg =  "Folder %s has been created = "+ folderName;
            Log.i(TAG , "[" + resultMsg + "]");
        } catch (AmazonS3Exception e) {
            e.printStackTrace();
            return e.getMessage();
        } /*catch (SdkClientException e) {
            e.printStackTrace();
            return e.getMessage();
        }*/


        try {
            aS3.putObject(bucketName, uploadData.getName(), uploadData);
            resultMsg =  "Object %s has been created = "+uploadData.getName();
            Log.i(TAG , "2[" + resultMsg + "]");
        } catch (AmazonS3Exception e) {
            e.printStackTrace();
            return e.getMessage();
        } /*catch (SdkClientException e) {
            e.printStackTrace();
            return e.getMessage();
        }*/
        return resultMsg;
    }
}
