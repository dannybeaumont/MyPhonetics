package dannybeaumont.myphonitics.model;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import dannybeaumont.myphonetics.BuildConfig;
import model.Definition;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class DefinitionTest {
    Definition definition;
    String json = " {\n" +
            "\"definition\": \"Italian open pie made of thin bread dough spread with a spiced mixture of e.g. tomato sauce and cheese\"\n" +
            "\"partOfSpeech\": \"noun\"\n" +
            "}";

    @Before
    public void setup(){
        try {
            JSONObject object = new JSONObject(json);
            definition = new Definition(object);
        } catch (JSONException e) {

        }

    }

    @Test
    public void definitionTest(){

    }
}
