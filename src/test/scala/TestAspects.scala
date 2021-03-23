import zio.random
import zio.test.Assertion.equalTo
import zio.test.{DefaultRunnableSpec, Gen, Sized, ZSpec, assertM}
import zio.random.Random
import zio.test.TestAspect.flaky
import zio.test.magnolia._
import zio.test.mock.Expectation.value
import zio.test.mock.MockRandom
import zio.test._
object TestAspects extends DefaultRunnableSpec {

  sealed trait Color
  case object Red extends Color
  case object Green extends Color
  case object Blue extends Color

  val genColor: Gen[Random with Sized, Color] = DeriveGen[Color]

  override def spec: ZSpec[_root_.zio.test.environment.TestEnvironment, Any] =
    suite("derivation")(testM("expect call for overloaded method") {
      val app = random.nextInt
      val env = MockRandom.NextInt(value(42))
      val out = app.provideLayer(env)
      assertM(out)(equalTo(42))
    }, testM("sealed traits can be derived") {
      check(genColor)(as => assert(Red)(equalTo(as)))
    } @@ flaky)
}
