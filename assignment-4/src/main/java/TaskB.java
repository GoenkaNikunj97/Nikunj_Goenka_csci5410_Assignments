import com.amazonaws.AmazonServiceException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectResult;

import java.io.File;

public class TaskB {

    AmazonS3 s3Object;

    public TaskB(){
        AmazonS3 s3Credentials = AmazonS3ClientBuilder.standard()
                .withRegion(Regions.US_EAST_1)
                .build();
        s3Object = s3Credentials;
    }
    public void startPushingAtSetIntervals(String filePath, String bucketName){
        try{

            File file = new File(filePath);
            String objectKey = file.getName();

            PutObjectResult result = s3Object.putObject(bucketName, objectKey, file);
            System.out.format("File %s pushed to bucket %s \n", objectKey,bucketName);
        }catch(AmazonServiceException e) {
            System.out.println(e.getMessage());
        }
    }
}
