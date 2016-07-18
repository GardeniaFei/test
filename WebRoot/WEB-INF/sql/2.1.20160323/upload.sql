/* Table:  EB_BUSS_ITEM      企业类事项表               */
alter table EB_BUSS_ITEM ADD province_user_name varchar(255); 
alter table EB_BUSS_ITEM ADD province_user_position varchar(255); 
alter table EB_BUSS_ITEM ADD province_user_tel varchar(255); 
alter table EB_BUSS_ITEM ADD province_user_addresss varchar(255);
alter table EB_BUSS_ITEM ADD province_item_serial varchar(255);  
alter table EB_BUSS_ITEM ADD bitem_type number(6); 

/*==============================================================*/
/* Table: EB_NOTICE       通知公告表                                */
/*==============================================================*/
alter table EB_NOTICE ADD notice_tatus number(6); 