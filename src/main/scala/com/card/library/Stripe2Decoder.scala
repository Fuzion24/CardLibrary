package com.card.library

import scala.collection.BitSet

trait Stripe2Decoder extends StripeDecoder {
  def decodeBits(bitSet:BitSet):String = {
    android.util.Log.d("Decoder" , bitSet.toString())
    "nooo"
  }
}
