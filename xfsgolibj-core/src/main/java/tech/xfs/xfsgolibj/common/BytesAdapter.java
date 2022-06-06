package tech.xfs.xfsgolibj.common;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import tech.xfs.xfsgolibj.utils.Bytes;

import java.io.IOException;

public class BytesAdapter extends TypeAdapter<byte[]> {


    @Override
    public void write(JsonWriter jsonWriter, byte[] bytes) throws IOException {

    }

    @Override
    public byte[] read(JsonReader jsonReader) throws IOException {
        JsonToken jt = jsonReader.peek();
        if (jt == JsonToken.NULL){
            jsonReader.nextNull();
            return null;
        }
        String hex = jsonReader.nextString();
        if (hex == null || hex.isEmpty()){
            return null;
        }
        return Bytes.hexToBytes(hex);
    }
}
