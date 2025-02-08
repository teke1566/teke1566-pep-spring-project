drop table if exists message;
drop table if exists account;
create table account (
    accountId int primary key auto_increment,
    username varchar(255) not null unique,
    password varchar(255)
);
CREATE TABLE message (
    messageId INT PRIMARY KEY AUTO_INCREMENT,
    postedBy INT,
    messageText VARCHAR(255) NOT NULL,
    timePostedEpoch BIGINT,
    FOREIGN KEY (postedBy) REFERENCES account(accountId)
);


-- Starting test values with ids of 9999 to avoid test issues
insert into account values (9999, 'testuser1', 'password');
insert into account values (9998, 'testuser2', 'password');
insert into account values (9997, 'testuser3', 'password');
insert into account values (9996, 'testuser4', 'password');

insert into message values (9999, 9999,'test message 1',1669947792);
insert into message values (9997, 9997,'test message 2',1669947792);
insert into message values (9996, 9996,'test message 3',1669947792);

