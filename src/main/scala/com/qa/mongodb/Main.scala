package com.qa.mongodb
import org.mongodb.scala._
import com.qa.mongodb.Helpers._
import org.mongodb.scala.model.Filters._

object Main extends App {

  val mongoClient: MongoClient = MongoClient("mongodb://localhost")

  val database: MongoDatabase = mongoClient.getDatabase("mydb")

  val collection: MongoCollection[Document] = database.getCollection("test")

//  collection.drop().results()
//
  val doc: Document = Document("_id" -> 0, "name" -> "MongoDB", "type" -> "database",
    "count" -> 1, "info" -> Document("x" -> 203, "y" -> 102))

  val observable: Observable[Completed] = collection.insertOne(doc)

  observable.subscribe(new Observer[Completed] {

    override def onNext(result: Completed): Unit = println("Inserted")

    override def onError(e: Throwable): Unit = println("Failed")

    override def onComplete(): Unit = println("Completed")
  })


//  val documents = (1 to 100) map { i: Int => Document("i" -> i) }
//
//  val insertObservable = collection.insertMany(documents)
//
//  val insertAndCount = for {
//    insertResult <- insertObservable
//    countResult <- collection.countDocuments()
//  } yield countResult
//
//  collection.insertOne(doc).results()

//  println(s"total # of documents after inserting 100 small ones (should be 101):  ${insertAndCount.headResult()}")
  collection.find().first().printHeadResult()
  collection.find().printResults()
//  collection.find(equal("i", 71)).first().printHeadResult()


}
