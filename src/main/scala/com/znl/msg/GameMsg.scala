package com.znl.msg

import java.util

import akka.actor.ActorRef
import akka.util.ByteString

import com.znl.proto.M3.{TimeInfo, M30000}
import com.znl.proto.M5

import org.apache.mina.core.session.IoSession
import com.znl.framework.socket.{ Request}
/**
 * Created by woko on 2015/10/7.
 */
object GameMsg {

  ///////////////////////GateServer相关消息类型
  final case class SessionOpen(ioSession: IoSession)  //网络链接 连接
  final case class SessionMessageReceived(ioSession: IoSession, request: Request) //接受到网络消息
  final case class SessionClose(ioSession: IoSession) //网络链接关闭
  final case class AutoClearOffLine() //自动清除下线玩家

  final case class SendPlayerNetMsg(accountName : String, request: Request) //发送网络数据给具体player actor
  final case class CreatePlayerActor(accountName : String, areaId : Int, ioSession: IoSession) //创建player actor
  final case class StopPlayerActor(accountName : String)  //停止player actor
  final case class CreatePlayerActorSuccess(id : Long, accountName : String)  //创建玩家成功
  final case class StopPlayerActorSuccess(id : Long)  //成功停止玩家，则表示下线
  final case class GetPlayerIsOnline(id : Long)  //判断玩家是否在线

  final case class GetPlayerSimpleInfo(id : Long) //获取玩家数据
//  final case class GetPlayerSimpleInfoSuccess(simplePlayer: SimplePlayer) //获取玩家数据成功
//
//  //////////////////////MailService相关消息类型
//  final case class SendMail(players : List[Long],rewardIds : String,content : String,title : String)
//
//  //////////////////////worldService世界地图相关消息/////////////////////////////
//  final case class CompleteInitWorldBlock(worldBlock : WorldBlock)  //初始化完成世界块
//  final case class UpdateWorldBlock(worldBlock : WorldBlock)  //更新世界块信息
//  final case class AddWorldBuildingToTile(playerId : Long, accountName : String)  //添加世界建筑到格子
//
//  final case class AutoAddBuilding(playerId : Long, accountName : String)  //系统达到一定层度，自动添加建筑
//  final case class AddWorldBuildingSuccess(playerId : Long, accountName : String, buildingId : Long)  //添加建筑成功 通知到具体的玩家
//
//  final case class WatchBuildingTileInfo(x : Int, y : Int)  //查看x y周围的各种信息
//  final case class WatchBuildingTileInfoBack(list : java.util.List[WorldTile]) //返回查看的相关信息列表
//
//  /////////////////net/////////////////
//  final case class ReceiveNetMsg(request: Request) //player actor接受到网络数据
//  final case class SendNetMsgToClient(response: Response) //player actor发送网络消息直接到客户端
//  final case class PushtNetMsgToClient()//推送协议到客户端
//  final case class SendNetMsg(response: Response) //模块发送消息结构体到playerActor
//  final case class MulticastNetToClient() //将缓存在playerActor上的协议消息体一起发送到客户端
//  ////////////////////////////////////////////////
//
//  ////////////////////db////////////////////////////////////////////////
//  final case class SaveDBPojo(pojo : BaseDbPojo)
//  final case class DelDBPojo(pojo : BaseDbPojo)  //将DB数据删除，这里需要做一级日志，确保出线问题可以还原
//  final case class CreateDBPojo(pojoClass : Class[_])
//  final case class GetDBPojo(id : Long, pojoClass : Class[_])
//  final case class FinalizeDbPojo(pojo : BaseDbPojo)  //释放掉对应的DB缓存
//
//  final case class GetPlayerByAccountName(name:String)  //通过账号名称 获取角色ID
//  final case class AddPlayerByAccountName(name : String, id : Long, areaKey : String) //添加角色ID
//
//  final case class AddRoleName(roleName : String, areaKey : String)  //添加角色名称到库
//  final case class IsRepeatRoleName(roleName : String, areaKey : String)  //角色名是否重复
//
//
//  final case class GetAllWorldBuildingKey(areaKey : String)  //获取所有的一个人的建筑的KEY列表
//  final case class AddWorldBuildingToDb(buildingTileKey : String, id: Long, areaKey : String) //添加建筑到全局DB，用来记录有多少个建筑了
//  final case class GetWorldBuildingsIdByTileKey(buildingTileKeySet : java.util.List[String], areaKey : String) //获取一组数组的key值
//
//  //mysql
//  final case class InsertToMysql(table : String, id : Long)  //将新的创建的ID插入到MySQL
//  final case class UpdateToMysql(table : String, id : Long, json : String) //将数据结构转成json 更新到mysql
//  final case class DelToMysql(table : String, id : Long) //通过id 删除数据
//  final case class TriggerExecuteToMysql() // 触发运行到MySQL数据库
//
//  ////////////////////////////////////////////////////////
//
//  /////////////////////m -> playerActor//////////////////////////////////
//  final case class LoginSuccess(player : Player, gameProxy: GameProxy)
//  final case class AutoSavePlayer()
  final case class CheckHeartbeat() //检测心跳包
//  final case class SystemTimer(m30000: M30000.S2C.Builder,infoList:util.List[TimeInfo])  //定时器时间
//  final case class BuildInfo(infos:util.List[util.List[Int]])  //建筑信息
//
//  ////////////////////////admin////////////////////////////////
//  final case class GMCommand(command : String)
//
//  ////////////////////server///////////////////////////
//  final case class OnServerTrigger()
//
//  ////////////////chat/////////////////////
//  final case class GetChat(playerIndex : Int,accoutName : String)
//  final case class AddChat(playerChat: PlayerChat)
//  final case class SendChatToPlayer(accoutName : String,chats : util.ArrayList[PlayerChat],chattype : Int,index : Int)
//  final case class GetSystemChatIndex(index : Int,chatType : Int)
//  //////////////////trigger//service///////////////////////////
//  final case class AddTriggerEvent( triggerEvent: TriggerEvent ) //添加触发倒计时
//  final case class RemoveTriggerEvent(triggerEvent: TriggerEvent)
//
//  ////////////////////battle///////////////////////////
//  final case class ReqPuppetList(message : AnyRef)
//  final case class ServerBattleEndHandle(battle : PlayerBattle)//SERVER_BATTLE_END_HANDLE
//  final case class ClientEndHandle(battleId: Int)
//  final case class EndBattle(battle : PlayerBattle)
//  final case class PackPuppet(puppet : M5.PuppetAttr)
//  final case class ErrorBattle(rs : Int)
//
//  ///////////////////soldier//////////////////////
//  final case class FixSoldierList()
//
//  //////////////role////////////////////
//  final case class InitSendRoleInfo()  //playerActor初始化发送角色信息
}
