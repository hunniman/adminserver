package com.znl.robot

import java.security.SecureRandom
import java.util
import java.util.Collections

import akka.actor.{Props, Actor}
import akka.actor.Actor.Receive
import com.znl.TestSession
import com.znl.framework.socket.Request
import com.znl.msg.GameMsg.{SessionClose, SessionMessageReceived, SessionOpen}
import com.znl.proto.Common.FightElementInfo
import com.znl.proto._
import com.znl.utils.GameUtils

import scala.concurrent.duration._
/**
 * Created by Administrator on 2015/12/3.
 */

object RobotActor{
  def props(roleId : Int) = Props(classOf[RobotActor], roleId)
}

class RobotActor(roleId : Int) extends Actor{

  import context.dispatcher

  val offLineTime = GameUtils.getRandomValueByInterval(600, 4000)
//  val onLineTime = GameUtils.getRandomValueByInterval(100, 700)
  context.system.scheduler.schedule(offLineTime seconds,offLineTime seconds,context.self, "stop")
//  context.system.scheduler.schedule(60 seconds,60 seconds,context.self, "stop")

  context.system.scheduler.schedule(10 seconds,10 seconds,context.self, "CheckHeartbeat")
  context.system.scheduler.scheduleOnce(5 seconds,context.self,"SetName")

  var chatTime = GameUtils.getRandomValueByInterval(10, 50)
  context.system.scheduler.schedule(chatTime seconds, chatTime seconds,context.self, "Chat")
  chatTime = GameUtils.getRandomValueByInterval(50, 120)
//  context.system.scheduler.schedule(chatTime seconds,chatTime seconds,context.self, "MapTest")

  //192.168.10.124 203.195.140.103
  val actor = context.actorSelection("akka.tcp://game@192.168.10.240:4711/user/root/gateServer")
  val session = new TestSession()

  var isStop = false

  override def preStart() ={
    startRobot()
  }

  override def receive: Receive = {
    case "stop"=>
      if(isStop){
        startRobot()
      }else{
        stopRobot()
      }
    case "CheckHeartbeat" =>
      checkHeartbeat()
    case "Chat" =>
      chat()
    case "SetName" =>
      setName()
    case "MapTest" =>
      mapTest()
    case _=>
  }


  def startRobot() ={
    isStop = false
    actor ! SessionOpen(session)

    val areId = 9989
    val account = "dd" + roleId

    session.setAttribute("playerActorName", account + "_" + areId)
    session.setAttribute("playerAreaId", areId)

    //请求网关
    val request = Request.valueOf(1, 9999, M1.M9999.C2S.newBuilder().setType(1).setAreId(areId).setAccount(account).build())
    request.setSession(session)
    actor ! SessionMessageReceived(session, request)

    //登录
    val requist10000 = Request.valueOf(1, 10000, M1.M10000.C2S.newBuilder().setAccount(account).setAreId(areId).build())
    requist10000.setSession(session)
    actor ! SessionMessageReceived(session, requist10000)
  }

  def stopRobot() ={
    isStop = true
    actor ! SessionClose(session)
  }

  import scala.collection.JavaConversions._
  val gmCommonList = new util.ArrayList[String]()
  val itemList =  new util.ArrayList[Int]()
  for(i <- 1024 until 1090){
    itemList.add(0, i)
  }
  itemList.foreach( typeId => {
    gmCommonList.add("zb addItem %d 1".format(typeId))  //增加道具
  })

  val soldierList = new util.ArrayList[Int]()
  for(i <- 101 until 109){
    soldierList.add(0, i)
  }
  for(i <- 201 until 209){
    soldierList.add(0, i)
  }
  for(i <- 301 until 309){
    soldierList.add(0, i)
  }
  for(i <- 401 until 409){
    soldierList.add(0, i)
  }

  soldierList.foreach( typeId => {
    gmCommonList.add("zb addSoldier %d 1".format(typeId))  //增加佣兵
  })

  for(i <- 1 until 17){
    gmCommonList.add("zb buildup %d 1".format(i))
  }

  for(i <- 10 until 100){
    gmCommonList.add("zb charge %d".format(i))
  }

  for(i <- 10 until 100){
    gmCommonList.add("这是一句来自机器人的对话")
  }

  //设置名字
  def setName(): Unit ={
    val charList = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
    val name = new StringBuffer()
    var index = 4
    while (index > 0){
      val ran = getRandomValueByRange(charList.length)
      val str = charList.charAt(ran)
      name.append(str)
      index = index-1
    }
    val requist20008 = Request.valueOf(2, 20008, M2.M20008.C2S.newBuilder().setName(name.toString).setSex(1).build())
    requist20008.setSession(session)
    actor ! SessionMessageReceived(session, requist20008)
  }

  //世界
  def mapTest(): Unit ={
    val code = getRandomValueByRange(3)
    val x = getRandomValueByRange(599)
    val y = getRandomValueByRange(599)
    if (code == 0){
      //发送80000协议
      val requist80000 = Request.valueOf(8, 80000, M8.M80000.C2S.newBuilder().setX(x).setY(y).build())
      requist80000.setSession(session)
      actor ! SessionMessageReceived(session, requist80000)
    }else if(code == 1){
      val M80001 = M8.M80001.C2S.newBuilder()
      M80001.setX(x).setY(y).
        addTeam(getFightElementInfo(100,1,405)).
        addTeam(getFightElementInfo(100,2,305)).
        addTeam(getFightElementInfo(100,3,404)).
        addTeam(getFightElementInfo(100,4,403)).
        addTeam(getFightElementInfo(100,5,205))
      val requist80001 = Request.valueOf(8, 80001, M80001.build())
      requist80001.setSession(session)
      actor ! SessionMessageReceived(session, requist80001)
    }else{
      val requist80002 = Request.valueOf(8, 80002, M8.M80002.C2S.newBuilder().setX(x).setY(y).setType(2).build())
      requist80002.setSession(session)
      actor ! SessionMessageReceived(session, requist80002)
    }

  }

  def getFightElementInfo(num : Int,post : Int,typeId : Int): Common.FightElementInfo ={
    val  info1 = Common.FightElementInfo.newBuilder()
    info1.setNum(num)
    info1.setPost(post)
    info1.setTypeid(typeId)
    info1.build()
  }

  //聊天
  def chat() ={

    Collections.shuffle(gmCommonList)
    val index = getRandomValueByRange(gmCommonList.size())
    val context = gmCommonList.get(index)
    val requist140000 = Request.valueOf(14, 140000, M14.M140000.C2S.newBuilder().setType(1).setContext(context).build())
    requist140000.setSession(session)
    actor ! SessionMessageReceived(session, requist140000)
  }

  val random = new SecureRandom()

  //Random() //
  def getRandomValueByRange(bound: Int): Int = {
    random.nextInt(bound)
  }

  //心跳包
  def checkHeartbeat() ={
    val requist8888 = Request.valueOf(28, 280015, M28.M280015.C2S.newBuilder().build())
    requist8888.setSession(session)
    actor ! SessionMessageReceived(session, requist8888)
  }


}
