package deepstreamhub.getting_started;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;

import io.deepstream.DeepstreamClient;
import io.deepstream.DeepstreamFactory;
import io.deepstream.MergeStrategy;
import io.deepstream.Record;
import io.deepstream.RecordPathChangedCallback;
import io.deepstream.RpcRequestedListener;
import io.deepstream.RpcResponse;
import io.deepstream.RpcResult;


public class RpcActivity extends AppCompatActivity {

    DeepstreamClient client;
    DeepstreamFactory factory;

    Button submitButton;
    CheckBox provideCheckBox;
    EditText inputField;
    TextView outputField;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rpc);

        factory = DeepstreamFactory.getInstance();
        client = factory.getClient();

        submitButton = (Button) findViewById(R.id.submit_button);
        provideCheckBox = (CheckBox) findViewById(R.id.provider_check_box);
        inputField = (EditText) findViewById(R.id.input);
        outputField = (TextView) findViewById(R.id.result);
    }

    public void toggleProvide(View view) {
        if (provideCheckBox.isChecked()) {
            client.rpc.provide("to-uppercase", new RpcRequestedListener() {
                @Override
                public void onRPCRequested(String name, Object data, RpcResponse response) {
                    String uppercaseResult = data.toString().toUpperCase();
                    response.send(uppercaseResult);
                }
            });
        } else {
            client.rpc.unprovide("to-uppercase");
        }
    }

    public void makeToUppercase(View view) {
        String data = inputField.getText().toString();
        final RpcResult result = client.rpc.make("to-uppercase", data);
        Log.w("dsh", result.success() + ":" + result.getData().toString());
        if (result.success()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    outputField.setText(result.getData().toString());
                }
            });
        } else {
            Toast.makeText(this, "Error making RPC", Toast.LENGTH_LONG).show();
        }
    }
}

