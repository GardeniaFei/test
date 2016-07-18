/* Table: Eb_User_Info       用户表增加字段标识当前用户是否多角色主要用于办事员用户是否属于多部门                               */
alter table Eb_User_Info add(is_mdept number(1) default 0);