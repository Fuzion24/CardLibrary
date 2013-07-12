package com.card.library

import org.scaloid.common._
import android.os.Bundle
import android.view.Gravity
import android.graphics.Color
import java.util.concurrent.atomic.AtomicInteger

class MainActivity extends SActivity {
  val viewSeq = new AtomicInteger(0)

  override def onCreate(savedState: Bundle) {
    super.onCreate(savedState)
    val prefs = Preferences()

    viewSeq.set(0)
    setContentView(
      new SVerticalLayout {
        style {
          case v => v.id = viewSeq.incrementAndGet
        }
        STextView("Say Hello").gravity(Gravity.CENTER).textSize(16 sp).textColor(Color.BLUE).<<.marginBottom(16 dip).>>
        val name = SEditText(prefs.String.name).hint("enter your first name").selectAllOnFocus(true)
        SButton("Greet!", {
          val reader = new StripeReader with Stripe2Decoder
           reader.getSwipeData
        })
      }
    )

  }
}