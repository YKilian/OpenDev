# login.py

This README tells you how to use the program login.py

## Installations

To use login.py you need to install mySQL and set up a database. The database should contain a table with the columns 'username' and 'password'. Please make sure, that 'username' is the PRIMARY KEY of your table. If you dont do that and there are more users with that username, the program will always take the first one.

You also need to install following packages:

```
pip install mysql-connector-python
```

```
pip3 install customtkinter
```

## Setup

To make the program work, you need to connect your database with python. To do so, fill in all the data between the double quotes in line 13-19

```
# Connect to Server
print("Connecting to SQL server")
my_db = mysql.connector.connect(
    host = "HOST",
    user = "USER",
    password = "PASSWORD",
    database = "DATABASE"
)

usersdata="TABLE"
```

After that everything should work fine and you can start working with the program.

## Usage

If you fill in userdata that exists, the program terminates and the window closes.

If you fill in userdata, that does not exist in your SQL file and press 'Login', you get the message, that something is wrong with the input. In this case the password field gets cleared.

If you want to add userdata to your SQL file, all you need to do here is entering the data you want and press 'Register'. If the username already exists, you will get notified about that. Otherwise, this userdata be added to your SQL file. You then have to press 'Login' if you want to login the user.
