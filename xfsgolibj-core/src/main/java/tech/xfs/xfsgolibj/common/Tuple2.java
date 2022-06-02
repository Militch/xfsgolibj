package tech.xfs.xfsgolibj.common;

public class Tuple2<U,V> {
    private final U first;
    private final V second;

    public Tuple2(U first, V second) {
        this.first = first;
        this.second = second;
    }

    public U getFirst() {
        return first;
    }

    public V getSecond() {
        return second;
    }
}
