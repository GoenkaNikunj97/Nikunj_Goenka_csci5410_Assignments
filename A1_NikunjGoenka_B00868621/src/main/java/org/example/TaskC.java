package org.example;

import com.amazonaws.services.s3.AmazonS3;

import java.sql.*;
import java.util.Dictionary;
import java.util.Enumeration;

public class TaskC {

    TaskB taskBObject;
    AmazonS3 s3Object;
    Dictionary lookup = null;
    public TaskC(){
        taskBObject = new TaskB();
        s3Object = taskBObject.getConnectionObject();

    }

    public Connection connectToSql(){
        Connection con = null;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/assignment-1-task-c","root","root");

            System.out.println("Connected to the database");

        }catch(Exception e){ System.out.println(e);}
        return con;
    }

    public void insertToDB(Connection con, String password, int userId) {
        System.out.println("Password without encryption: "+ password);

        String encryptedPassword = getEncryptedPassword(password);
        System.out.println("Encrypted Password: "+ encryptedPassword);

        String query = "INSERT INTO usertable " + "VALUES (" +userId+", '"+encryptedPassword+"')";
        try{
        Statement stmt=con.createStatement();
        stmt.executeUpdate(query);
        System.out.println("Data pushed");
        } catch(Exception e) {
            System.out.println(e);
        }
    }

    public void getPassword(Connection con, int userId) {
        String query = "SELECT `Password` FROM `usertable` WHERE userID IN (" +userId+")";
        String password = "";
        try{
            Statement stmt=con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()){
                password = rs.getString(1);
            }
            System.out.println("Encrypted Password is: "+ password);
            String deCryptedPassword = getDecryptedPassword(password);
            System.out.println("Decrypted Password is: "+ deCryptedPassword);

        }catch(Exception e){ System.out.println(e);}
    }

    public String getEncryptedPassword(String password){

        String newPassword = "";

        //getting lookup file from S3
        if(lookup == null){
            lookup = taskBObject.getFileFromBucket(s3Object, "task-c-bucket", "Lookup.txt");
        }
        System.out.println("----- Encrypting Password -----");
        for(int i=0; i< password.length(); i++){
            String lookupChar = String.valueOf(password.charAt(i)).toLowerCase();
            String val = (String) lookup.get(lookupChar);
            newPassword = newPassword + val;
        }

        return newPassword;
    }

    public String getDecryptedPassword(String password){
        if(lookup == null){
            lookup = taskBObject.getFileFromBucket(s3Object, "task-c-bucket", "Lookup.txt");
        }

        String decryptedPassword = "";
        System.out.println("----- Decrypting Password -----");
        for (int i = 0; i< password.length(); i+=2){
            String lookupValue = String.valueOf(password.charAt(i)) + String.valueOf( password.charAt(i+1));
            boolean flag = true;
            Enumeration<String> keys = lookup.keys();
            while(flag){
                String key = keys.nextElement();
                if(String.valueOf(lookup.get(key)).equals(lookupValue)){
                    decryptedPassword += key;
                    flag = false;
                }
            }
        }
        return decryptedPassword;
    }
}
