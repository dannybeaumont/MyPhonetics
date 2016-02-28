package dannybeaumont.myphonitics.activity;

import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Loader;
import com.koushikdutta.ion.mock.MockLoader;
import com.koushikdutta.ion.mock.MockRequestHandler;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.io.StringReader;
import java.util.ArrayList;

import dannybeaumont.myphonetics.BuildConfig;
import dannybeaumont.myphonetics.MainActivity;
import dannybeaumont.myphonetics.R;
import dannybeaumont.myphonitics.CustomRobolectricGradleTestRunner;
import model.Definition;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;

@RunWith(CustomRobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class MainActivityTest {

    MainActivity subject;
    String json = "{\n" +
            "  \"definitions\": [\n" +
            "    {\n" +
            "      \"definition\": \"an unofficial association of people or groups\",\n" +
            "      \"partOfSpeech\": \"noun\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"definition\": \"a cord-like tissue connecting two larger parts of an anatomical structure\",\n" +
            "      \"partOfSpeech\": \"noun\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"definition\": \"a stripe or stripes of contrasting color\",\n" +
            "      \"partOfSpeech\": \"noun\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"definition\": \"a group of musicians playing popular music for dancing\",\n" +
            "      \"partOfSpeech\": \"noun\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"definition\": \"attach a ring to the foot of, in order to identify\",\n" +
            "      \"partOfSpeech\": \"verb\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"definition\": \"an adornment consisting of a strip of a contrasting color or material\",\n" +
            "      \"partOfSpeech\": \"noun\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"definition\": \"jewelry consisting of a circlet of precious metal (often set with jewels) worn on the finger\",\n" +
            "      \"partOfSpeech\": \"noun\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"definition\": \"a strip of material attached to the leg of a bird to identify it (as in studies of bird migration)\",\n" +
            "      \"partOfSpeech\": \"noun\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"definition\": \"a driving belt in machinery\",\n" +
            "      \"partOfSpeech\": \"noun\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"definition\": \"a range of frequencies between two limits\",\n" +
            "      \"partOfSpeech\": \"noun\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"definition\": \"a restraint put around something to hold it together\",\n" +
            "      \"partOfSpeech\": \"noun\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"definition\": \"a thin flat strip of flexible material that is worn around the body or one of the limbs (especially to decorate the body)\",\n" +
            "      \"partOfSpeech\": \"noun\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"definition\": \"a thin flat strip or loop of flexible material that goes around or over something else, typically to hold it together or as a decoration\",\n" +
            "      \"partOfSpeech\": \"noun\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"definition\": \"bind or tie together, as with a band\",\n" +
            "      \"partOfSpeech\": \"verb\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"definition\": \"instrumentalists not including string players\",\n" +
            "      \"partOfSpeech\": \"noun\"\n" +
            "    }\n" +
            "  ]\n" +
            "}";

    @Before
    public void setup(){
        subject = Robolectric.setupActivity(MainActivity.class);
        setupLoader(Ion.getDefault(RuntimeEnvironment.application));
    }


    @Test
    public void lookupButtonCallsIonService() throws Exception {

        Button button = (Button) subject.findViewById(R.id.button_define);
        button.performClick();
        ArrayList<Definition> testDefinition = subject.getDefined();
        Assert.assertThat(testDefinition.size(), is(15));
    }

    @Test
    public void definitionModelTest(){
        Definition expected = getExpectedDefinition();
        Button button = (Button) subject.findViewById(R.id.button_define);
        button.performClick();
        ArrayList<Definition> testDefinition = subject.getDefined();
        Definition actual = testDefinition.get(0);
        Assert.assertEquals(expected.getDefinition(), actual.getDefinition());
        Assert.assertEquals(expected.getPartOfSpeech(), actual.getPartOfSpeech());
    }

    private Definition getExpectedDefinition() {
        String response = "{\n" +
                "      \"definition\": \"an unofficial association of people or groups\",\n" +
                "      \"partOfSpeech\": \"noun\"\n" +
                "    }";
        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new StringReader(response));
        Definition definition = gson.fromJson(reader,Definition.class);
        return definition;
    }

    private void setupLoader(Ion ion) {
        boolean load = true;
        for (Loader loader: ion.configure().getLoaders()) {
            if (loader instanceof MockLoader)
                load = false;
        }
        if(load){
           setupIonMock();
        }
    }

    private void setupIonMock(){
        MockRequestHandler handler = Mockito.mock(MockRequestHandler.class);
        when(handler.request("https://wordsapiv1.p.mashape.com/words/band/definitions")).thenReturn(getJsonResposne());

        MockLoader.install(Ion.getDefault(RuntimeEnvironment.application), handler);
    }

    private JsonObject getJsonResposne() {
        JsonParser parser = new JsonParser();

        return (JsonObject) parser.parse(json);
    }
}
