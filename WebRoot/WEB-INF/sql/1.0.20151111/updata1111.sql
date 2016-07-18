/* Table: EB_SPC_PROGRAM       特别程序表增加字段                               */
alter table eb_news drop(user_id);
alter table eb_news add(user_id NUMBER(10));
alter table eb_spc_program modify(distr_id NUMBER(10));