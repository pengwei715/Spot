package com.spot.parking.tracking

import org.apache.spark.sql.SparkSession

trait SparkSessionWrapper extends Serializable {

  lazy val spark: SparkSession = {
    SparkSession.builder().master("local[1]").appName("spark session").getOrCreate()
  }

}