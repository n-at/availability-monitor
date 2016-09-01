alter table monitoring
add name varchar(250) not null default 'unknown' ;

alter table monitoring
add active bool;
