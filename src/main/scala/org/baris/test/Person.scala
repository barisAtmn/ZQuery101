package org.baris.test

case class Person(givenName: String, familyName: String) {
  override def toString: String = s"$givenName $familyName"
}
