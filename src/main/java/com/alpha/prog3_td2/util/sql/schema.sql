create type continent as enum('AFRICA', 'ASIA', 'EUROPA', 'AMERICA');
create type player_position as enum('GK', 'DF', 'MIDF', 'STR');
create table Team( id int primary key not null,
                   name varchar(100) not null,
                   player_continent continent
);
create table Player(id int primary key not null,
                    name varchar(100) not null,
                    position player_position,
                    id_team int references Team(id)
);