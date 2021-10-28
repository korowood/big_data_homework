package tf_idf

import org.apache.spark.sql._
import org.apache.spark.sql.functions._
import tf_idf.entitis.{calcIdf, LIMIT}


object SparkWordCount {


  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .master("local[*]")
      .appName("tf_idf")
      .getOrCreate()

    import spark.implicits._

    val df = spark.read
      .option("header", "true")
      .option("interShema", "true")
      .csv("tripadvisor_hotel_reviews.csv")

    //      df.show()

    val columnName = "Review"
    val lower_df = df.withColumn(columnName, lower(col(columnName)))

    val reg_df = lower_df
      .withColumn(columnName, regexp_replace(col(columnName),
        "[\\?,\\,,\\$,\\-,\\!,\\',\\*]", " "))
      .withColumn(columnName, trim(col(columnName)))
      .withColumn(columnName, regexp_replace(col(columnName),
        "[^a-zA-Z0-9]+", " "))

    reg_df.show()
    val columnNameId = "Review_id"
    val docsCount = reg_df.count()
    val word = reg_df.withColumn(columnNameId, monotonically_increasing_id())
      .withColumn(columnName, split(col(columnName), " "))
      .drop("Rating")
      .select($"Review_id", explode($"Review") as "token")
      .where(length($"token") > 1 or $"token".cast("int").isNotNull)

    word.show()

    val tf = word.groupBy(columnNameId, "token")
      .agg(count("token") as "tf")

    tf.show()

    val docFreq = word.groupBy("token")
      .agg(countDistinct(columnNameId) as "df")
      .orderBy($"df".desc).limit(LIMIT)
    val calcIdfUdf = udf { df: Long => calcIdf(docsCount, df) }
    val idf = docFreq.withColumn("idf", calcIdfUdf(col("df")))

    idf.show()

    val tokensWithTf = tf.join(idf, Seq("token"), "inner")
      .withColumn("tf_idf", col("tf") * col("idf"))

    tokensWithTf.show()

    val result = tokensWithTf.groupBy(columnNameId)
      .pivot("token")
      .agg(round(first(col("tf_idf")), 2))

    result.show()

  }
}
