package deepstreamhub.getting_started;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import io.deepstream.DeepstreamClient;
import io.deepstream.DeepstreamFactory;
import io.deepstream.EventListener;

public class EventActivity extends AppCompatActivity {

    EditText inputField;
    TextView outputField;
    Button submitButton;
    DeepstreamClient client;
    DeepstreamFactory factory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        factory = DeepstreamFactory.getInstance();
        client = factory.getClient();

        submitButton = (Button) findViewById(R.id.submit_button);
        inputField = (EditText) findViewById(R.id.input);
        outputField = (TextView) findViewById(R.id.output);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eventPayload = inputField.getText().toString();
                client.event.emit("test-event", eventPayload);
                inputField.setText("");
            }
        });

        client.event.subscribe("test-event", new EventListener() {
            @Override
            public void onEvent(String s, final Object o) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        outputField.setText((String) o);
                    }
                });
            }
        });


    }
}
