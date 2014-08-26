package de.leanovate.dose.cart.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule

object Json {
  val jsonMapper = {
    val m = new ObjectMapper()
    m.registerModule(DefaultScalaModule)
  }

  def readValue[T](json: String, clazz: Class[T]): T = jsonMapper.readValue(json, clazz)
}
