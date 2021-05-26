package org.example;

import java.sql.Connection;
import java.util.*;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;

public class App {

    public static void main( String[] args ) {
        Scanner sc = new Scanner(System.in);
        int userSelection = 0;

        String filePath = "N:/STUDIES/MACS/SEM-3/5410 - Serverless Data Processing/Assignements/A1/Nikunj.txt";

        /* Set Bucket Name */
        String bucket1 = "task-b-bucket-1";
        String bucket2 = "task-b-bucket-2";

        TaskB taskBObject = new TaskB();
        TaskC taskCObject = new TaskC();
        AmazonS3 s3Credentials = taskBObject.getConnectionObject();
        Connection con = taskCObject.connectToSql();

        System.out.println("============================================================");
        System.out.println("===================Serverless Assignment-1==================");
        System.out.println("============================================================");

        while(userSelection != -1) {
            System.out.println("");
            System.out.println("============================================================");
            System.out.println("Choose task to do from Assignment-1");
            System.out.println("1-->Task B(b) [push 'Nikunj.txt' to Bucket-1]");
            System.out.println("2-->Task B(c) [create Bucket-2 and set access permission]");
            System.out.println("3-->Task B(d) [move file from bucket-1 to bucket-2]");
            System.out.println("4-->Task c(c) [insert userId and Password]");
            System.out.println("5-->Task c(d) [fetch password based on userId]");
            userSelection = sc.nextInt();
            if (userSelection == 1) {

                taskBObject.uploadFileToS3Bucket(filePath,s3Credentials,bucket1);

            } else if (userSelection == 2) {
                // creating bucket
                Bucket bucket = taskBObject.createBucket(bucket2,s3Credentials);

                //Setting Access to "disable public access”
                taskBObject.setPublicAccess(bucket.getName(), s3Credentials);
                taskBObject.SetACL(bucket.getName(), s3Credentials);
            } else if (userSelection == 3) {
                taskBObject.moveObject(s3Credentials, bucket1, bucket2, "Nikunj.txt");
            } else if (userSelection == 4) {
                System.out.println("Enter userId");
                int userId = sc.nextInt();

                System.out.println("Enter Password");
                String password = String.valueOf(sc.next());

                taskCObject.insertToDB(con, password , userId);
            } else if (userSelection == 5) {
                System.out.println("Enter userId");
                int userId = sc.nextInt();
                taskCObject.getPassword(con, userId);
            }
        }
    }
}
