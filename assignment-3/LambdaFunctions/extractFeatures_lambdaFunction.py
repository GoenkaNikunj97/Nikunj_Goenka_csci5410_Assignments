import json
import urllib.parse
import boto3

BUCKET_2 = "tags-b00868621"
def lambda_handler(event, context):
    # TODO implement
    s3 = boto3.client('s3')
    
    file_ob = event['Records'][0]
    
    fileName = str(file_ob['s3']['object']['key'])
    bucketName = str(file_ob['s3']['bucket']['name'])
    
    file = s3.get_object(Bucket = bucketName, Key = fileName)
    fileContent = file["Body"].read().decode()
    
    splitData = fileContent.split()
    nameEntities = dict()
    for word in splitData:
        if(word[0].isupper()):
            word.replace(".", "")
            word.replace(",", "")
            word.replace("?", "")
            
            if(word in nameEntities.keys()):
                nameEntities[word] +=1
            else:
                nameEntities[word] = 1

    print(nameEntities)