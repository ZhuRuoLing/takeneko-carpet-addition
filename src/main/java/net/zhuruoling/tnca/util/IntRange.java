package net.zhuruoling.tnca.util;

public class IntRange {
    int from;
    int to;

    public IntRange(int from, int to) {
        this.from = from;
        this.to = to;
    }

    public int from(){
        return from;
    }

    public int to(){
        return to;
    }

    public boolean withinInclusive(IntRange range){
        return range.from >= this.from && this.to <= range.to;
    }

    public boolean withinExclusive(IntRange range){
        return range.from > this.from && this.to < range.to;
    }

    public boolean withinInclusive(int from, int to){
        return from >= this.from && this.to <= to;
    }

    public boolean withinExclusive(int from, int to){
        return from > this.from && this.to < to;
    }

    @Override
    public String toString() {
        return "{" +
                "from: " + from +
                " , to: " + to +
                '}';
    }
}
