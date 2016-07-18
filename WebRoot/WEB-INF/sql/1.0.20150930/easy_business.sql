/*==============================================================*/
/* DBMS name:      ORACLE Version 11g                           */
/* Created on:     2015/8/19 12:06:03                           */
/*==============================================================*/


/*==============================================================*/
/* Table: EB_OVERTIME_NEWS       事项超时统计消息表                                */
/*==============================================================*/
create table EB_OVERTIME_NEWS 
(
   OT_NEWS_ID           NUMBER(10)            not null,
   TITLE          		VARCHAR2(255),
   NEWS_CONTENT			VARCHAR2(1024),
   REFRESH_TIME			VARCHAR2(50)		 not null,
   SEND_STATUS			NUMBER(1)			 not null,
   IS_DELET             NUMBER(1)            not null,
   TASK_ID              NUMBER(10)			 not null,
   ITEM_ID              NUMBER(10)            not null,
   constraint PK_EB_OVERTIME_NEWS primary key (OT_NEWS_ID)
);

/*================== 超时统计表序列 =====================*/
create sequence overtime_news_seq
increment by 1 start with 100
nomaxvalue nocycle nocache;


/*==============================================================*/
/* Table: ATTENTION_TABLE       领导关注企业表                                */
/*==============================================================*/
create table EB_ATTENTION 
(
   ATTENT_ID			NUMBER(10)            not null,
   USER_ID              NUMBER(10)            not null,
   BUSINESS_ID          NUMBER(10)            not null,
   IS_DELET             NUMBER(1)            not null,
   constraint PK_EB_ATTENTION primary key (ATTENT_ID)
);

/*================== 领导关注表序列 =====================*/
create sequence lead_atten_seq
increment by 1 start with 100
nomaxvalue nocycle nocache;

/*==============================================================*/
/* Table: BUSINESS_TABLE        企业信息表                                */
/*==============================================================*/
create table EB_BUSINESS 
(
   BUSINESS_ID          NUMBER(10)            not null,
   BUSINESS_NAME        VARCHAR2(255)        not null,
   PROJECT_NAME         VARCHAR2(255)        not null,
   CONTACT_NAME         VARCHAR2(20)         not null,
   CONTACT_PHONE        VARCHAR2(20)         not null,
   TYPE_ID              NUMBER(10)            not null,
   SIGN_STATUS          NUMBER(1)            not null,
   USER_ID              NUMBER(10),
   USER_CODE      		VARCHAR2(20),
   USER_NAME      		VARCHAR2(20),
   BUILD_USER_ID        NUMBER(10),
   BUILD_USER_CODE      VARCHAR2(20),
   BUILD_USER_NAME      VARCHAR2(20),
   SIGN_TIME            VARCHAR2(50),
   SETTLE_TIME          VARCHAR2(50),
   SETTLE_STATUS        NUMBER(1)            not null,
   DISTRIBUTED_STATUS   NUMBER(1)            not null,
   project_intro        VARCHAR2(255),
   constraint PK_EB_BUSINESS primary key (BUSINESS_ID)
);

/*================== 企业信息表序列 =====================*/
create sequence business_seq
increment by 1 start with 100
nomaxvalue nocycle nocache;

/*==============================================================*/
/* Table: BUSINESS_TYPE_TABLE      企业类型表                             */
/*==============================================================*/
create table EB_BUSINESS_TYPE 
(
   TYPE_ID              NUMBER(10)            not null,
   TYPE_NAME            VARCHAR2(255)        not null,
   REFRESH_TIME         VARCHAR2(50),
   IS_DELET             NUMBER(1)            not null,
   constraint PK_EB_BUSINESS_TYPE primary key (TYPE_ID)
);

/*================== 企业类型序列 =====================*/
create sequence buss_type_seq
increment by 1 start with 100
nomaxvalue nocycle nocache;

