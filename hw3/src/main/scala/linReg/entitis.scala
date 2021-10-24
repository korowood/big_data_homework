package linReg

import java.io.{File, PrintWriter}
import java.util.logging.{FileHandler, Logger, SimpleFormatter}

import breeze.stats.mean
import breeze.linalg.sum
import breeze.numerics.{abs, pow}
import breeze.linalg.DenseVector
import breeze.linalg.{DenseMatrix, csvread}

object entitis {

  def maeScore(y: DenseVector[Double], yPredict: DenseVector[Double]): Double = {
    val diff = y.toArray zip yPredict.toArray map (z => abs(z._1 - z._2))
    mean(diff)
  }

  def mseScore(y: DenseVector[Double], yPredict: DenseVector[Double]): Double = {
    val diff = y.toArray zip yPredict.toArray map (z => pow(z._1 - z._2, 2))
    mean(diff)
  }

  def r2Score(yTrue: DenseVector[Double], yPredict: DenseVector[Double]): Double = {
    1 - (sum(pow(yTrue - yPredict, 2)) / sum(pow(yTrue - mean(yTrue), 2)))
  }

  def getDataFrame(path: String, targetCol: Int): (DenseMatrix[Double], DenseVector[Double]) = {
    val csvFile: File = new File(path)
    val data: DenseMatrix[Double] = csvread(csvFile, skipLines = 1)
    val maxColNum = data.cols - 1
    val cols = for (i <- 0 to maxColNum if i != targetCol) yield i
    val X = data(::, cols).toDenseMatrix
    val y: DenseVector[Double] = data(::, targetCol)
    (X, y)
  }

  def writeInFile(path: String, iterator: DenseVector[Double]): Unit = {
    val writer = new PrintWriter(new File(path))

    for (el <- iterator) {
      writer.write(f"$el\n")
    }
    writer.close()
  }

  def getLogger(name: String, outputPath: String): Logger = {
    System.setProperty(
      "java.util.logging.SimpleFormatter.format",
      "%1$tF %1$tT %4$s %5$s%6$s%n"
    )

    val logger = Logger.getLogger(name)
    val handler = new FileHandler(outputPath)
    val formatter = new SimpleFormatter()
    handler.setFormatter(formatter)
    logger.addHandler(handler)
    logger
  }
}
