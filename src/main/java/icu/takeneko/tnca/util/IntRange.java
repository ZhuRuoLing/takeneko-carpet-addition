package icu.takeneko.tnca.util;

import net.minecraft.predicate.NumberRange;

import java.util.Objects;

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

    public static IntRange convert(NumberRange.IntRange range){
        return new IntRange(
                Objects.isNull(range.getMin()) ? Integer.MIN_VALUE : range.getMin(),
                Objects.isNull(range.getMax()) ? Integer.MAX_VALUE : range.getMax()
        );
    }

    @Override
    public String toString() {
        return "[%d, %d]".formatted(from, to);
    }
}
