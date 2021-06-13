#REGISTER

from flask import *
from flask_mysqldb import MySQL
import MySQLdb.cursors
import databaseOperations,myConfig


app = Flask(__name__)
app.secret_key = "MyKey1234"
app.config['MYSQL_HOST'] = myConfig.Config.DB_HOST_NAME
app.config['MYSQL_USER'] = myConfig.Config.DB_USER
app.config['MYSQL_PASSWORD'] = myConfig.Config.DB_PASSWORD
app.config['MYSQL_DB'] = myConfig.Config.DB_NAME

mysql = MySQL(app)

@app.route('/')
def home():
    return render_template("index.html",
                           login = myConfig.Config.LOGIN_URL)

@app.route('/register' ,methods = ['POST'])
def register():
    message = ""
    if request.method == 'POST':
        name = request.form['name']
        password = request.form['password']
        email = request.form['email']
        topic = request.form['Topic']

        dbObject = databaseOperations.DatabaseOp(mysql)
        result = dbObject.insert(name, password, email, topic)

        if(result):
            message = "User Registered Sucessfully!!"
        else:
            message = "User Already Exist!"

    return render_template('register_message.html',
                           msg=message,
                           login = myConfig.Config.LOGIN_URL,
                           register = myConfig.Config.REGISTER_URL)

if __name__ == "__main__":
    app.run(host = "0.0.0.0", port=5001)