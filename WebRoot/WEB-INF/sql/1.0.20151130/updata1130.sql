/* Table: Eb_MESSAGE       留言管理表增加审核状态字段                          */
alter table Eb_MESSAGE add(Exam_status number(1) default 1 not null);