create table test_result(
  id identity primary key,
  monitoring_id integer not null,
  created_at timestamp not null,
  result integer not null,
  constraint monitoring_fk foreign key (monitoring_id) references monitoring
);
