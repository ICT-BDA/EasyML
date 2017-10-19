drop database if exists oozie;
create database oozie;
grant all privileges on oozie.* to 'oozie'@'localhost' identified by 'oozie';
grant all privileges on oozie.* to 'oozie'@'%' identified by 'oozie';

drop database if exists studio;
create database studio CHARACTER SET 'utf8' COLLATE 'utf8_general_ci';

grant all privileges on studio.* to 'root'@'localhost' identified by '111111';
grant all privileges on studio.* to 'root'@'%' identified by '111111';

drop database if exists test;
create database test CHARACTER SET 'utf8' COLLATE 'utf8_general_ci';
grant all privileges on test.* to 'root'@'localhost' identified by '111111';
grant all privileges on test.* to 'root'@'%' identified by '111111';

flush privileges;

use studio;
source /root/mysql/studio/account.sql;
source /root/mysql/studio/bdajob.sql;
source /root/mysql/studio/dataset.sql;
source /root/mysql/studio/jobcron.sql;
source /root/mysql/studio/oozieaction.sql;
source /root/mysql/studio/ooziejob.sql;
source /root/mysql/studio/program.sql;
source /root/mysql/studio/supernode.sql;
source /root/mysql/studio/category.sql;
source /root/mysql/studio/moduleversion.sql;

use test;
source /root/mysql/test/titanic_train.sql;