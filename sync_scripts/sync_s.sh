rsync -av -e "ssh -i ~/.ssh/pgw-IAM-keypair.pem" /usr/local/spark/conf/ ubuntu@10.0.0.53:/usr/local/spark/conf/
rsync -av -e "ssh -i ~/.ssh/pgw-IAM-keypair.pem" /usr/local/spark/conf/ ubuntu@10.0.0.54:/usr/local/spark/conf/
rsync -av -e "ssh -i ~/.ssh/pgw-IAM-keypair.pem" /usr/local/spark/conf/ ubuntu@10.0.0.125:/usr/local/spark/conf/
