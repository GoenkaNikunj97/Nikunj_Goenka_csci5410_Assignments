import java.util.*;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;

public class App {

    public static void main( String[] args ) {
        String queueName = "HfxFlowers";
        String url = "";
        int totalMsgToSend = 15;

        List<String> flowerSizeList = new ArrayList<String>();
        flowerSizeList.add("SMALL");
        flowerSizeList.add("MEDIUM");
        flowerSizeList.add("LARGE");
        flowerSizeList.add("EXTRA-LARGE");

        List<String> flowerTypeList = new ArrayList<String>();
        flowerTypeList.add("Rose");
        flowerTypeList.add("Lily");
        flowerTypeList.add("White Rose");
        flowerTypeList.add("TULIP");

        AmazonSQS amazonSQS = AmazonSQSClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
        url = amazonSQS.getQueueUrl(queueName).getQueueUrl();
        for(int i = 0; i < totalMsgToSend; i++)
        {
            Random random = new Random();
            int flowerSize = random.nextInt(flowerSizeList.size());
            String size = flowerSizeList.get(flowerSize);

            int flowerType = random.nextInt(flowerTypeList.size());
            String type = flowerTypeList.get(flowerType);

            String message = "Order: "+String.valueOf(i)+": Size: "+size +" and Type: "+type;

            System.out.println(message);
            SendMessageRequest send_msg_request = new
                    SendMessageRequest().withQueueUrl(url).withMessageBody(message).withDelaySeconds(15);
            amazonSQS.sendMessage(send_msg_request);
        }
    }
}
