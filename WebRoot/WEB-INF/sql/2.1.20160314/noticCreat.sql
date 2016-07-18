/*==============================================================*/
/* Table: EB_NOTICE       通知公告表                                */
/*==============================================================*/
create table EB_NOTICE 
(
   NOTICE_ID          				NUMBER(20)            not null,
   NOTICE_TITLE          			VARCHAR2(255),
   NOTICE_CONTENT			clob,
   NOTICE_TIME					VARCHAR2(255)		not null,
   NOTICE_USER_code			VARCHAR2(255),
   NOTICE_USER_name		VARCHAR2(255),
   NOTICE_depart_Id			NUMBER(10),
   NOTICE_depart_name		VARCHAR2(255),
   NOTICE_Buss_Name        VARCHAR2(255),
   BUSINESS_ID					NUMBER(10),
   NOTICE_TYPE					NUMBER(6),
   is_delet              				NUMBER(1),
   constraint PK_EB_NOTICE primary key (NOTICE_ID)
);

/*================== 通知公告表序列 =====================*/
create sequence EB_NOTICE_seq
increment by 1 start with 100
nomaxvalue nocycle nocache;

/*==============================================================*/
/* Table:  EB_BUSS_ITEM      企业类事项表                                */
/*==============================================================*/
create table EB_BUSS_ITEM 
(
   BITEM_ID          				NUMBER(20)            not null,
   BUSER_CODE          			VARCHAR2(255)      not null,
   VUSER_CODE					VARCHAR2(255)      not null,
   BITEM_NAME					VARCHAR2(500)		 not null,
   BUSS_ID							NUMBER(20)            not null,
   BUSS_NAME						VARCHAR2(255),
   CREAT_TIME						VARCHAR2(255),
   BTRAN_STATUS				NUMBER(20),  
   SIGN_TIME						VARCHAR2(255),
   bitem_describe				VARCHAR2(500),
   constraint PK_EB_BUSS_ITEM primary key (BITEM_ID)
);

/*================== 企业事项序列 =====================*/
create sequence EB_BUSS_ITEM_seq
increment by 1 start with 100
nomaxvalue nocycle nocache;

/*==============================================================*/
/* Table: EB_NOTICE_EMAIL       通知邮箱                                */
/*==============================================================*/
create table EB_NOTICE_EMAIL 
(
   EMAIL_ID          				NUMBER(20)            not null,
   EMAIL_NAME          			VARCHAR2(255),
   creat_time					VARCHAR2(255)		not null,
   is_delet              				NUMBER(1),
   constraint PK_EB_EMAIL primary key (EMAIL_ID)
);

/*================== 通知公告表序列 =====================*/
create sequence EB_EMAIL_seq
increment by 1 start with 100
nomaxvalue nocycle nocache;