/*==============================================================*/
/* Table: DEPART_EVALUATE_TABLE     部门评价表                            */
/*==============================================================*/
create table EB_DEPT_EVAINFO 
(
   PARAM_ID                  NUMBER(10)           not null,
   EVALUATE_ID          NUMBER(10)            not null,
   GRADE                NUMBER(1)            not null,
   CONTENT              VARCHAR2(255),
   DEPART_ID            NUMBER(10)            not null,
   EVA_TIME                 VARCHAR2(50)         not null,
   DEPART_NAME                 VARCHAR2(255)         not null,
   constraint PK_EB_DEPT_EVAINFO primary key (PARAM_ID)
);

/*================== 部门评价序列 =====================*/
create sequence dept_evaluate_seq
increment by 1 start with 100
nomaxvalue nocycle nocache;

/*==============================================================*/
/* Table: DEPART_TABLE        部门表                                  */
/*==============================================================*/
create table EB_DEPART 
(
   DEPART_ID            NUMBER(10)            not null,
   DEPART_NAME          VARCHAR2(100)         not null,
   DEPART_TYPE          NUMBER(1)            not null,
   USER_ID              NUMBER(10),
   REFRESH_TIME         VARCHAR2(50)         not null,
   WORK_TEL         	VARCHAR2(20),
   WORK_ADDRESS         VARCHAR2(255),
   USER_CODE         	VARCHAR2(10),
   constraint PK_EB_DEPART primary key (DEPART_ID)
);

/*================== 部门信息序列 =====================*/
create sequence dept_seq
increment by 1 start with 100
nomaxvalue nocycle nocache;

/*==============================================================*/
/* Table: EVALUATE_TABLE      综合评价表                                  */
/*==============================================================*/
create table EB_EVALUATE 
(
   EVALUATE_ID          NUMBER(10)            not null,
   BUSINESS_ID          NUMBER(10)            not null,
   COMP_GRADE           NUMBER(1)            not null,
   EVALUATE_TIME        VARCHAR2(50)         not null,
   BUSINESS_NAME		VARCHAR2(255),
   constraint PK_EB_EVALUATE primary key (EVALUATE_ID)
);

/*================== 综合评价序列 =====================*/
create sequence evaluat_seq
increment by 1 start with 100
nomaxvalue nocycle nocache;


/*==============================================================*/
/* Table: ITEM_TABLE         事项信息表                                   */
/*==============================================================*/
create table EB_ITEM 
(
   ITEM_ID              NUMBER(10)            not null,
   DEPART_ID            NUMBER(10)            not null,
   DEPART_NAME          VARCHAR2(50),
   TRANSACTION_OBJECT   VARCHAR2(255),
   NEED_CONDITION       VARCHAR2(1024),
   NEED_MATERIAL        VARCHAR2(1024),
   MATERIAL_ANNEX       VARCHAR2(255),
   ITEM_FLOW            VARCHAR2(1024),
   FLOW_ANNEX           VARCHAR2(255),
   TRANSACTION_WINDOW   VARCHAR2(1024),
   ITEM_CHARGE          VARCHAR2(1024),
   ITEM_QUESTIONS       VARCHAR2(1024),
   GIST_LAW             VARCHAR2(1024),
   ITEM_NAME            VARCHAR2(255)        not null,
   TIME_LIMIT           NUMBER(2)            not null,
   REFRESH_TIME         VARCHAR2(50)         not null,
   IS_DELET             NUMBER(1)            not null,
   ITEM_TYPE            NUMBER(1)            not null,
   constraint PK_EB_ITEM primary key (ITEM_ID)
);

/*================== 事项信息序列 =====================*/
create sequence iteminfo_seq
increment by 1 start with 100
nomaxvalue nocycle nocache;

