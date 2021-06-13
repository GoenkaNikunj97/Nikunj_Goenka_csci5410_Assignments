from flask import *
from flask_mysqldb import MySQL
import MySQLdb.cursors
import databaseOperations,myConfig

app = Flask(__name__)

app.secret_key = "MyKey1234"
app.config['MYSQL_HOST'] = '108.59.80.112'
app.config['MYSQL_USER'] = 'root'
app.config['MYSQL_PASSWORD'] = 'admin'
app.config['MYSQL_DB'] = 'a2'

mysql = MySQL(app)

@app.route('/login')
def login():
    return render_template("loginForm.html")

@app.route('/validateLogin' ,methods = ['POST'])
def validateLogin():
    message = ""
    if request.method == 'POST':
        password = request.form['password']
        email = request.form['email']

        dbObject = databaseOperations.DatabaseOp(mysql)
        result = dbObject.selectUser(email, password)
        if result:
            message = 'Logged In'
            dbObject.setState(email)
            url = myConfig.Config.PROFILE_URL+"/"+email+"/"+result['name']
            return redirect(url, code=200)
        else:
            message = 'User Not Found'
            return render_template('register_message.html',
                                   msg=message,
                                   login=myConfig.Config.LOGIN_URL,
                                   register=myConfig.Config.REGISTER_URL)

if __name__ == "__main__":
    app.run(debug=True, host = "0.0.0.0", port=5001)