--
-- UserDAO SQL test data script
--

insert into USERS(id, username, password, salt, email) values
	(10000, 'user1Test', 'pwd1Hash', 'testSalt', 'test1@email.domain'),
	(10001, 'user2Test', 'pwd2Hash', 'testSalt', 'test2@email.domain');
	