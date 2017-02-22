package deepstreamhub.getting_started;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.google.gson.JsonObject;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

import io.deepstream.DeepstreamClient;
import io.deepstream.DeepstreamFactory;
import io.deepstream.DeepstreamRuntimeErrorHandler;
import io.deepstream.Event;
import io.deepstream.LoginResult;
import io.deepstream.Record;
import io.deepstream.Topic;

public class MainActivity extends AppCompatActivity {

    Button eventButton;
    Button rpcButton;
    Button recordButton;
    DeepstreamFactory factory;
    DeepstreamClient client;
    Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        factory = DeepstreamFactory.getInstance();
        ctx = this;

        eventButton = (Button) findViewById(R.id.event_button);
        rpcButton = (Button) findViewById(R.id.rpc_button);
        recordButton = (Button) findViewById(R.id.record_button);

        eventButton.setOnClickListener(new TypeOnClickListener(EventActivity.class));
        rpcButton.setOnClickListener(new TypeOnClickListener(RpcActivity.class));
        recordButton.setOnClickListener(new TypeOnClickListener(RecordActivity.class));

        new LoginTask().execute();
    }

    private class TypeOnClickListener implements View.OnClickListener {

        private Class clazz;

        TypeOnClickListener(Class clazz) {
            this.clazz = clazz;
        }

        @Override
        public void onClick(View v) {
            Intent i = new Intent(getApplicationContext(), this.clazz);
            startActivity(i);
        }
    }

    private class LoginTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {
            try {
                client = factory.getClient("<Your app url>");
                client.setRuntimeErrorHandler(new DeepstreamRuntimeErrorHandler() {
                    @Override
                    public void onException(Topic topic, Event event, String s) {
                        Log.w("dsh", topic + ":" + event + ":" + s);
                    }
                });



            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            LoginResult res = client.login();
            Log.w("dsh", String.valueOf(res.loggedIn()));
            return null;
        }
    }
}
