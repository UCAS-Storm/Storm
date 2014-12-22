/*==============================================================*/
/* DBMS name:      MySQL 5.5                                    */
/* Created on:     2014-12-18 10:12:56                          */
/*==============================================================*/

drop database if exists TallyBook;

create database TallyBook;

use TallyBook;

drop table if exists Tenant;

drop table if exists Service;

drop table if exists Category;

drop table if exists BuyService;

drop table if exists Income;

drop table if exists PayOut;

/*==============================================================*/
/* Table: Tenant	租户表(此处也只用户表)                				*/
/*==============================================================*/
create table Tenant
(
   TenantID         varchar(20) not null,
   TenantName       varchar(50),
   Password					varchar(20),
   Tel          		varchar(50),
   Email            varchar(50),
   primary key (TenantID)
);

/*==============================================================*/
/* Table: Service	服务表                                   		  */
/*==============================================================*/
create table Service
(
   ServiceID     int auto_increment not null,
   Money      	 double,
   TimeType      varchar(10), 
   Descri  			 varchar(50),
   primary key (ServiceID)
);

/*==============================================================*/
/* Table: Category	花费类别表                                  */
/*==============================================================*/
create table Category
(
   CategoryID           int not null,
   CategoryName         varchar(50),
   primary key (CategoryID)
);

/*==============================================================*/
/* Table: BuyService 服务购买表                                 */
/*==============================================================*/
create table BuyService
(
   BuyServiceID         int auto_increment not null,
   TenantID             varchar(20),
   ServiceID            int,
   Money                double,
   StartTime            date,  
 	 EndTime         		  date,
 	 Note									varchar(50),
   primary key (BuyServiceID) 
);

/*==============================================================*/
/* Table: Income	        收入表                                */
/*==============================================================*/
create table Income
(
   IncomeID             int auto_increment not null,
   TenantID            	varchar(20),
   CategoryID           int,
   ExpenseTime    		  date,
   Money        		    double, 
   Note                 varchar(50),
   primary key (IncomeID)
);

/*==============================================================*/
/* Table: PayOut  支出                                          */
/*==============================================================*/
create table PayOut
(
   PayOutID             int auto_increment not null,
   TenantID            	varchar(20),
   CategoryID           int,
   ExpenseTime    		  date,
   Money        		    double, 
   Note                 varchar(50),
   primary key (PayOutID)
);

alter table BuyService add constraint FK_relationship_1 foreign key (TenantID)
      references Tenant (TenantID) on delete cascade on update cascade;

alter table BuyService add constraint FK_relationship_2 foreign key (ServiceID)
      references Service (ServiceID) on delete restrict on update restrict;  

alter table Income add constraint FK_relationship_3 foreign key (TenantID)
      references Tenant (TenantID) on delete restrict on update restrict;

alter table Income add constraint FK_relationship_4 foreign key (CategoryID)
      references Category (CategoryID) on delete restrict on update restrict;

alter table PayOut add constraint FK_relationship_5 foreign key (TenantID)
      references Tenant (TenantID) on delete restrict on update restrict;

alter table PayOut add constraint FK_relationship_6 foreign key (CategoryID)
      references Category (CategoryID) on delete restrict on update restrict;
      
/*==============================================================*/
/* 初始化服务表		                                              */
/*==============================================================*/
insert into Service(Money, TimeType, Descri) values(1000, '月', '记账基础功能');
insert into Service(Money, TimeType, Descri) values(2500, '季度', '记账基础功能');
insert into Service(Money, TimeType, Descri) values(9500, '年', '记账基础功能');
insert into Service(Money, TimeType, Descri) values(1500, '月', '记账高级功能');
insert into Service(Money, TimeType, Descri) values(4000, '季度', '记账高级功能');
insert into Service(Money, TimeType, Descri) values(14500, '年', '记账高级功能');

/*==============================================================*/
/* 初始化花费类别表                                             */
/*==============================================================*/
insert into Category(CategoryID, CategoryName) values(1, '餐饮');
insert into Category(CategoryID, CategoryName) values(2, '娱乐');
insert into Category(CategoryID, CategoryName) values(3, '购物');
insert into Category(CategoryID, CategoryName) values(4, '交通');
insert into Category(CategoryID, CategoryName) values(5, '工资');
insert into Category(CategoryID, CategoryName) values(6, '其他');

/*==============================================================*/
/* 初始化租户表		                                              */
/*==============================================================*/
insert into Tenant(TenantID, TenantName, Password, Tel, Email) values('liubei', '刘备', 'liubei', '13121111001', '1234555@qq.com');
insert into Tenant(TenantID, TenantName, Password, Tel, Email) values('xiangyu', '项羽', 'xiangyu', '13121111002', '1234556@qq.com');
insert into Tenant(TenantID, TenantName, Password, Tel, Email) values('sunquan', '孙权', 'sunquan', '13121111003', '1234557@qq.com');

/*==============================================================*/
/* 初始化服务购买表		                                          */
/*==============================================================*/
insert into BuyService(TenantID, ServiceID, Money, StartTime, EndTime) values('liubei', 1, 1000, '2014-12-12 12:10:10', '2015-01-12 12:10:10');
insert into BuyService(TenantID, ServiceID, Money, StartTime, EndTime) values('xiangyu', 4, 1500, '2014-12-12 12:10:10', '2015-01-12 12:10:10');
insert into BuyService(TenantID, ServiceID, Money, StartTime, EndTime) values('sunquan', 2, 25000, '2014-12-12 12:10:10', '2015-03-12 12:10:10');