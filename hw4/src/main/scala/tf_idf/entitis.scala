package tf_idf


object entitis {

  val LIMIT = 100
  def calcIdf(docsCount: Long, docFreq: Long): Double =
    math.log((docsCount.toDouble + 1) / (docFreq.toDouble + 1))

}
