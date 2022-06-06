package tech.xfs.xfsgolibj.common;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class HashAdapter extends TypeAdapter<Hash> {
    @Override
    public void write(JsonWriter jsonWriter, Hash hash) throws IOException {

    }

    @Override
    public Hash read(JsonReader jsonReader) throws IOException {
        JsonToken jt = jsonReader.peek();
        if (jt == JsonToken.NULL){
            jsonReader.nextNull();
            return null;
        }
        String hex = jsonReader.nextString();
        if (hex == null || hex.isEmpty()){
            return null;
        }
        return Hash.fromHex(hex);
    }
}
