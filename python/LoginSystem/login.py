#V1.0

import mysql.connector
import customtkinter
from customtkinter import *

customtkinter.set_appearance_mode("System")
customtkinter.set_default_color_theme("dark-blue")

# Connect to Server
print("Connecting to SQL server")
my_db = mysql.connector.connect(
    host = "HOST",
    user = "USER",
    password = "PASSWORD",
    database = "DATABASE"
)

usersdata="TABLE"

print(f"Connecting to {my_db} successfull")
my_cursor = my_db.cursor()

show_password = FALSE

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

    def username_matches_password(self, user):
        sql = f"""
            SELECT password FROM {usersdata}
            WHERE username = '{user}'
            """
        my_cursor.execute(sql)
        result = my_cursor.fetchall()
        if len(result) > 0:
            return result[0][0]
        else:
            return f"{self.password.get()}FAILURE"

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

    def register(self):
        if not self.exists(self.username.get()):
            sql = f"""
            INSERT INTO {usersdata} VALUES (
                '{self.username.get()}', '{self.password.get()}'
            )
            """
            my_cursor.execute(sql)
            my_db.commit()
            print("Registration successfull")
        else: self.failure.configure(text = "Username already exists. Choose another one.", text_color="orange")
        
    
    def exists(self, username):
        sql = f"""
                SELECT username FROM {usersdata}
                WHERE username = '{username}'
            """
        my_cursor.execute(sql)
        result = my_cursor.fetchall()
        if len(result) > 0:
            return True
        return False

    def show_pass(self):
        global show_password
        show_password = not show_password
        if show_password:
            self.password.configure(show="")
        else:
            self.password.configure(show="*")

App().mainloop()
