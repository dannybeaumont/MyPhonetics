package dannybeaumont.myphonetics;

import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.async.parser.JSONArrayParser;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import model.Definition;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, TextToSpeech.OnInitListener {

    private final String TAG = MainActivity.this.getClass().getSimpleName();
    Button speakButton;
    Button lookupButton;
    TextToSpeech speech;
    String word;
    Spinner spinnerFirst;
    Spinner spinnerSecond;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        speakButton = (Button) findViewById(R.id.button_speak);
        speakButton.setOnClickListener(this);
        spinnerFirst = (Spinner) findViewById(R.id.spinner);
        spinnerSecond = (Spinner) findViewById(R.id.spinner2);
        textView = (TextView) findViewById(R.id.textView);
        speech = new TextToSpeech(getApplicationContext(),this);
        lookupButton = (Button) findViewById(R.id.button_define);
        lookupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setWord(getSpinnersString());
                Ion.with(getApplicationContext())
                        .load(getString(R.string.url) + getWord() + getString(R.string.definition))
                        .setHeader(getString(R.string.header_key), getString(R.string.mashape_key))
                        .setHeader("Accept", "application/json")
                        .asJsonObject()
                        .withResponse()
                        .setCallback(new FutureCallback<Response<JsonObject>>() {
                            @Override
                            public void onCompleted(Exception e, Response<JsonObject> result) {
                                if (result != null) {
                                    if (result.getResult() != null) {
                                        JsonArray definitions = result.getResult().getAsJsonArray("definitions");
                                        textView.setText("");
                                        ArrayList<Definition> defined = new ArrayList<Definition>();
                                        for(JsonElement definition : definitions){
                                            defined.add(new Definition(definition.getAsJsonObject()));
                                            textView.append(definition.getAsJsonObject().get("definition").getAsString()+"\n");
                                            textView.append(definition.getAsJsonObject().get("partOfSpeech").getAsString()+"\n");
                                        }
                                    }else{
                                        textView.setText("No Word Found");
                                    }

                                }
                            }
                        });
            }
        });
    }

    private void speakWords() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            speech.speak(getWord(), TextToSpeech.QUEUE_FLUSH, MainActivity.this.getIntent().getExtras(), null);
        } else {
            speech.speak(getWord(), TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    @Override
    public void onClick(View v) {
        setWord(getSpinnersString());
        speakWords();
    }

    @Override
    public void onInit(int status) {
        if (status != TextToSpeech.ERROR) {
            speech.setLanguage(Locale.US);
        }
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    private String getSpinnersString() {
        return spinnerFirst.getSelectedItem().toString() + spinnerSecond.getSelectedItem().toString();
    }

}
