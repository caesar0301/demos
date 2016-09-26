package io.transwarp.demo

import java.util.Date

import org.json4s.NoTypeHints
import org.json4s.native.Serialization

case class Project(name: String, startDate: Date, lang: Option[Language], teams: List[Team])
case class Language(name: String, version: Double)
case class Team(role: String, members: List[Employee])
case class Employee(name: String, experience: Int)

object JSON4S {

  def main(args: Array[String]): Unit = {
    import Serialization.{write => swrite}

    implicit val formats = Serialization.formats(NoTypeHints)
    // implicit val formats = Serialization.formats(ShortTypeHints(List(classOf[Project])))

    val project = Project("test", new Date, Some(Language("Scala", 2.75)), List(
      Team("QA", List(Employee("John Doe", 5), Employee("Mike", 3))),
      Team("Impl", List(Employee("Mark", 4), Employee("Mary", 5), Employee("Nick Noob", 1)))))

    val ser = swrite(List(project, project))
    println(ser)
  }
}
