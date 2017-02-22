package deepstreamhub.getting_started;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonElement;

import io.deepstream.DeepstreamClient;
import io.deepstream.DeepstreamFactory;
import io.deepstream.EventListener;
import io.deepstream.MergeStrategy;
import io.deepstream.Record;
import io.deepstream.RecordPathChangedCallback;

public class RecordActivity extends AppCompatActivity {

    EditText firstnameInputField;
    EditText lastnameInputField;
    DeepstreamClient client;
    DeepstreamFactory factory;
    Record record;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        factory = DeepstreamFactory.getInstance();
        client = factory.getClient();

        firstnameInputField = (EditText) findViewById(R.id.input_firstname);
        lastnameInputField = (EditText) findViewById(R.id.input_lastname);

        firstnameInputField.addTextChangedListener(new CustomTextChangedWatcher("firstname"));
        lastnameInputField.addTextChangedListener(new CustomTextChangedWatcher("lastname"));

        record = client.record.getRecord("test-rec13");
        record.setMergeStrategy(MergeStrategy.REMOTE_WINS);
        record.subscribe("firstname", new CustomRecordPathChangedCallback(firstnameInputField), true);
        record.subscribe("lastname", new CustomRecordPathChangedCallback(lastnameInputField), true);
    }

    private class CustomRecordPathChangedCallback implements RecordPathChangedCallback {

        private EditText field;

        CustomRecordPathChangedCallback(EditText editTextField) {
            this.field = editTextField;
        }

        @Override
        public void onRecordPathChanged(String recordName, String path, final JsonElement data) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (data.isJsonNull()) {
                        return;
                    }
                    String newData = data.getAsString();
                    if (field.getText().length() == newData.length()) {
                        return;
                    }
                    field.setText(data.getAsString());
                    field.setSelection(field.getText().length());
                }
            });
        }
    }

    private class CustomTextChangedWatcher implements TextWatcher {

        private String field;

        CustomTextChangedWatcher(String recordField) {
            this.field = recordField;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() == 0) {
                return;
            }
            if (s.toString().length() == 0) {
                return;
            }
            Log.w("dsh", "afterTextChanged:" + s.toString());
            record.set(field, s.toString());
        }
    }
}