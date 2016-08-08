package com.znl

import akka.actor.{Props, ActorSystem}
import com.znl.framework.socket.Request
import com.znl.msg.GameMsg
import com.znl.msg.GameMsg.{SessionClose, SessionMessageReceived, SessionOpen}
import com.znl.proto.M1
import com.znl.robot.RobotActor

/**
 * Created by Administrator on 2015/10/29.
 */


object AdminMainServer extends App{

  implicit val system = ActorSystem("Admin")
  import system.dispatcher

//  val actor = system.actorSelection("akka.tcp://game@192.168.10.124:2552/user/adminServer")
//  actor ! GameMsg.GMCommand("stop")

//  system.actorOf(Props(classOf[RobotActor]))
  val startId = 34000
  val robotNum = 1000

  var index = startId
  var flag = true
  while (flag){
//    for(i <- 0 until 100){
//      Thread.sleep(90)
//      system.actorOf(Props(classOf[RobotActor]))
//    }
    index = index + 1
    system.actorOf(RobotActor.props(index))
    Thread.sleep(200)

    if(index == robotNum + startId){
      flag = false
    }
//     Thread.sleep(2000)
  }

//  val actor = system.actorSelection("akka.tcp://game@192.168.10.124:4711/user/gateServer")
//
//  val session = new TestSession()
//  actor ! SessionOpen(session)
//
//  val areId = 9993
//  val account = "woko6"
//
//  session.setAttribute("playerActorName", account + "_" + areId)
//  session.setAttribute("playerAreaId", areId)
//
//  val request = Request.valueOf(1, 9999, M1.M9999.C2S.newBuilder().setType(1).setAreId(areId).setAccount(account).build())
//  actor ! SessionMessageReceived(session, request)
//
//  val requist10000 = Request.valueOf(1, 10000, M1.M10000.C2S.newBuilder().setAccount(account).setAreId(areId).build())
//  actor ! SessionMessageReceived(session, requist10000)

//  actor ! SessionClose(session)


  print("------------------")
}
