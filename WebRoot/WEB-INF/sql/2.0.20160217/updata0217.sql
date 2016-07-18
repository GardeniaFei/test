/* Table: EB_TASK_DISTRIBUTE        任务分发表 增加部门分发人               */
alter table EB_TASK_DISTRIBUTE ADD depart_distr_code varchar(20); 

/* Table: EB_BUSINESS        企业信息表增加VIP服务专员字段                              */
 ALTER TABLE EB_BUSINESS ADD service_admin varchar(20) ; 
 
 
/* Table: EB_DEPART        部门表中修改user_code字段可为空                              */
 alter table eb_depart drop constraints pk_eb_depart;
 alter table EB_DEPART modify( user_code null);
 alter table eb_depart add constraint pk_eb_depart primary key(depart_id);
 
 /*              将所有的表中的depart_id中的数据精度都增大                 */
alter table eb_depart modify(depart_id number(20));
alter table eb_depart modify(user_code varchar2(20));
alter table EB_DEPT_EVAINFO modify(depart_id number(20));
alter table EB_ITEM modify(depart_id number(20));
alter table EB_MESSAGE modify(depart_id number(20));
alter table EB_NEWS modify(depart_id number(20));
alter table EB_SPC_PROGRAM modify(depart_id number(20));
alter table EB_TASK_DISTRIBUTE modify(depart_id number(20));
alter table EB_USER_INFO modify(depart_id number(20));
alter table EB_TASK_LIST modify(depart_id number(20));

alter table EB_TASK_DISTRIBUTE ADD service_admin varchar(255);

/*==============================================================*/
/* Table: EB_FILE_IMG   文件图片存放地址                                           */
/*==============================================================*/
CREATE TABLE EB_FILE_IMG 
(
   IMAGE_ID             NUMBER(10)           NOT NULL,
   DISTR_ID			    NUMBER(10) 			 not null,
   IMAGE_Name			VARCHAR2(500)        NOT NULL,
   IMAGE_URL            VARCHAR2(500),
   IMAGE_CONTEN         BLOB,
   CONSTRAINT PK_EB_IMG PRIMARY KEY (IMAGE_ID)
);
alter table eb_depart add(IS_DELET number(1));
alter table EB_TASK_DISTRIBUTE add(CONTROL_SEQ varchar(255));

create sequence EB_FILE_seq
increment by 1 start with 100
nomaxvalue nocycle nocache;
