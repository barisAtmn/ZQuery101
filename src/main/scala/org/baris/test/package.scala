package org.baris

import zio.Has

package object test {

  type Conf = Has[Conf.Root]
  type PersonQueries = Has[PersonQueries.Service]

}
