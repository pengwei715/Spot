package com.spot.parking.tracking

import org.apache.spark.sql.SparkSession

trait SparkSessionWrapper extends Serializable {

  lazy val spark: SparkSession = {
    SparkSession.builder().master("spark://10.0.0.223:7077").appName("spark session").getOrCreate()
  }

}