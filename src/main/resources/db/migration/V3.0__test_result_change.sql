create table test_result_difference (
  id identity primary key,
  result_id integer not null,
  prev_result_id integer not null,
  constraint result_fk foreign key (result_id) references test_result on delete cascade,
  constraint prev_result_fk foreign key (prev_result_id) references test_result on delete cascade
);
