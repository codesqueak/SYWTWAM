# AWS Deployment Notes

http://docs.aws.amazon.com/cli/latest/userguide/awscli-install-linux.html

install python
 
sudo yum install python

install pip package manager

curl https://bootstrap.pypa.io/get-pip.py -o get-pip.py
python get-pip.py
sudo python get-pip.py

Install aws cli tools

pip install awscli --upgrade --user

This command will also upgrade an installed instance

Check it is installed

aws --version

Add key info etc

aws configure

# how-to

https://coreos.com/kubernetes/docs/latest/kubernetes-on-aws.html


# aws kube

download https://github.com/kubernetes-incubator/kube-aws/releases/download/v0.9.6/kube-aws-linux-amd64.tar.gz
tar zxvf kube-aws-linux-amd64.tar.gz
mv linux-amd64/kube-aws /usr/local/bin

aws ec2 describe-instances

Note: Set up a user with EC2, key power user API access

AWSKeyManagementServicePowerUser
AmazonEC2FullAccess
AmazonAPIGatewayInvokeFullAccess
AmazonS3FullAccess


# EC2 key pair

Generate key pair on console: https://console.aws.amazon.com/ec2/v2/home?region=us-east-1


#  KMS key

aws kms --region=us-east-1 create-key --description="kube-aws assets"

# External DNS name

Pick a domain name ...


# S3 bucket

aws s3api --region=us-east-1 create-bucket --bucket <mybucket>

or, if not us-east-1

aws s3api create-bucket --bucket <my-bucket> --region eu-west-1 --create-bucket-configuration LocationConstraint=eu-west-1

bucket names need to be unqiue on system !

# Initialize an asset directory

mkdir my-cluster
cd my-cluster

kube-aws init \
--cluster-name=my-cluster-name \
--external-dns-name=my-cluster-endpoint \
--region=us-west-1 \
--availability-zone=us-west-1c \
--key-name=key-pair-name \
--kms-key-arn="arn:aws:kms:us-west-1:xxxxxxxxxx:key/xxxxxxxxxxxxxxxxxxx"


# Render contents of the asset directory

kube-aws render credentials --generate-ca

kube-aws render stack

kube-aws validate



# Launch your Kubernetes cluster on AWS

kube-aws up --s3-uri s3://<mybucket>/xyzzy



