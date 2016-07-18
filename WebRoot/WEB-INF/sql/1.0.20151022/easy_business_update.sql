/* Table: EB_BUSINESS       企业信息表增加字段                               */
alter table eb_business add(regist_capital varchar2(255));
alter table eb_business add(operate_scope varchar2(1024));
alter table eb_business add(buss_email varchar2(255));
alter table eb_business add(business_intro varchar2(2048));
alter table eb_business add(add_time varchar2(50));
alter table EB_DEPT_EVAINFO add(exam_status number(1));
alter table eb_business add(web_address varchar2(255));
alter table eb_business add(buss_address varchar2(1024));
alter table eb_user_info add(business_name varchar2(1024));
alter table EB_DEPT_EVAINFO add(business_id number(10));
alter table EB_DEPT_EVAINFO add(business_name varchar2(255));

alter table eb_business drop(project_name);
alter table eb_business add(project_name varchar2(255));

alter table EB_DEPT_EVAINFO add(exam_param_id number(10));

drop table eb_news;
/*==============================================================*/
/* Table: EB_NEWS                                               */
/*==============================================================*/
create table EB_NEWS 
(
   NEWS_ID              NUMBER(20)           not null,
   USER_ID              NUMBER(6)            not null,
   USER_CODE            VARCHAR2(20),
   USER_NAME            VARCHAR2(50),
   NEWS_TYPE            NUMBER(2)            not null,
   NEWS_CONTENT         VARCHAR2(2048),
   COUPLE_BACK          VARCHAR2(255),
   NEWS_TIME            VARCHAR2(50)         not null,
   BUSINESS_ID          NUMBER(6)            not null,
   BUSINESS_NAME        VARCHAR2(255)        not null,
   ITEM_ID              NUMBER(10),
   ITEM_NAME            VARCHAR2(255),
   STATUS               NUMBER(1)            not null,
   DEPART_ID            NUMBER(10),
   DEPART_NAME          VARCHAR2(255),
   constraint PK_EB_NEWS primary key (NEWS_ID)
);