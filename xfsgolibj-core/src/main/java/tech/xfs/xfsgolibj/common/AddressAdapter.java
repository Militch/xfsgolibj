package tech.xfs.xfsgolibj.common;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class AddressAdapter extends TypeAdapter<Address> {

    @Override
    public void write(JsonWriter jsonWriter, Address address) throws IOException {

    }

    @Override
    public Address read(JsonReader jsonReader) throws IOException {
        JsonToken jt = jsonReader.peek();
        if (jt == JsonToken.NULL){
            jsonReader.nextNull();
            return null;
        }
        String encoded = jsonReader.nextString();
        if (encoded == null || encoded.isEmpty()){
            return null;
        }
        return Address.fromString(encoded);
    }
}
