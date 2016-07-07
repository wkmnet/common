--选择数据库
use oracle;

--创建数据库表格
drop table if exists toolMenu;
create table if not exists toolMenu(
    id varchar(50) primary key comment '主健',
    parentId varchar(50) not null comment '父菜单主健',
    menuName varchar(60) comment '菜单名称',
    menuLink varchar(200) comment '菜单链接'
)ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8;

--节假日表
drop table if exists holidayList;
create table if not exists holidayList(
    id varchar(16) primary key comment '主健',
    holidayType int(2) not null comment '节假日类型0:工作日;1:休息日;2节假日',
    holidayDesc varchar(10) comment '节假日类型0:工作日;1:休息日;2节假日'
)ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8;

--交易时间表
drop table if exists tradeTime;
create table if not exists tradeTime(
    id varchar(16) primary key comment '主健',
    startTime varchar(10) not null comment '开始时间',
    endTime varchar(10) comment '结束时间'
)ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8;

--U-CLOUD-CACHE
drop table if exists cloud;
create table if not exists cloud(
    id int(32) primary key AUTO_INCREMENT comment '主健',
    ucdnId varchar(50) not null comment 'ucdid',
    url varchar(200) comment '路径'
)ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8;

--Server List
drop table if exists ServerList;
create table if not exists ServerList(
    id int(32) primary key AUTO_INCREMENT comment '主健',
    serverName varchar(50) not null comment '服务名称',
    serverDomain varchar(100) not null comment '服务域名',
    checkInterface varchar(200) not null comment '检查接口',
    status int(1) not null DEFAULT 0 comment '状态:0-异常;1-正常'
)ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8;