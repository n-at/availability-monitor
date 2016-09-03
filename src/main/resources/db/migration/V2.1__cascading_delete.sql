alter table test_result
drop foreign key monitoring_fk;

alter table test_result
add constraint monitoring_fk foreign key (monitoring_id) references monitoring on delete cascade;
