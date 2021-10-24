package linReg
import java.util.logging.Logger
import breeze.linalg.{DenseMatrix, DenseVector, pinv}
import linReg.entitis.{mseScore, maeScore}


class linearRegression(lrLogger: Logger) {

  var weight: DenseVector[Double] = DenseVector.ones[Double](0)
  var logger: Logger = lrLogger

  def fit(X: DenseMatrix[Double], y: DenseVector[Double], iteration: Int = 1500, learningRate: Double = 0.005): Unit = {

    var lossBest = Double.MaxValue
    var loss = Double.MaxValue
    var yPred = DenseVector.zeros[Double](y.length)
    var gradients = DenseVector.zeros[Double](X.cols)
    weight = DenseVector.ones[Double](X.cols)


    for (i <- 0 to iteration; if lossBest >= loss) {
      lossBest = loss
      yPred = predict(X)
      gradients = pinv(X) * (yPred - y)

      weight -= learningRate * gradients
      yPred = predict(X)
      loss = mseScore(y, yPred)

      if (i % 250 == 0) {
        val loss_mae = maeScore(y, yPred)
        val lossMSE = mseScore(y, yPred)
        logger.info(f"Iteration: $i, MAE=$loss_mae%.2f, MSE=$lossMSE%.2f")
        logger.info("-"*50)
      }
    }
  }

    def predict(X: DenseMatrix[Double]): DenseVector[Double] = {
      X * weight
}
}
