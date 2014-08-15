package views

object BootstrapHelper {
  import views.html.helper.FieldConstructor

  implicit val bootstrapFields = FieldConstructor(html.bootstrapFieldConstructor.f)
}
