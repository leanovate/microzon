package models.user

case class Customer(
                     id: Option[Long],
                     email: String,
                     password: Option[String],
                     firstName: Option[String],
                     lastName: Option[String]
                     )
