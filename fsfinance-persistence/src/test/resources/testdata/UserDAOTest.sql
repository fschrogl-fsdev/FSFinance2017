--
-- UserDAO SQL test data script
--

insert into USERS(usr_id, usr_username, usr_password, usr_salt, usr_email) values
	(10000, 'user1Test', 'pwd1Hash', 'testSalt', 'test1@email.domain'),
	(10001, 'user2Test', 'pwd2Hash', 'testSalt', 'test2@email.domain');
	