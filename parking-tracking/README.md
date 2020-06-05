# parking-tracking

Aggregate the historical data of parking ticket based on time and location

## Compile and run

complie
```
sbt clean
sbt compile
```
package fat jar
```
sbt assembly
```
Start the postgresql first, then run job on Spark, 
```
bin/spark-submit --class com.spot.parking.tracking.Aggregateor --master yarn --deploy-mode client ~/Spot/parking-tracking/target/scala-2.11/parking-tracking-assembly-0.0.1.jar
```

## Documentation



## How to contribute

*How others can contribute to the project*