import java.util.Timer;
import java.util.TimerTask;

public class App {

    public static void main( String[] args ) {

        Timer t = new Timer();

        t.scheduleAtFixedRate( new TimerTask() {
            final String BUCKET_1 = "source-data-b00868621";
            String filePath = "N:/STUDIES/MACS/SEM-3/5410 - Serverless Data Processing/Assignements/AssignmentCode/assignment-3/Dataset/";
            

            TaskA taskAObject = new TaskA();
            int i = 0;
            public void run() {
                i+=1;
                String fileToPush = filePath;
                if(i<10){
                    fileToPush += "/00"+String.valueOf(i)+".txt";
                }else if(i <100){
                    fileToPush += "/0"+String.valueOf(i)+".txt";
                }else{
                    fileToPush += "/" + String.valueOf(i)+".txt";
                }
                taskAObject.startPushingAtSetIntervals(fileToPush, BUCKET_1);
            }
        }, 0,100);
    }
}
