/* Table: EB_SPC_PROGRAM       特别程序表增加字段                               */
alter table eb_spc_program add(depart_id NUMBER(6));
alter table eb_spc_program add(depart_name VARCHAR2(50));

/* Table: EB_DEPART       部门表设置复合主键                               */
alter table EB_DEPART modify (USER_CODE not null);

alter table EB_DEPART drop constraint PK_EB_DEPART cascade;
alter table EB_DEPART add constraints PK_EB_DEPART primary key (depart_id,USER_CODE);

/* Table: EB_ITEM       事项表增加索引                               */
CREATE INDEX index_item_departId ON EB_ITEM(depart_id);

/* Table: EB_TASK_DISTRIBUTE       任务分发表增加索引                               */
CREATE INDEX index_task_distri_taskId ON EB_TASK_DISTRIBUTE(task_id);
CREATE INDEX index_task_distri_departId ON EB_TASK_DISTRIBUTE(depart_id);
CREATE INDEX index_task_distri_itemId ON EB_TASK_DISTRIBUTE(item_id);

/* Table: EB_DEPT_EVAINFO       部门评价表增加索引                               */
CREATE INDEX index_dept_evainfo_departId ON EB_DEPT_EVAINFO(depart_id);

