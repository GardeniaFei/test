/*==============================================================*/
/* Table: EB_MAP_FILE   地图图片存放地址                                           */
/*==============================================================*/
CREATE TABLE EB_MAP_FILE 
(
   MAP_ID                    NUMBER(10)            NOT NULL,
   MAP_CODE			    VARCHAR2(255)			 not null,
   MAP_TYPE			    NUMBER(10) 	         NOT NULL,
   MAP_CONTEN         BLOB,
   MAP_URL                 VARCHAR2(500),
   CONSTRAINT PK_EB_MAP PRIMARY KEY (MAP_ID)
);

create sequence EB_MAP_seq
increment by 1 start with 100
nomaxvalue nocycle nocache;

/*==============================================================*/
/* Table: EB_BUSS_SHOW   企业展示表                                           */
/*==============================================================*/
CREATE TABLE EB_BUSS_SHOW 
(
   SHOW_ID                    NUMBER(10)            NOT NULL,
   BUSS_ID			           NUMBER(10) 			 not null,
   ORDER_SEQ                 NUMBER(10) 	NOT NULL,
   MAP_CODE			       VARCHAR2(255), 	         
   SHOW_INTRO             VARCHAR2(500),
   REFRESH_TIME            VARCHAR2(255),
   CONSTRAINT PK_EB_SHOW PRIMARY KEY (SHOW_ID)
);

create sequence EB_SHOW_seq
increment by 1 start with 100
nomaxvalue nocycle nocache;

/*==============================================================*/
/* Table: EB_CENTER   中心表                                           */
/*==============================================================*/
CREATE TABLE EB_CENTER 
(
   CENTER_ID                   NUMBER(10)            NOT NULL,
   SLOGAN			           VARCHAR2(500),
   MAP_CODE			       VARCHAR2(255), 	         
   CENTER_INTRO           VARCHAR2(500),
   REFRESH_TIME            VARCHAR2(255),
   CONSTRAINT PK_EB_CENTER PRIMARY KEY (CENTER_ID)
);

create sequence EB_CENTER_seq
increment by 1 start with 100
nomaxvalue nocycle nocache;

/*==============================================================*/
/* Table: EB_INFORMATION   资讯表                                           */
/*==============================================================*/
CREATE TABLE EB_INFORMATION 
(
   INFORMATION_ID                       NUMBER(10)            NOT NULL,
   INFORMATION_TITLE			        VARCHAR2(500),
   INFORMATION_CONTENT			clob,      
   INFORMATION_TYPE                   NUMBER(10), 	
   INFORMATION_BUSS_ID              NUMBER(10),
   REFRESH_TIME                             VARCHAR2(255),
   CONSTRAINT PK_EB_INFORMATION  PRIMARY KEY (INFORMATION_ID)
);

create sequence EB_INFORMATION_seq
increment by 1 start with 100
nomaxvalue nocycle nocache;