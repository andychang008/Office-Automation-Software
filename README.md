-The database is stored as:

	OfficeAutomationDB.sql
--------------------------------------------------------------------------------------------
-To dump the database:

	/usr/local/mysql/bin/mysqldump -u root -p OfficeAutomationDB>OfficeAutomationDB.sql
--------------------------------------------------------------------------------------------
-To run this program, execute the command:

	java -cp .:mysql-connector-java-8.0.25.jar:jfreechart-1.5.2.jar Welcome
--------------------------------------------------------------------------------------------
-To import the dump file into a database:

	mysqladmin -u root -p create OfficeAutomationDB

	mysql -u root -p OfficeAutomationDB<OfficeAutomationDB.sql
--------------------------------------------------------------------------------------------
-To Log In as an Admin:

	Username: admin
	Password: password
--------------------------------------------------------------------------------------------
*Please refer to the documentation folder in the GitHub Repository for any and all questions regarding the application.
