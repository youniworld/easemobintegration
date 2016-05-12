## 集成环信SDK培训项目

### 主要目的
1. 掌握IM基本概念
2. 理解环信SDK结构
3. 掌握环信重要功能类
5. 掌握如何导入IM SDK
6. 掌握主要集成场景


### 项目执行计划

1. 讲解基本原理
2. 场景式代码练习
3. 简单聊天应用的实现

### 环境准备

* Android SDK
* Eclipse
* [环信SDK3.0版本](http://www.easemob.com/download/im)


### 环信SDK主要类

#### 业务类
**EMClient**

    SDK 入口类
    EMClient.getInstance().init(context,options);
    
    注册
    
    EMClient.getInstance().createAccount(name,pwd);
    
    登录
    
    EMClient.getInstance().login(name,pwd);
    
**EMChatManager**

    主要聊天类: 消息收发，会话管理
    
    EMChatManager chatMgr = EMClient.getInstance().chatManager();
    
    sendMessage
    getConversation

**EMContactManager**
    
    聊天好友管理类 : 加减好友，黑名单管理
    
    EMContactManager contactMgr - EMClient.getInstance().contactManager();
    
    getContactsFromServer
    getContacts
    
**EMGroupManager**

    群组管理：加减群，创建群，私有群，公开群管理
    
    EMGroupManager groupMgr = EMClient.getInstance().groupManager();

**EMChatroomManager**

    聊天室管理：加入，离开聊天室
    EMChatroomManager chatroomMgr = EMClient.getInstance().groupManager();
    
    
**EMCallManager**
    
    实时音视频：拨打，接收，IP 电话
    
    EMCallManager callMgr = EMClient.getInstance().callManager();
    
#### 实体类

**EMMessage**

	EMMessageBody
	
		EMTextMessageBody
		
		EMImageMessageBody
		
		EMVoiceMessageBody
		
		EMVideoMessageBody

**EMConversation**

    String 	conversationId ()

**EMContact**

    String 	getUsername ()

**EMGroup**
	 
	String 	getGroupId ()
	 
	String 	getGroupName ()
	
### 主要场景

####SDK初始化

	防止多次重入导致自踢现象
	
	全局事件监听

####登录鉴权

	自动登录
	
	手动登录
	
	登录成功

####消息收发

	消息状态监测
	
	设置消息发送状态回调

####会话列表

    获取会话列表
    
####联系人列表

    获取联系人列表
    
###IM Demo APP 实现

每个人按需求实现一个简单的聊天APP

1. 开屏页面
2. 注册登录页面
3. 主页面包含三个子页面(三个 Fragment)
   1. [会话列表页面](https://github.com/atsiliconvalley/easemobintegration/blob/master/conversation_list.png)
   2. [联系人页面](https://github.com/atsiliconvalley/easemobintegration/blob/master/contact_list.png)
   3. 设置页面
4. [会话页面](https://github.com/atsiliconvalley/easemobintegration/blob/master/conversation.png)
5. 收到消息更新会话列表页面
6. 点击联系人进入会话页面
7. 可以发送文字，图片，和视频消息
8. 每个人必须按要求创建一个分支，提交代码到此分支，老师会检查
	   
	   