/*==============================================================*/
/* Table: MESSAGE_TABLE     留言信息表                                    */
/*==============================================================*/
create table EB_MESSAGE 
(
   MESSAGE_ID           NUMBER(10)            not null,
   BUSINESS_NAME        VARCHAR2(255)        not null,
   PARAM_ID             NUMBER(10),
   MESSAGE_CONTENT      VARCHAR2(1024)       not null,
   MESSAGE_TIME         VARCHAR2(50)         not null,
   STATUS               NUMBER(1)            not null,
   USER_ID              NUMBER(10)            not null,
   USER_CODE            VARCHAR2(20),
   USER_NAME            VARCHAR2(255)        not null,
   BUSINESS_ID          NUMBER(10)            not null,
   DEPART_ID            NUMBER(10),
   ITEM_ID              NUMBER(10),
   constraint PK_EB_MESSAGE primary key (MESSAGE_ID)
);

/*================== 留言信息序列 =====================*/
create sequence message_seq
increment by 1 start with 100
nomaxvalue nocycle nocache;

/*==============================================================*/
/* Table: NEWS_TABLE       消息信息表                                     */
/*==============================================================*/
create table EB_NEWS 
(
   NEWS_ID              NUMBER(20)           not null,
   USER_ID              NUMBER(10)            not null,
   USER_CODE            VARCHAR2(20),
   ITEM_ID              NUMBER(10)            not null,
   STATUS               NUMBER(1)            not null,
   COUPLE_BACK          VARCHAR2(255),
   NEWS_TIME            VARCHAR2(50)         not null,
   BUSINESS_ID          NUMBER(10)            not null,
   BUSINESS_NAME		VARCHAR2(255)        not null,
   SEND_STATUS          NUMBER(1)			 not null,
   DEPART_ID            NUMBER(10),
   NEWS_TYPE            NUMBER(1),
   constraint PK_EB_NEWS primary key (NEWS_ID)
);

/*================== 消息信息序列 =====================*/
create sequence news_seq
increment by 1 start with 100
nomaxvalue nocycle nocache;

/*==============================================================*/
/* Table: SPC_PROGRAM_TABLE    特别程序表                                 */
/*==============================================================*/
create table EB_SPC_PROGRAM
(
   PROGRAM_ID           NUMBER(10)            not null,
   BUSINESS_ID          NUMBER(10)            not null,
   TRANSACTION_WAY      VARCHAR2(255),
   TIME_LIMIT           NUMBER(2)            not null,
   BUILD_TIME           VARCHAR2(50)         not null,
   ADDRESS              VARCHAR2(255),
   CONTACT_PHONE        VARCHAR2(20),
   CONTACT_NAME         VARCHAR2(20),
   REASON               VARCHAR2(1024),
   CONMENT              VARCHAR2(1024),
   EXAMINE_STATUS       NUMBER(1)            not null,
   OPINION              VARCHAR2(255),
   ITEM_NAME            VARCHAR2(255),
   ITEM_ID              NUMBER(10),
   USER_ID              NUMBER(10),
   BUILD_USER_ID        NUMBER(10)            not null,
   EXAMIN_TIME          VARCHAR2(50),
   BUSINESS_NAME		VARCHAR2(255),
   constraint PK_EB_SPC_PROGRAM primary key (PROGRAM_ID)
);

/*================== 特别程序序列 =====================*/
create sequence program_seq
increment by 1 start with 100
nomaxvalue nocycle nocache;

