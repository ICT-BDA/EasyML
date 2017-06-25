drop database if exists oozie;
create database oozie;
grant all privileges on oozie.* to 'oozie'@'localhost' identified by 'oozie';
grant all privileges on oozie.* to 'oozie'@'%' identified by 'oozie';

drop database if exists studio;
create database studio CHARACTER SET 'utf8' COLLATE 'utf8_general_ci';

grant all privileges on studio.* to 'root'@'localhost' identified by '111111';
grant all privileges on studio.* to 'root'@'%' identified by '111111';

flush privileges;

use studio;

source /root/mysql/account.sql;
source /root/mysql/bdajob.sql;
source /root/mysql/dataset.sql;
source /root/mysql/jobcron.sql;
source /root/mysql/oozieaction.sql;
source /root/mysql/ooziejob.sql;
source /root/mysql/program.sql;
source /root/mysql/supernode.sql;
source /root/mysql/category.sql;