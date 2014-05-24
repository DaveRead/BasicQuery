Welcome to BasicQuery.

BasicQuery is a Java-based application used to access 
databases through JDBC. It features a Swing-based GUI 
and includes capabilities useful to developers. It 
also produces timing information, which is valuable 
during tuning exercises.

This readme provides a few details to help you get started.


Building BasicQuery from the Source Code
========================================
BasicQuery includes a build.xml requiring Apache's Ant build tool.  
Once Ant is configured on your system, bring up a command 
prompt, set your working directory to BasicQuery's home directory 
(containing the build.xml file) and type:

ant

The default target (dist) will create the build and dist 
directories, compile the Java files (placing the class files under 
the build directory, and JAR the result, into BasicQuery.jar in 
the dist directory.


Configuration Files
===================
BasicQuery uses 4 files at startup.  These are 
BasicQuery.Connect, BasicQuery.Drivers, 
BasicQuery.Properties, and BasicQuery.SQL

Each of these files should be located in a directory 
called BasicQuery under the user's home directory.  When
BasicQuery is first run it will look for these files.  If
any of these files are missing, a default version of the 
file, supplied in the BasicQUery installation file, will
be copied to the BasicQuery directory and used.  These
default versions are named BasicQuery.Connect.txt, 
BasicQuery.Drivers.txt, BasicQuery.Properties.txt, 
and BasicQuery.SQL.txt


BasicQuery.Properties
---------------------
This is a typical Java properties file containing configuration 
information that is used to configure the GUI and locate resources.  
Many of the entries are changed as a result of user 
actions within the GUI.  The most important entries are:

DBDRIVERDIR=Location of DB Driver Files (JAR and/or ZIP files).  
    This can be an absolute or relative path to the run location

SQLFILE=Location of SQL statement history file.  
    This can be changed within the GUI using the File|Open menu 
    option

BasicQuery.Drivers
------------------
This file contains the set of DB drivers to be loaded at startup 
along with a display description of the driver.  Each driver 
class name and description must be listed on its own line, with 
a comma separating the driver class from the description.  The 
default installation contains driver requests for MySQL, 
Oracle (9/10), Sybase, Apache Jakarta Commons Pooling, and the 
ODBC bridge.

BasicQuery.Connect
------------------
This files contains the history of connect URLs used in the 
application.  The default installation includes a set of 
fictitious URLs as samples for MySQL, Oracle, and Sybase.

BasicQuery.SQL
--------------
This is the default SQL statement history file.  Within the file 
each SQL statement is stored along with a value (0, 1 or 2).  
The associated value, separated from the statement by an open 
square bracket ([), represents how the SQL is to be executed.  
Statements returning resultsets are represented by 0.  
Statements without resultsets (i.e. insert/update/delete) are 
represented by 1.  The value 3 is used for statement returning 
resultsets, where the results are not to be retrieved; rather 
all that is desired is a schema description and timing 
information.


Database Drivers
================
You will need to provide the JDBC driver files from your database 
vendor.  By default these need to be placed in the DBDrivers 
directory under BasicQuery's installation directory.  This 
location can be changed by editing the BasicQuery.Properties.txt 
file and defining the DBDRIVERDIR property to point to a 
different location.


Running BasicQuery
==================
Both a bq.sh file and a bq.bat file are provided to start 
BasicQuery.  The bq.sh file should work on most Linux and UNIX 
systems.  The bq.bat file is for executing BasicQuery on 
Microsoft platforms.  If you only downloaded the source code you 
will need to build the application before these scripts will be 
of any value to you.
