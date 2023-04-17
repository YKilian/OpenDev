# login.py

This README tells you how to use the program login.py

## Installations

To use login.py you need to install mySQL. There is no need to create a database on your own. The system will create everything it needs as you start the program the first time.

You also need to install following packages:

```
pip install mysql-connector-python
```

```
pip3 install customtkinter
```

## Setup

To make the program work, you need to connect your database with python. To do so, fill in all the data between the double quotes in line 11-13

```
# SQL-Data
sql_host = "HOST"
sql_user = "USER"
sql_psw = "PASSWORD"
```

After that everything should work fine and you can start working with the program.

Important to know is that the program doesn't encrypt your data. So everything will be safed as cleartext in your sql database.

## Usage

The program automaticly creates one admin account for you.

```
username: admin001
password: MasterPass123!
```

If you want to add a new user fill in a new username and choose a password.
If there is already a user with this username, the program will tell you by clicking on 'register'. If this username is free and you click on 'register', this user will be created. Click 'login' to log in the user.

If you fill in userdata that exists and click 'login' the program will terminate and closes.

In case you fill in userdata that does not exist and you click 'login', you will get a message, that there is something wrong with the userdata. The password field will get cleared in this case.
