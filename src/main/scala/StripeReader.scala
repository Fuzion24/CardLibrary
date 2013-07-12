import android.media.{AudioFormat, MediaRecorder, AudioRecord}
import scala.annotation.tailrec
import scala.collection.BitSet
import scala.concurrent.{ExecutionContext, Future}

object StripeReader {
  val BUFFER_SIZE = 44100
  val CUTOFF = 700
}

trait Decoder {
     def decodeBits(bits:BitSet):String
}

sealed trait ReaderState
case object READY         extends ReaderState
case object READING_SWIPE extends ReaderState
case object DECODING      extends ReaderState


class StripeReader(implicit ec:ExecutionContext) { self:Decoder =>
  import StripeReader._
  val minBufferSize = AudioRecord.getMinBufferSize(44100, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
  val audioSRC = 1//android.media.MediaRecorder.AudioSource.MIC
  val recorder = new AudioRecord(audioSRC, 44100, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, BUFFER_SIZE)
  private val buffer = new Array[Short](minBufferSize)

  def getSwipeData:Future[String] = Future {
    waitForSwipe
    decodeBits(decodeSwipe(readSwipe))
  }

  //@tailrec
  def waitForSwipe:Unit = {
    val readCnt = recorder.read(buffer,0, minBufferSize)
    val startReading = buffer.slice(0,readCnt).exists(isValidSample)
    if(!startReading) waitForSwipe
  }

  def readSwipe:Array[Short] =
  {
    @tailrec
    def readSwipeHelp(acc:Array[Short]):Array[Short] =
      if(signalIsQuiet(acc)) acc else {
        val readCnt = recorder.read(buffer,0, minBufferSize)
        readSwipeHelp(acc ++ buffer.slice(0,readCnt))
      }
    readSwipeHelp(Array())
  }

  def signalIsQuiet(samples:Array[Short]):Boolean = {
    val silenceQuorum = 5
    val silenceLevel = 500
    samples.takeRight(silenceQuorum).forall(Math.abs(_) < silenceLevel)
  }

  def decodeSwipe(samples:Array[Short]):BitSet = {
    def decodeSwipeHelp(acc:BitSet, samplesBuffer:Array[Short], above:Boolean, gap:Int, lastGap:Int, gotOne:Boolean):BitSet =
      if(samplesBuffer.isEmpty) acc else {
        if(above && samplesBuffer.head < -CUTOFF || !above && samplesBuffer.head > CUTOFF)

          if(lastGap == 0)
            decodeSwipeHelp(acc + 0, samplesBuffer.tail,!above, 0, gap, gotOne)
          else if( Math.abs(gap - lastGap) < Math.abs(gap - (lastGap/2)) )
            decodeSwipeHelp(acc + 0, samplesBuffer.tail,!above, 0, gap, gotOne)
          else if(!gotOne)
            decodeSwipeHelp(acc + 1, samplesBuffer.tail, !above, 0, 2 * gap, gotOne = true)
          else
            decodeSwipeHelp(acc, samplesBuffer.tail,!above, 0, lastGap, gotOne = false)

        else decodeSwipeHelp(acc,samplesBuffer.tail, above, gap + 1, lastGap, gotOne)
      }

    decodeSwipeHelp(BitSet(),samples, samples.head > 0, 0, 0, gotOne = false)
  }

  def isValidSample(sample:Short):Boolean =
    if (sample > CUTOFF || sample < -CUTOFF) true
    else false
}