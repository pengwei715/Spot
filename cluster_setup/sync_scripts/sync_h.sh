rsync -av -e "ssh -i ~/.ssh/pgw-IAM-keypair.pem" /usr/local/hadoop/etc/hadoop/ ubuntu@10.0.0.53:/usr/local/hadoop/etc/hadoop/
rsync -av -e "ssh -i ~/.ssh/pgw-IAM-keypair.pem" /usr/local/hadoop/etc/hadoop/ ubuntu@10.0.0.54:/usr/local/hadoop/etc/hadoop
rsync -av -e "ssh -i ~/.ssh/pgw-IAM-keypair.pem" /usr/local/hadoop/etc/hadoop ubuntu@10.0.0.125:/usr/local/hadoop/etc/hadoop
