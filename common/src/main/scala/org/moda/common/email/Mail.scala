package org.moda.common.email

//import scala.util._
import com.typesafe.config._
//import com.typesafe.scalalogging.Logger

import scala.concurrent.Future


/**
 * @author huanggh
 * 2020/9/7 上午11:26
 */
object Mail {

//  private [this] val logger = Logger(getClass)

  private [this] val config  = ConfigFactory.load()
  private [this] val server  = config.getString("mailer.server")
  private [this] val port    = config.getInt("mailer.port")
  private [this] val sender: String = config.getString("mailer.sender")
  private [this] val password = config.getString("mailer.password")


  import courier._, Defaults._

  val mailer: Mailer = Mailer(server, port)
    .auth(true)
    .as(sender, password)
    .ssl(true)() // 采用ssl


  def envelopeOf(recipients: Seq[String]): Envelope = Envelope(
    from = sender.addr
    , _to = recipients.map(_.addr)
  )

  def send(subject: String, content: String, recipients: Seq[String]): Future[Unit] =
    mailer(
      envelopeOf(recipients)
        .subject(subject)
        .content(Text(content))
    )
//      .onComplete {
//        case Success(_) => logger.debug("{} email send success", subject)
//        case Failure(_) => logger.debug("{} email send failure", subject)
//    }

}