/*==============================================================*/
/* Table: TASK_DISTRIBUTE_TABLE    任务清单表                             */
/*==============================================================*/
create table EB_TASK_DISTRIBUTE 
(
   DISTR_ID			    NUMBER(10) 			 not null,
   TASK_ID              NUMBER(10)			 not null,
   ITEM_ID              NUMBER(10)            not null,
   ITEM_NAME            VARCHAR2(100),
   SIGN_STATUS          NUMBER(1)            not null,
   SIGN_USER_ID         NUMBER(10),
   TRANSACTION_STATUS   NUMBER(1)            not null,
   SIGN_TIME            VARCHAR2(50),
   DEPART_ID            NUMBER(10)            not null,
   DEPART_NAME          VARCHAR2(100),
   USER_ID              NUMBER(10)            not null,
   TRANSACTOR_ID        NUMBER(10),
   TRANSACTION_TIME     VARCHAR2(50),
   RETURN_REASON        VARCHAR2(255),
   DISTRIB_TIME         VARCHAR2(50)		 not null,
   status          		NUMBER(1) 			 not null,
   SIGN_USER_CODE     	VARCHAR2(20),
   DISTR_USER_CODE     	VARCHAR2(20),
   TRANS_USER_CODE      VARCHAR2(20),
   ITEM_TYPE            NUMBER(1)            not null,
   constraint PK_TASK_DISTRIBUTE primary key (DISTR_ID)
);

/*================== 任务清单序列 =====================*/
create sequence distr_seq
increment by 1 start with 100
nomaxvalue nocycle nocache;

/*==============================================================*/
/* Table: TASK_LIST_TABLE       企业类型事项配置表                                */
/*==============================================================*/
create table EB_TASK_LIST 
(
   T_ID					NUMBER(10)            not null,
   TYPE_ID              NUMBER(10)            not null,
   ITEM_ID              NUMBER(10)            not null,
   ITEM_NAME         	VARCHAR2(100),
   DEPART_ID            NUMBER(10),
   DEPART_NAME         	VARCHAR2(100),
   IS_DELET             NUMBER(1)            not null,
   REFRESH_TIME         VARCHAR2(50)		 not null,
   ITEM_TYPE             NUMBER(1)            not null,
   constraint TASK_LIST_PRIK primary key (T_ID)
);

/*================== 企业类型事项配置序列 =====================*/
create sequence task_list_seq
increment by 1 start with 100
nomaxvalue nocycle nocache;

/*==============================================================*/
/* Table: TASK_TABLE            任务表                                */
/*==============================================================*/
create table EB_TASK 
(
   TASK_ID              NUMBER(10)            not null,
   USER_ID              NUMBER(10)            not null,
   USER_CODE            VARCHAR2(20),
   TYPE_ID              NUMBER(10)            not null,
   BUSINESS_ID          NUMBER(10)            not null,
   COMP_TIME            VARCHAR2(50),
   COMP_STATUS          NUMBER(1)            not null,
   ADD_TIME             VARCHAR2(50)         not null,
   SIGN_STATUS          NUMBER(1)            not null,
   SIGN_USER_ID         NUMBER(10),
   SIGN_TIME            VARCHAR2(50),
   BUSINESS_NAME		VARCHAR2(255),
   constraint PK_EB_TASK primary key (TASK_ID)
);

/*================== 任务列表序列 =====================*/
create sequence task_seq
increment by 1 start with 100
nomaxvalue nocycle nocache;

/*==============================================================*/
/* Table: USER_INFO             用户信息表                                */
/*==============================================================*/
create table EB_USER_INFO 
(
   USER_ID              NUMBER(10)           not null,
   USER_ACCOUNT         VARCHAR2(20)         not null,
   PASSWORD             VARCHAR2(20)         not null,
   USER_NAME            VARCHAR2(255)        not null,
   ROLE_TYPE            NUMBER(1)            not null,
   DEPART_ID            NUMBER(10),
   DEPART_NAME			VARCHAR2(100),
   AUTHORITY            NUMBER(1),
   BUSINESS_ID          NUMBER(10),
   USER_PHOTO			VARCHAR2(255),
   SID					VARCHAR2(50),
   IS_DELET             NUMBER(1)            not null,
   USER_EMAIL			VARCHAR2(50),
   USER_TEL				VARCHAR2(20),
   constraint PK_EB_USER_INFO primary key (USER_ID)
);

/*================== 用户信息序列 =====================*/
create sequence userinfo_seq
increment by 1 start with 100
nomaxvalue nocycle nocache;

