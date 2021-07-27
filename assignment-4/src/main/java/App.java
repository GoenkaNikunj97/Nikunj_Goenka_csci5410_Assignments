import java.util.Timer;
import java.util.TimerTask;

public class App {

    public static void main( String[] args ) {
        String BUCKET_1 = "twitter-data-b00868621";

        String filePath = "N:/STUDIES/MACS/SEM-3/5410 - Serverless Data Processing/Assignements/AssignmentCode/assignment-4/file_mongo_tweets.txt";
        TaskB taskBObject = new TaskB();
        taskBObject.startPushingAtSetIntervals(filePath, BUCKET_1);
    }
}
