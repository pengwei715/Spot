import sys
import os
import time, json
from app import app
from datetime import datetime
from flask import jsonify, render_template, request
from math import floor
import psycopg2
import random
import yaml
from collections import OrderedDict
import pdb


# configure connection string for PostgreSQL database
def loadconfig(file):
    with open(file) as config_file:
        configs = yaml.safe_load(config_file)
    return configs


app.dbconfig = loadconfig('/home/ubuntu/Spot/flask/app/config/postgres.yml')
app.conn_str = "host='%s' dbname='%s' user='%s' password='%s'" % (app.dbconfig["host"],
                                                                  app.dbconfig["dbname"],
                                                                  app.dbconfig["user"],
                                                                  app.dbconfig["password"])

def fetch_from_postgres(query):
    """
    gets the data from PostgreSQL database using the specified query
    :type query: str
    :rtype     : list of records from database
    """
    conn = psycopg2.connect(app.conn_str)
    cursor = conn.cursor()
    cursor.execute(query)
    data = cursor.fetchall()
    cursor.close()
    conn.close()
    return data


# define the behavior when accessing routes '/', '/index', '/demo', '/track' and '/query'

@app.route('/')
@app.route('/index')
def index():
    return render_template('index.html')


@app.route('/getTime', methods=['POST'])
def getTime():
    pass


@app.route('/getUnit', methods=['POST'])
def getUnit():
    pass


@app.route('/getLoc', methods=['POST'])
def getLoc():
    pass



