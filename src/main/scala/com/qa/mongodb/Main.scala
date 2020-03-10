package com.qa.mongodb
import org.mongodb.scala._
import com.qa.mongodb.Helpers._
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.model.Updates._
import scala.io.StdIn.readLine

object Main extends App {

  val mongoClient: MongoClient = MongoClient("mongodb://localhost")

  val database: MongoDatabase = mongoClient.getDatabase("mydb")

  val collection: MongoCollection[Document] = database.getCollection("test")

//  collection.drop().results()
//
  val doc: Document = Document("name" -> "MongoDB", "type" -> "database",
    "count" -> 1, "info" -> Document("x" -> 203, "y" -> 102))

  val observable: Observable[Completed] = collection.insertOne(doc)

  observable.subscribe(new Observer[Completed] {

    override def onNext(result: Completed): Unit = println("Inserted")

    override def onError(e: Throwable): Unit = println("Failed")

    override def onComplete(): Unit = println("Completed")
  })


  val documents = (1 to 100) map { i: Int => Document("i" -> i) }

  val insertObservable = collection.insertMany(documents)

  val insertAndCount = for {
    insertResult <- insertObservable
    countResult <- collection.countDocuments()
  } yield countResult

  collection.insertOne(doc).results()

  println(s"total # of documents after inserting 100 small ones (should be 101):  ${insertAndCount.headResult()}")
  collection.find().first().printHeadResult()
  collection.find().printResults()
  collection.find(equal("i", 71)).first().printHeadResult()

//  collection.updateOne(equal("i", 10), set("i", 110)).printHeadResult("Update Result: ")
//  collection.updateMany(lt("i", 100), inc("i", 100)).printHeadResult("Update Result: ")

//  collection.deleteOne(equal("i", 110)).printHeadResult("Delete Result: ")
//  collection.deleteMany(gte("i", 100)).printHeadResult("Delete Result: ")

  def deleteField: Unit = {
    val fieldName = readLine("Please enter the name of the field you would like to change\n")
    val entry = readLine("Please enter the entry you would like to delete\n").toInt
    collection.deleteOne(equal(fieldName, entry)).printHeadResult("Delete Result: ")
  }

  def readById: Unit = {
    val fieldName = readLine("Please enter the name of the field you would like to read\n")
    val entry = readLine("Please enter the entry you would like to read\n").toInt
    collection.find(equal(fieldName, entry)).first().printHeadResult()
  }

  def updateById: Unit = {
    val fieldName = readLine("Please enter the name of the field you would like to update\n")
    val oldEntry = readLine("Please enter the entry you would like to update\n").toInt
    val newEntry = readLine("Please enter the new value for the entry\n").toInt
    collection.updateOne(equal(fieldName, oldEntry), set(fieldName, newEntry)).printHeadResult("Update Result: ")
  }

  def createOneEntry(doc: Document): Unit = {
    collection.insertOne(doc).results()
  }

  val docNew: Document = Document("name" -> "MongoDB", "type" -> "database",
    "count" -> 1, "info" -> Document("x" -> 205, "y" -> 104))

  createOneEntry(docNew)

  readById



}
