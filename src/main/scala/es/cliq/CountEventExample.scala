package es.cliq

import eventstore._
import eventstore.tcp._
import _root_.akka.actor.{Actor, ActorLogging, ActorSystem, Props, ReceiveTimeout}
import scala.concurrent.duration.DurationInt

object CountAll extends App {
  val system = ActorSystem()
  val connection = system.actorOf(ConnectionActor.props(), "connection")
  val countAll = system.actorOf(Props[CountAll], "count-all")
  system.actorOf(SubscriptionActor.props(connection, countAll, None, None, Settings.Default), "subscription")
}

class CountAll extends Actor with ActorLogging {
  context.setReceiveTimeout(1 second)

  def receive = count(0)

  def count(n: Long, printed: Boolean = false): Receive = {
    case _: IndexedEvent       => context become count(n + 1)
    case LiveProcessingStarted => log.info("live processing started")
    case ReceiveTimeout if !printed =>
      log.info("count {}", n)
      context become count(n, printed = true)
  }
}
