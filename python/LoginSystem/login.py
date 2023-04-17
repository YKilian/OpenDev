# V1.1.2

import mysql.connector
import customtkinter
from customtkinter import *

customtkinter.set_appearance_mode("System")
customtkinter.set_default_color_theme("dark-blue")

# SQL-Data
sql_host = "HOST"
sql_user = "USER"
sql_psw = "PASSWORD"


sql_db = "database_login_py" # Can be changed if there is already a database with this name

# Connect to Server
print("Connecting to SQL server")
my_db = mysql.connector.connect(
    host = sql_host,
    user = sql_user,
    password = sql_psw
)
print(f"Connecting to {my_db} successfull")
my_cursor = my_db.cursor()

# Connect to database
def reconnect():
    global connected

    my_cursor.execute("SHOW DATABASES")
    all_db = my_cursor.fetchall()
    # Print out all existing databases
    # print(all_db)

    print("Checking databases")
    connected = FALSE
    for db in all_db:
        if db[0] == sql_db:
            print(f'Connecting to {sql_db}')
            my_db.connect(  host = sql_host,
                        user = sql_user,
                        password = sql_psw,
                        db = sql_db)
            print(f"Connecting to f'{sql_db}' successfull")
            connected = TRUE;
            break;

reconnect()

print("connected is", connected)

# If there is no database for this project, this part creates all it needs
if not connected:
    print(f'{sql_db} not found')
    print("Creating database")
    db_create=f"""CREATE DATABASE {sql_db};"""
    my_cursor.execute(db_create)
    print("Creating database successfull")
    reconnect()
    print("Creating table login_data")
    table_one_create="""CREATE TABLE login_data(
    username VARCHAR(25) PRIMARY KEY,
    password VARCHAR(25) NOT NULL
    )"""
    my_cursor.execute(table_one_create)
    print("Creating table login_data successfull")
    print("Creating admin")

    # A admin user is created
    amin_create="""INSERT INTO login_data(username, password) VALUES(
    'admin001','MasterPass123!'
    )"""

    my_cursor.execute(amin_create)
    print("Creating admin successfull")


show_password = FALSE

# Show all user with usernames in the console
# command="""SELECT * FROM login_data"""
# my_cursor.execute(command)
# print(my_cursor.fetchall())

# Main loop
class App(customtkinter.CTk):

    def __init__(self):
        super().__init__()

        # Window configuraator
        self.title("LogIn-System")
        self.geometry("500x400")
        self.eval("tk::PlaceWindow %s center" % self.winfo_toplevel())

        self.login_frame = customtkinter.CTkFrame(self)
        self.login_frame.pack(pady=20, padx=60, fill="both", expand=True)
        self.label = customtkinter.CTkLabel(self.login_frame, text="Login System", font=("Roboto", 24))
        self.label.pack(pady=12, padx=10)
        self.username = customtkinter.CTkEntry(self.login_frame, placeholder_text="username")
        self.username.pack(pady=12, padx=10)
        self.password = customtkinter.CTkEntry(self.login_frame, placeholder_text="password", show="*")
        self.password.pack(pady=12, padx=10)
        self.checkbox = customtkinter.CTkCheckBox(self.login_frame, text="Show Password", command=self.show_pass)
        self.checkbox.pack(pady=4, padx=10)
        self.failure = customtkinter.CTkLabel(self.login_frame, text="")
        self.failure.pack(pady=0, padx=10)
        self.login_button = customtkinter.CTkButton(self.login_frame, text="Login", command=self.login)
        self.login_button.pack(pady=12, padx=15, side= BOTTOM)
        self.register_button = customtkinter.CTkButton(self.login_frame, text="Register", command=self.register)
        self.register_button.pack(pady=12, padx=15, side = BOTTOM)

    # Checking if login data is correct
    def username_matches_password(self, user):
        sql = f"""
            SELECT password FROM login_data
            WHERE username = '{user}'
            """
        my_cursor.execute(sql)
        result = my_cursor.fetchall()
        if len(result) > 0:
            return result[0][0]
        else:
            return f"{self.password.get()}FAILURE"

    # Login the user
    def login(self):
        my_db.commit()
        if self.username_matches_password(self.username.get()) == self.password.get():
            self.failure.configure(text="Success", text_color="green")
            self.quit()
            print("LOGIN",self.username.get())
            print("Application closed as expected")
        else: 
            self.failure.configure(text="Oops. That's not our failure. \n Check username and password", text_color="red")
            self.password.delete(0, len(self.password.get()))

    # Register the user
    def register(self):
        if not self.password.get() == "":
            if not self.exists(self.username.get()):
                sql = f"""
                INSERT INTO login_data VALUES (
                    '{self.username.get()}', '{self.password.get()}'
                )
                """
                my_cursor.execute(sql)
                my_db.commit()
                print("Registration successfull")
            else: self.failure.configure(text = "Username already exists. Choose another one.", text_color="orange")
        else: self.failure.configure(text = "Password can't be empty", text_color="red")

    # Checking if the username already exists
    def exists(self, username):
        sql = f"""
                SELECT username FROM login_data
                WHERE username = '{username}'
            """
        my_cursor.execute(sql)
        result = my_cursor.fetchall()
        if len(result) > 0:
            return True
        return False

    # Handle checkbox for showing password
    def show_pass(self):
        global show_password
        show_password = not show_password
        if show_password:
            self.password.configure(show="")
        else:
            self.password.configure(show="*")

App().mainloop()
