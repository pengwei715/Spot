import sys
import os
import time, json
from app import app
from datetime import datetime
from flask import jsonify, render_template, request
from math import floor
import psycopg2
import yaml
from collections import OrderedDict
import googlemaps

# configure connection string for PostgreSQL database
def loadconfig(file):
    with open(file) as config_file:
        configs = yaml.safe_load(config_file)
    return configs


app.dbconfig = loadconfig('/home/ubuntu/Spot/flask/app/config/postgres.yml')
app.conn_str = "host='%s' port=5432 dbname='%s' user='%s' password='%s'" % (app.dbconfig["host"],
                                                                  app.dbconfig["dbname"],
                                                                  app.dbconfig["user"],
                                                                  app.dbconfig["password"])
app.update = False

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


@app.route('/track')
def track():
    default_timestr = datetime.now().strftime('%Y-%m-%d %H:%M:%S')
    timestr = request.args.get('timestr', default=default_timestr, type=str)
    unit = request.args.get("time_unit", default = 'hour', type=str)
    address = request.args.get("typed_address", default = 'the university of chicago', type=str)
    
    time_obj = datetime.strptime(timestr, '%Y-%m-%d %H:%M:%S')
    if unit == 'hour': unit_num = time_obj.hour
    elif unit == 'month': unit_num = time_obj.month
    elif unit == 'day': unit_num = time_obj.day
    elif unit == 'week_day': unit_num = time_obj.weekday()
    
    gmaps = googlemaps.Client(key = "*******")
    res = gmaps.geocode(address)[0]['geometry']['location']
    lng_id = floor((res['lng']+87.79)/0.0025)
    lat_id = floor((res['lat']-41.63)/0.0025)
    sql = "select {}_avg, {}_count from {}_table where {} = '{}' and lng_id = {} and lat_id = {}".format(unit, unit, unit, unit, unit_num, lng_id, lat_id)
    data = fetch_from_postgres(sql)[0]
    rate = data[1]/data[0]
    color = ""
    if rate > 1.5:
        color = "#FF0000"
    elif 0.8 < rate < 1.5:
        color = "#FFF000"
    elif rate < 0.8:
        color = "#008000"
    app.color = color
    app.carloc = [res['lng'], res['lat']]
    return render_template('track.html', color = app.color, lat = res['lat'], lng = res['lng'], avg=data[0], count = data[1])
