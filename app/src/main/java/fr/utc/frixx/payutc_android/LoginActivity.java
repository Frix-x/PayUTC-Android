package fr.utc.frixx.payutc_android;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity extends Activity {


    private UserLoginTask mAuthTask = null;

    // UI references.
    private EditText mLoginView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        mLoginView = (EditText) findViewById(R.id.username);
        mPasswordView = (EditText) findViewById(R.id.password);

        Button mSignInButton = (Button) findViewById(R.id.login_button);
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mLoginView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String username = mLoginView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(username, password);
            mAuthTask.execute((Void) null);
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUsername;
        private final String mPassword;
        private int resultCon = 0;

        UserLoginTask(String username, String password) {
            mUsername = username;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            //Connexion au CAS - Stage 1
            CasConnexion httpCAS1 = new CasConnexion();
            String tgt = httpCAS1.mkRequest(mUsername, mPassword);

            if (tgt == null || !tgt.startsWith("TGT")) {
                resultCon = 1;
                return false;
            } else {

                //Connexion au CAS - Stage 2
                CasConnexion2 httpCAS2 = new CasConnexion2();
                String ticketCAS = httpCAS2.mkRequest(tgt, "http://localhost");
                if (ticketCAS.startsWith("ST")) {

                    //Connexion au CAS - Stage 3
                    CasConnexion3 httpCAS3 = new CasConnexion3();
                    String lcrep = httpCAS3.mkRequest("http://localhost", ticketCAS, "payutcdev", getString(R.string.app_key));
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
                        //Connexion ok. Setting base user data and return true
                        UserData.manager().setLoginAndId(login, sessionid);
                        return true;
                    } else {
                        resultCon = 2;
                        return false;
                    }
                } else {
                    resultCon = 3;
                    return false;
                }
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            } else {
                mPasswordView.requestFocus();
                if (resultCon == 1) {
                    Toast.makeText(LoginActivity.this, "Identifiants invalides", Toast.LENGTH_SHORT).show();
                } else if (resultCon == 2) {
                    Toast.makeText(LoginActivity.this, "Serveur Payutc injoignable", Toast.LENGTH_SHORT).show();
                } else if (resultCon == 3) {
                    Toast.makeText(LoginActivity.this, "Serveur CAS injoignable", Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}