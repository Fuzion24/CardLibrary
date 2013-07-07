import android.media.{AudioFormat, MediaRecorder, AudioRecord}

object StripeReader {
  val BUFFER_SIZE = 44100
  val CUTOFF = 700
}

trait Decoder {

}

sealed trait ReaderState
case object READY         extends ReaderState
case object READING_SWIPE extends ReaderState
case object DECODING      extends ReaderState


class StripeReader { self:Decoder =>
 import StripeReader._

  val recorder = new AudioRecord(MediaRecorder.AudioSource.MIC, 44100, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, BUFFER_SIZE)

  def startReading{
    recorder
  }
  def readSwipe:Option[String] = {
          None
  }

  def isValidSample(sample:Byte):Boolean =
      if (sample > CUTOFF || sample < -CUTOFF) true
      else false
}