package linReg

import linReg.entitis.{getLogger, getDataFrame, maeScore, mseScore, r2Score, writeInFile}
object Main {

  def main(args: Array[String]): Unit = {

    val startTime = System.currentTimeMillis()
    var endTime = 0: Long
    val logger = getLogger(name = "linear_regression", outputPath = "linear_regression.log")

    var dataTrain = ""
    var dataTest = ""
    var targetCol = 0: Int
    var outTest = ""

    args.sliding(2, 2).toList.collect {
      case Array("--train", argDataTrain: String) => dataTrain = argDataTrain
      case Array("--test", argDataTest: String) => dataTest = argDataTest
      case Array("--target", argTargetCol: String) => targetCol = argTargetCol.toInt
      case Array("--output", argOutTest: String) => outTest = argOutTest
    }


    logger.info(f"Loading train data: $dataTrain")
    logger.info("="*50)
    val dataFrame = getDataFrame(path = dataTrain, targetCol = targetCol)
    val xTrain = dataFrame._1
    val yTrain = dataFrame._2

    logger.info(f"Start training....")
    val lr = new linearRegression(logger)
    lr.fit(xTrain, yTrain)
    val yPredictTrain = lr.predict(xTrain)
    val maeTrain = maeScore(yTrain, yPredictTrain)
    val mseTrain = mseScore(yTrain, yPredictTrain)
    logger.info(f"Train MAE: $maeTrain%.3f, MSE: $mseTrain%.3f")

    logger.info(f"Loading validation data...")
    val dataFrameTest = getDataFrame(dataTest, targetCol)
    val xTest = dataFrameTest._1
    val yTest = dataFrameTest._2
    val yPredictTest = lr.predict(xTest)
    val maeTest = maeScore(yTest, yPredictTest)
    val mseTest = mseScore(yTest, yPredictTest)
    val r2 = r2Score(yTest, yPredictTest)
    endTime = System.currentTimeMillis()
    logger.info(f"MAE test: $maeTest%.3f, MSE: $mseTest%.3f, R2: $r2%.3f")

    val finalTime = (endTime - startTime) / 1000
    logger.info(f"Время расчета в секундах: $finalTime")
    writeInFile(outTest, yPredictTest)


  }
}
