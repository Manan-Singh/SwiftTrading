-- NOTE: This is to locally create the user required, you must have CREATE USER permissions to execute this
CREATE USER 'swift'@'localhost' IDENTIFIED BY 'trading';
-- NOTE: This grants ALL privleges to the db user, you can change this script to configure specific privlegs
GRANT ALL PRIVILEGES ON *.* TO 'swift'@'localhost';
