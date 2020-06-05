package com.spot.parking.tracking
import org.apache.spark.sql.{DataFrame,SaveMode, SparkSession}
import org.apache.spark.sql.functions._
import java.util.Properties

object Aggregator{
    def main(args: Array[String]): Unit = {

        //start a new spark session
	//master("spark://10.0.0.223:7077")
        val spark = SparkSession.builder
            .appName("aggregate parking ticket")
            .master("spark://10.0.0.223:7077")
            .getOrCreate

        //useful for rdd and sql
	import spark.implicits._
	var df = spark.read.format("csv")
	    .option("header","true")
	    .load("parking_aa")
	    //.load("chicago_parking_tickets.csv")

	//checke the sample data
	val dbConnectionUrl = "jdbc:postgresql://10.0.0.223/parking"
	val prop = new Properties();
    	prop.setProperty("driver", "org.postgresql.Driver");
    	prop.setProperty("user", "postgres");
    	prop.setProperty("password", "zhimakaimen");

	//show the result
	df.show()
	//check the schema
	df.printSchema()

	//store into the database
	df.write.mode(SaveMode.Overwrite).jdbc(dbConnectionUrl, "original", prop)


	//pure sql systax
	var useful_df = df.select($"ticket_number", $"issue_date", $"year", $"month", $"hour", $"geocoded_lng", $"geocoded_lat")
	useful_df.show()
	var clean_df = useful_df.na.drop()
	clean_df.show()
	
	//store into the database
	clean_df.write.mode(SaveMode.Overwrite).jdbc(dbConnectionUrl, "original_clean", prop)

        //required implicits
	var whole = clean_df.select($"ticket_number", $"issue_date", $"year", $"month", $"hour", $"geocoded_lng", $"geocoded_lat", floor(($"geocoded_lng" + 87.79)/0.0025).as("lng_id"), floor(($"geocoded_lat"-41.63)/0.0025).as("lat_id"), date_format($"issue_date", "u").as("week_day"), date_format($"issue_date", "W").as("week_of_month"), date_format($"issue_date", "d").as("day_month") )
	whole.show()


	//store into the database
	whole.write.mode(SaveMode.Overwrite).jdbc(dbConnectionUrl, "whole", prop)

        //build week_day_table
        var week_day_count1 = whole.groupBy("lng_id","lat_id","week_day").count()
	var week_day_count = week_day_count1.withColumnRenamed("count", "week_day_count")
	var week_day_avg = week_day_count.groupBy("week_day").avg("week_day_count")
	var week_day_df = week_day_avg.withColumnRenamed("avg(week_day_count)", "week_day_avg")
	var res_week_day = week_day_count.join(week_day_df, "week_day")
	res_week_day.show()

	
	//store into the database
	res_week_day.write.mode(SaveMode.Overwrite).jdbc(dbConnectionUrl, "week_day", prop)


        //build month__table
        var month_count_1 = whole.groupBy("lng_id","lat_id","month").count()
	var month_count = month_count_1.withColumnRenamed("count", "month_count")
	var month_avg = month_count.groupBy("month").avg("month_count")
	var month_df = month_avg.withColumnRenamed("avg(month_count)", "month_avg")
	var res_month_df = month_count.join(month_df, "month")
	res_month_df.show()
	
	//store into the database
	res_month_df.write.mode(SaveMode.Overwrite).jdbc(dbConnectionUrl, "month", prop)


        //build hour_table
	var hour_count_1 = whole.groupBy("lng_id","lat_id","hour").count()
	var hour_count = hour_count_1.withColumnRenamed("count", "hour_count")
	var hour_avg = hour_count.groupBy("hour").avg("hour_count")
	var hour_df = hour_avg.withColumnRenamed("avg(hour_count)", "hour_avg")
	var res_hour_df = hour_count.join(hour_df, "hour")
	res_hour_df.show()
	
	//store into the database
	res_hour_df.write.mode(SaveMode.Overwrite).jdbc(dbConnectionUrl, "hour", prop)

	//build week_of_month table
	var week_of_month_1 = whole.groupBy("lng_id","lat_id","week_of_month").count()
	var week_of_month_count = week_of_month_1.withColumnRenamed("count", "week_of_month_count")
	var week_of_month_avg = week_of_month_count.groupBy("week_of_month").avg("week_of_month_count")
	var week_of_month_df = week_of_month_avg.withColumnRenamed("avg(week_of_month_count)", "week_of_month_avg")
	var res_week_of_month_df = week_of_month_count.join(week_of_month_df, "week_of_month")
	res_week_of_month_df.show()
	
	//store into the database
	res_week_of_month_df.write.mode(SaveMode.Overwrite).jdbc(dbConnectionUrl, "week_of_month", prop)
	    
	spark.stop()
        
    }
}
