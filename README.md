# Spot!

> ***Spatial parking optimized tracking system to avoid parking ticket***


This is a project I completed during the Insight Data Engineering program (Boston, Summer 2020).
Visit [datapipeline.online](http://datapipeline.online) to see it in action (or watch it [here](https://www.youtube.com/watch?v=SEieUv3eGAM)).

## Table of Contents

1. [Usage](README.md#Usage)
1. [System](README.md#System)
1. [Data Source](README.md#Data-Sources)
1. [Setup](README.md#setup)
1. [Starting the pipeline](README.md#starting-the-pipeline)
1. [Contact Information](README.md#contact-information)

***

## Usage

This project aim to provide the drivers if the location has highter than average rate of parking citations or not. 

Red means that the number of parking citation is more than 1.5 x of the average in the 250 * 250 m^2 spatial buffer given some time unit buffer. Yellow means the number of parking citation is between 0.8 x and 1.5 x.Green means that the number of parking citation is less than 0.8 x.

The system requires three inputs. 
- Timestamp's format is "yyyy-mm-dd hh:mm:ss". 
- Time units (hour, week day, week of month, day of month). 
- Address

![Demo_gif](./doc/Spot_demo.gif)

For example, the first query above means that 1 pm parking near the University of Chicago is more likely to get a parking ticket compared to the other hours.

---
## System

The parking ticket data is stored in S3 bucket. Spark fetch the data, add the spatial index and abstract the useful time information, then aggregate the data based on spatial and temporal buffers. Store the result into postgres. 

![system_png](./doc/system.png)

---

## Data Sources

  [Chicago parking tickets](https://www.propublica.org/datastore/dataset/chicago-parking-ticket-data)

---
## Setup

Install and configure [AWS CLI](https://aws.amazon.com/cli/) and [Pegasus](https://github.com/InsightDataScience/pegasus) on your local machine, and clone this repository using


### Cluster Structure:

- (4 nodes) Spark Cluster - Batch & Airflow
- (1 node) PostgreSQL
- (1 node) Flask

```
peg up ./cluster_configs/spark/master.yml
peg up ./cluster_configs/spark/worker.yml
peg up ./cluster_configs/post_node.yml
peg up ./cluster_configs/flask_node.yml
```

For each cluster, install the services.

#### spark cluster
```
peg service install spark_cluster aws
peg service install spark_cluster environment
peg service install spark_cluster hadoop
peg service install spark_cluster spark
```
Config spark cluster and sync the hadoop and spark configs among nodes.
```
bash ./cluster_configs/sync_scripts/sync_h.sh
bash ./cluster_configs/sync_scripts/sync_s.sh
```

#### postgres node & flask node
```
peg service install post_node aws
peg service install post_node environment
peg service install flask_node aws
peg service install flask_node environment
```
On the postgres node install postgres
```
sudo apt-get update && sudo apt-get -y upgrade
sudo apt-get install postgresql postgresql-contrib​
```
On the flask node install flask
```
sudo apt-get install python3-pip
pip install Flask
```






