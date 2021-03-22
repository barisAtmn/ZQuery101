# ZQuery 101

- ZIO Query adds an efficient pipelining, batching, and caching to any data source. !!!
  
- ZIO Query helps you dramatically reduce load on data sources and improve performance.
  - Pipelining. ZIO Query detects parts of composite queries that can be combined together for fewer individual requests to the data source.
  - Batching. ZIO Query detects parts of composite queries that can be executed in parallel without changing the semantics of the query.
  - Caching. ZIO Query can transparently cache read queries to minimize the cost of fetching the same item repeatedly in the scope of a query.
  
- Optimizing data-fetching!!!

- ZQuery[R, E, A]

```
trait ZQuery[-R, +E, +A] {
  def step: ZIO[(R, QueryContext), Nothing, Result[R, E, A]]
}

trait Request[+E, +A]  // What will be sent to DB 

trait DataSource[-R, -A] {  // How DB handles Request
  def run(request: Chunk[A]): ZIO[R, Nothing, CompletedRequestMap]
}

def fromRequest[R,E,A,B]
   (request:A)
   (dataSource:DataSource[R,A])
   (implicit ev: A <:< Request[E,B]) : ZQuery[R,E,B] = ???
   
Example Query
{

 1) trait UserRequest[+A] extends Request[Nothing,+A]
 
 2) val userDataSource: DataSource[Any, UserRequest] = new DataSource[Any, UserRequest] {
     def run(request:Chunk[UserRequest]): ZIO[Any, Nothing, CompletedRequestMap] = ??? 
 }
 
 3) ZQuery.fromRequest(UserRequest)(userDataSource): ZQuery[-R,+E,+A]
 
}


```

- ZQuery is an effect that returns a Result which can be Blocked, Done or Fail.

- Request : A Request to a data source that may fail with and E or succeed with an A
  - Users extend to describe the requests their data sources support
  - Purely a description of what is being request
  - No logic 
    
- DataSource 
  - able to execute requests of type A using an environment R
  - Parameterized on a collection of requests so can batch
  - Supports polymorphic requests in type safe way
    
- Out of box caching!!!

- writing queries in a high level, compositional style, with confidence that they will automatically be optimized

- ZQuery : Composable, General , Performant

- ZIO[R, E, A] ==> ZQuery[R, E, A] // Instead of ZIO effect

- Separate the description and interpretation of a query 
  - Capture queries as data reflecting parallel and sequential structure
  - Evaluate everything that can be performed in parallel as a batch
  - Repeat sequentially 
 
- Caliban : GraphQL Library for Scala
```
case class Queries(
  users : ZQuery[Any, Nothing, List[User]],
  user  : UserArgs => ZQuery[Any, Nothing, User]
)
---- Just simple change. "ZIO to ZQuery"
case class Queries(
  users : ZIO[Any, Nothing, List[User]],
  user  : UserArgs => ZIO[Any, Nothing, User]
)
```
- Models of Computations
  - Sequential and dependent computations : MONODIC COMPUTATIONS     // for comprehension
  - Parallel and independent computations : APPLICATIVE COMPUTATIONS  // ZQuery.foreachPar()()
    
- ConnectionIO == ZIO[Connection, E, A]