#!/bin/bash

FOLDER=$AIRFLOW_HOME/dags
FILE=scheduler.py

if [ ! -d $FOLDER ] ; then

    sudo mkdir $FOLDER

fi

sudo cp $FILE $FOLDER/
python $FOLDER/$FILE

airflow initdb
