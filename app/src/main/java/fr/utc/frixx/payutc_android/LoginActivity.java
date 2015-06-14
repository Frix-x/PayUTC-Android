package fr.utc.frixx.payutc_android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;


public class LoginActivity extends AppCompatActivity {
    public final static String LOGIN_MESSAGE = "fr.utc.payutc.loginmessage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final Button btnCon = (Button) findViewById(R.id.btnConnect);
        btnCon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText usernameText = (EditText) findViewById(R.id.txtUsername);
                String username = usernameText.getText().toString();
                EditText passwdText = (EditText) findViewById(R.id.txtPassword);
                String passwd = passwdText.getText().toString();

                CasConnexion c = new CasConnexion();
                String response = null;
                try {
                    response = c.execute(username, passwd).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                if (response == null || !response.startsWith("TGT")) {
                    Toast.makeText(LoginActivity.this, "Identifiants invalides", Toast.LENGTH_SHORT).show();
                } else {
                    //ok
                    TGTConnexion tgt = new TGTConnexion();
                    String stresp = null;
                    try {
                        stresp = tgt.execute(response, "http://localhost").get();
                        System.out.println(stresp);

                        if (stresp.startsWith("ST")) {
                            LoginCas lc = new LoginCas();
                            String lcrep = null;
                            try {
                                lcrep = lc.execute("http://localhost", stresp, "payutcdev", getString(R.string.app_key)).get();
                                JSONObject jObject = null;
                                try {
                                    jObject = new JSONObject(lcrep);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                String sessionid = null, login = null;
                                try {
                                    sessionid = jObject.getString("sessionid");
                                    login = jObject.getString("username");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                if (sessionid != null) {
                                    UserData.manager().setLoginAndId(login, sessionid);
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    //intent.putExtra(LOGIN_MESSAGE, lcrep);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(LoginActivity.this, "Serveur Payutc injoignable", Toast.LENGTH_SHORT).show();
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "Serveur CAS injoignable", Toast.LENGTH_SHORT).show();
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

}
