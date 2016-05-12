## 环信培训集成SDK项目

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

### 环信SDK主要类

#### 业务类
**EMClient**

    SDK 入口类
    EMClient.getInstance().init(context,options);
    
    注册
    登录
    
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

**EMMessageBody**
