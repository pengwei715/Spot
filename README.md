# Spot!

> ***Spatial parking optimized tracking system to avoid parking ticket***


This is a project I completed during the Insight Data Engineering program (Boston, Summer 2020).
Visit [datapipeline.online](http://datapipeline.online) to see it in action (or watch it [here](https://www.youtube.com/watch?v=SEieUv3eGAM)).

***

This project aim to provide the drivers if the 



![Demo_gif](./doc/Spot_demo.gif)



System
---
100 GB parking ticket data is stored in S3 bucket. Spark fetch the data, add the spatial index and abstract the useful time information, then aggregate the data based on spatial and temporal buffers and store the result into postgres. Users provide the timestamp and the address to query against the database, the web app will show if the location is higher than average rate of parking ticket or not by different colors. 


![system_png](./doc/system.png)