package delays

import java.time.Duration
import java.util.Properties

import org.apache.kafka.clients.consumer.KafkaConsumer

import scala.jdk.CollectionConverters._

object DelayObserver {

  def startObserving(storage: DelayManager) {
    new Thread(new DelayUpdater(storage)).start()
  }
}

class DelayUpdater(storage: DelayManager) extends Runnable {
  private def getKafkaConfig: Properties = {
    val properties = new Properties()
    properties.put("bootstrap.servers", "localhost:9092")
    properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    properties.put("value.deserializer", "org.apache.kafka.common.serialization.IntegerDeserializer")
    properties.put("group.id", "public-transport")
    properties
  }

  def run(): Unit = {
    val kafkaConsumer = new KafkaConsumer[String, Int](getKafkaConfig)
    kafkaConsumer.subscribe(Seq("delays").asJavaCollection)

    while (true) {
      val delays = kafkaConsumer.poll(Duration.ofMillis(10))
      delays.forEach(record => storage.updateDelay(record.key(), record.value()))
    }
  }
}
