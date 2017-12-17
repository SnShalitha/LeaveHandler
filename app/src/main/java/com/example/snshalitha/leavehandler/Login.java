package com.example.snshalitha.leavehandler;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ActionMenuItemView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Login extends AppCompatActivity {
    EditText userName,password;
    Button login;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        login = (Button) findViewById(R.id.btnlogin);
        userName = (EditText) findViewById(R.id.txtuserName);
        password = (EditText) findViewById(R.id.txtpassword);
        progressDialog=new ProgressDialog(this);



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Doregister doregister = new Doregister();
                doregister.execute("");

            }
        });
    }


    private class Doregister extends AsyncTask<String, String, String> {
        String z=null;
        boolean is_succes=false;


        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Loading....");
            progressDialog.show();
        }


        @Override
        protected String doInBackground(String... strings) {
            String usrname = userName.getText().toString();


            String pswd = password.getText().toString();


            if (usrname.trim().equals("") || pswd.trim().equals("")) {
               z="Enter A Password Or User Name";
            } else {
                try {
                    Connection conn = DbConnect();
                    if (conn == null)
                        z=("Check Your Connection ");
                    else {
                        Statement st = conn.createStatement();

                         String sql = ("select * from teacher where UserName='"+usrname.toString()+"'and Password='"+pswd.toString()+"'");
                        ResultSet set = st.executeQuery(sql);

                        if (set.next()) {
                            z="Login Succes";
                            is_succes=true;
                            conn.close();
                        } else {
                            is_succes = false;
                            z = "Invalid Login";
                        }
                    }
                } catch (SQLException ex) {
                    is_succes=false;
                    z=ex.getMessage();
                }

            }
            return z;
        }
        @Override
        protected void onPostExecute(String s) {
            progressDialog.hide();
            if(is_succes){
                Toast.makeText(Login.this,"Login Succesfull",Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(Login.this,"Login Failed",Toast.LENGTH_LONG).show();
            }
        }
    }
    public Connection DbConnect(){

        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
       String url = "192.168.8.102";
       // String url ="127.0.0.1";
        String port = "3306";
        String database = "teachers";
        String relation = "teacher";
        String username = "Sha";
        String password = "";
        String JDBC_DRIVER = "com.mysql.jdbc.Driver";
        Connection conn = null;
        String DB_URL="jdbc:mysql://192.168.8.102:3306/teachers";
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL,username,password);

        }catch (SQLException ex) {
            System.out.println("K1");
            System.out.println(ex.getMessage());;
            Log.e("eror here 1 :",ex.getMessage());
        }catch (ClassNotFoundException ex2){
            System.out.println("K2");
            Log.e("eror here 2 :",ex2.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("K3");
            Log.e("eror here 3:",ex.getMessage());
        }

        return conn;
    }

}
