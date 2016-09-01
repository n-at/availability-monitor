create table monitoring(
  id identity primary key,
  url varchar(250) not null,
  check_interval int not null,
  respond_interval int not null
);
