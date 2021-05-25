package org.example;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;


import java.io.File;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class TaskB {

    public AmazonS3 getConnectionObject(){
        AmazonS3 s3Credentials = AmazonS3ClientBuilder.standard()
                .withRegion(Regions.US_EAST_1)
                .build();

        return s3Credentials;
    }

    public void uploadFileToS3Bucket(String filePath, AmazonS3 s3Object, String bucketName){
        try{
            File file = new File(filePath);
            String objectKey = file.getName();

            PutObjectResult result = s3Object.putObject(bucketName, objectKey, file);
            System.out.format("File %s pushed to bucket %s \n", objectKey,bucketName);
        }catch(AmazonServiceException e) {
            System.out.println(e.getMessage());
        }
    }

    public Bucket createBucket(String bucketName, AmazonS3 s3Object){
        Bucket newBucket = null;

        if (s3Object.doesBucketExistV2(bucketName)) {
            System.out.format("Bucket %s already exists.\n", bucketName);
            newBucket = findBucket(s3Object, bucketName);
        } else {
            try {
                newBucket = s3Object.createBucket(bucketName);
                System.out.format("New bucket '%s' created.\n", bucketName);
            } catch (AmazonS3Exception e) {
                System.err.println(e.getErrorMessage());
            }
        }
        return newBucket;
    }

    public void setPublicAccess(String bucketName, AmazonS3 s3Object){
        try {
            PublicAccessBlockConfiguration accessObject = new PublicAccessBlockConfiguration();

            accessObject.setBlockPublicAcls(true);
            accessObject.setBlockPublicPolicy(true);
            accessObject.setIgnorePublicAcls(true);
            accessObject.setRestrictPublicBuckets(true);

            SetPublicAccessBlockRequest setPublicAccessBlockRequest = new SetPublicAccessBlockRequest();
            setPublicAccessBlockRequest.setBucketName(bucketName);
            setPublicAccessBlockRequest.setPublicAccessBlockConfiguration(accessObject);

            s3Object.setPublicAccessBlock(setPublicAccessBlockRequest);
            System.out.println("----- Public Access Disabled -----");

        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            System.exit(1);
        }
    }

    public void SetACL(String bucketName, AmazonS3 s3Object){
        AccessControlList acl = s3Object.getBucketAcl(bucketName);

        CanonicalGrantee cg =new CanonicalGrantee(acl.getOwner().getId()) ;

        Grant newGrant = new Grant(cg, Permission.Write);

        acl.grantAllPermissions(newGrant);

        s3Object.setBucketAcl(bucketName, acl);

        System.out.println("----- Full access to owner given -----");
    }

    public void moveObject(AmazonS3 s3Object, String bucket1, String bucket2, String fileName){
        //Copy file from bucket-1 to bucket-2
        s3Object.copyObject(bucket1, fileName, bucket2, fileName);

        //Delete from bucket-1
        s3Object.deleteObject(bucket1, fileName);

        System.out.format("File %s moved from %s to %s\n", fileName, bucket1, bucket2);
    }

    public Bucket findBucket(AmazonS3 s3Object, String bucketName){
        Bucket bucket = null;

        List<Bucket> buckets = s3Object.listBuckets();

        for (Bucket b : buckets) {
            if(b.getName().equals(bucketName)){
                return b;
            }
        }

        return bucket;
    }

    public Dictionary getFileFromBucket(AmazonS3 s3Object, String bucketName, String objectName){
        Dictionary<String, String> wordDict = new Hashtable<>();
        //getting data from s3 bucket
        String o = s3Object.getObjectAsString(bucketName, objectName);

        // making dict out of String data
        String[] lines = o.split(String.valueOf("\n"));
        for(int i=1 ; i< lines.length; i++){
            String[] pair = lines[i].split(String.valueOf(lines[i].charAt(1)));

            wordDict.put(pair[0] , pair[1]);
        }
        //System.out.println("Dictionary made from lookup file \n" + wordDict);
        return wordDict;
    }
}
