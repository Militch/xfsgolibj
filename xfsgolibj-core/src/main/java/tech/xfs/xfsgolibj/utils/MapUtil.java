package tech.xfs.xfsgolibj.utils;

import java.util.*;

public class MapUtil {
    public static <T> List<T> exportMap(Map<?, T> map){
        List<T> list = new ArrayList<>();
        for (Object hash : map.keySet()){
            list.add(map.get(hash));
        }
        return list;
    }
    public static String sortAndSerialize(Map<String,String> params, String[] excludeKeys){
        Set<String> keys = params.keySet();
        List<String> keyList = new ArrayList<>(keys);
        Collections.sort(keyList);
        StringBuilder sb = new StringBuilder();
        List<String> excludeKeyList = excludeKeys==null?new ArrayList<>()
                :Arrays.asList(excludeKeys);
        for (int i=0; i<keyList.size(); i++){
            String key = keyList.get(i);
            if (excludeKeyList.contains(key))
                continue;
            String value = params.get(key);
            if (value == null || value.isEmpty()){
                continue;
            }
            sb.append(key).append("=")
                    .append(params.get(key))
                    .append(i<keyList.size()-1?"&":"");
        }
        return sb.toString();
    }
}
