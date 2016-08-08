package com.znl.utils

import java.security.SecureRandom
import java.text.SimpleDateFormat
import java.util.Calendar

import akka.actor.{ActorContext, ActorSelection}
import akka.pattern.ask
import akka.serialization.SerializationExtension
import akka.util.Timeout

import scala.collection.JavaConversions._
import scala.collection.mutable.ArrayBuffer
import scala.concurrent.Await
import scala.concurrent.duration._

/**
 * Created by woko on 2015/10/7.
 */
object GameUtils {

  def set2str[T](set : java.util.Set[T]) ={
    val sb = StringBuilder.newBuilder
    set.foreach( e => {
      sb.append(e + ",")
    })

    sb.toString()
  }

  //TODO 先只处理 set Long数据
  def str2set(str : String) : java.util.Set[java.lang.Long] = {
    val set : java.util.Set[java.lang.Long] = new java.util.HashSet[java.lang.Long]()
    if (str.equals("")){
      set
    }else{
      val ary = str.split(",")
      ary.foreach( e => {
        set.add(java.lang.Long.parseLong(e))
      })

      set
    }
  }

  def list2str[T](list : java.util.List[T])={
    val sb = StringBuilder.newBuilder
    list.foreach( e => {
      sb.append(e + ",")
    })
    sb.toString()
  }

  def str2list(str : String) ={
    val list = new java.util.ArrayList[java.lang.Integer]()
    if (str.equals("")){
      list
    }else{
      val ary = str.split(",")
      ary.foreach( e => {
        list.add(java.lang.Integer.parseInt(e))
      })

      list
    }
  }

  def getPowerMap(cls : Class[_],starWord : String) ={
    var map : Map[String, Integer] = Map()

    cls.getFields.foreach( f => {
      if(f.getName.startsWith(starWord)){
        val name : String = f.getName.replace(starWord, "")
        val value : Integer = f.get(null).asInstanceOf[Integer]
        map += (name  -> value)
      }
    })

    map
  }

  def getDataDefine(cls : Class[_]) = {
    var map : Map[String, AnyRef] = Map()

    cls.getFields.foreach( f => {
      val name : String = f.getName
      val value : AnyRef = f.get(null).asInstanceOf[AnyRef]
      map += (name  -> value)
    })

    map
  }

  //慎用！
  def futureAsk[T](actor : ActorSelection, msg : AnyRef, s : Int = 10) ={
    implicit val timeout = Timeout(30 seconds)
    val future = actor ? msg
    val result = Await.result(future, timeout.duration).asInstanceOf[T]
    result
  }

  var test = true

  var time = (System.currentTimeMillis / 1000).toInt
  def getServerTime = {
     time
  }

  def setServerTime(value : Int) = {
    time = value
  }

  def getServerDateStr() ={
    val format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val lt : Long = time.toLong * 1000
    format.format(lt)
  }

  def getServerDate() ={
    Calendar.getInstance().getTime
  }

  def setServerDate(dateStr : String) ={
    val format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val date = format.parse(dateStr)
    time = (date.getTime / 1000).toInt
    Calendar.getInstance().setTimeInMillis(time)
  }

  val random = new  SecureRandom() //Random() //
  def getRandomValueByRange(bound : Int)  : Int ={
    random.nextInt(bound)
  }

  def getRandomValueByInterval(min : Int, max : Int) : Int = {
    random.nextInt(max) % (max - min + 1) + min
  }

  def getCallStatckString() ={
    val strBuilding = StringBuilder.newBuilder
    val ex = new Throwable();
    val stackElements = ex.getStackTrace
    if(stackElements != null){
      stackElements.foreach( ele => {
        strBuilding.++=(ele.getClassName + "/t")
        strBuilding.++=(ele.getFileName + "/t")
        strBuilding.++=(ele.getLineNumber + "/t")
        strBuilding.++=(ele.getMethodName + "/t")
        strBuilding.++=("-----------------------------------/n")
      })
    }
    strBuilding.toString()
  }

  val preKey = "Game_"

  def getPojoKey(pojoClass : Class[_]) : String = {
    val packageName = pojoClass.getPackage.getName
    val fullName = pojoClass.getName
    val key = fullName.replace(packageName, "")
    preKey + key
  }

  def serialization(context : ActorContext, projo : AnyRef):  String ={
    val serialization = SerializationExtension(context.system)
    val serializer = serialization.findSerializerFor(projo)
    val bytes = serializer.toBinary(projo)
    byte2hex(bytes)
  }

  def unserialization(context : ActorContext, pojoClass : Class[_], byteString: String) ={
    val serialization = SerializationExtension(context.system)
    val serializer = serialization.findSerializerFor(pojoClass)
    val bytes = hex2byte(byteString)
    serializer.fromBinary(bytes)
  }

  def byte2hex(bytes : Array[Byte]) = {
    var hs : String = ""
    var stmp : String = ""
    bytes.foreach( byte =>
      {
        stmp = Integer.toHexString(byte & 0xFF)
        if(stmp.length() == 1){
          hs += "0" + stmp
        }else{
          hs += stmp
        }
      }
    )
    hs
  }

  def hex2byte(str : String) : Array[Byte]= {
    if(str == null)
      null

    val strValue =  str.trim()
    val len = strValue.length()
    if(len == 0 || len % 2 == 1)
      null

    val bytes = new ArrayBuffer[Byte](len / 2)
    for(i <- 0 until len if i % 2 == 0){
      val index = i / 2
      val value = Integer.decode("0x" + str.substring(i, i + 2)).intValue().toByte
      bytes += value
    }
    bytes.toArray
  }

}
