package batch

import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}
import org.apache.spark.sql.functions._
import org.apache.spark.storage.StorageLevel
import java.util.{Properties,Map}
import org.yaml.snakeyaml.Yaml
import java.io.{File, FileInputStream}

//used to preprocess parking-ticket historical data
object Aggregator{
    def parseConfig(filename : String): java.util.Map[String, String] = {
    	val ios = new FileInputStream(new File(filename))
	val yaml = new Yaml()
	val config = yaml.load(ios).asInstanceOf[java.util.Map[String, String]]
	return config
    }

    def processOneCol(cached_df: DataFrame, whole: DataFrame, column: String): DataFrame = {
	var df = whole.groupBy("lng_id", "lat_id", column)
	       	      .count()
		      .withColumnRenamed("count", column + "_count")
	cached_df.join(df, Seq("lng_id", "lat_id"), "inner")
    }
    
    def main(args: Array[String]): Unit = {
    	val filename = args(0)
	val config = this.parseConfig(filename)
        val spark = SparkSession.builder
            .appName(config.get("appname"))
	    .getOrCreate

	val dbConnectionUrl = config.get("connectionUrl")
	val prop = new Properties();
    	prop.setProperty("driver", config.get("driver"));
    	prop.setProperty("user", config.get("user"));
    	prop.setProperty("password", config.get("password"));

	var df = spark.read.format("csv")
	    .option("header","true")
	    .load(config.get("input_path"))
	import spark.implicits._	
	var clean_df = df.select(col(config.get("ids")),
				 col(config.get("date")),
				 col(config.get("year")),
				 col(config.get("month")),
				 col(config.get("hour")),
				 col(config.get("lng")),
				 col(config.get("lat"))).na.drop()
				  
	var whole = clean_df.select(col(config.get("ids")),
				    col(config.get("date")),
				    col(config.get("year")),
				    col(config.get("month")),
				    col(config.get("hour")),
				    col(config.get("lng")),
				    col(config.get("lat")),
				    floor(($"geocoded_lng" + 87.79)/0.0025).as("lng_id"),
				    floor(($"geocoded_lat"-41.63)/0.0025).as("lat_id"),
				    date_format($"issue_date", "u").as("week_day"),
				    date_format($"issue_date", "W").as("week_of_month"),
				    date_format($"issue_date", "d").as("day_month"))
				    
        whole.repartition(col("lng_id"), col("lat_id"))
	var cached_df = whole.groupBy("lng_id","lat_id").count().cache()
	
	var temp_df1 = cached_df.select($"lng_id", $"lat_id", ($"count"/7).as("week_day_avg"))
        var week_day_df = processOneCol(temp_df1, whole, config.get("week_day")).cache()

	var temp_df2 = cached_df.select($"lng_id", $"lat_id", ($"count"/12).as("month_avg"))
	var month_df = processOneCol(temp_df2, whole, config.get("month")).cache()

	var temp_df3 = cached_df.select($"lng_id", $"lat_id", ($"count"/24).as("hour_avg"))
	var hour_df = processOneCol(temp_df3, whole, config.get("hour")).cache()
	
	var temp_df4 = cached_df.select($"lng_id", $"lat_id", ($"count"/31).as("day_month_avg"))
	var day_month_df = processOneCol(temp_df4, whole, "day_month").cache()

	
	var temp1 = week_day_df.join(month_df, Seq("lng_id", "lat_id"), "inner").cache()
        var temp2 = temp1.join(hour_df, Seq("lng_id", "lat_id"), "inner").cache()
	var res_df = temp2.join(day_month_df, Seq("lng_id", "lat_id"), "inner")
	res_df.write.mode(SaveMode.Overwrite).jdbc(dbConnectionUrl, "res_df", prop)
	spark.stop()
    }
}
