package icu.takeneko.tnca.command.arguments;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import icu.takeneko.tnca.util.IntRange;

import java.util.function.BiPredicate;

public class IntRangeArgumentType implements ArgumentType<IntRange> {

    public static final Dynamic2CommandExceptionType FROM_GREATER_THAN_TO = new Dynamic2CommandExceptionType((from, to) -> new LiteralMessage("Integer " + from + " is greater than " + to));
    public static final SimpleCommandExceptionType INCOMPLETE_EXCEPTION = new SimpleCommandExceptionType(new LiteralMessage("Incomplete IntRange."));
    public static final Dynamic2CommandExceptionType OUT_OF_RANGE = new Dynamic2CommandExceptionType((a, b) -> new LiteralMessage("IntRange %s is not in IntRange %s".formatted(a, b)));

    BiPredicate<IntRange, IntRange> predicate = IntRange::withinInclusive;
    int fromLimit = 0;
    int toLimit = 15;

    IntRange limit = new IntRange(fromLimit, toLimit);

    public IntRangeArgumentType() {
    }

    public IntRangeArgumentType(BiPredicate<IntRange, IntRange> predicate, int fromLimit, int toLimit) {
        this.predicate = predicate;
        this.fromLimit = fromLimit;
        this.toLimit = toLimit;
        limit = new IntRange(fromLimit, toLimit);
    }

    @Override
    public IntRange parse(StringReader reader) throws CommandSyntaxException {
        int start = reader.getCursor();
        int from = reader.readInt();
        while (reader.canRead() && reader.peek() == ' ') {
            reader.read();
        }
        if (!reader.canRead()) {
            throw INCOMPLETE_EXCEPTION.createWithContext(reader);
        }
        int to = reader.readInt();
        if (to < from) {
            reader.setCursor(start);
            throw FROM_GREATER_THAN_TO.createWithContext(reader, from, to);
        }
        IntRange r = new IntRange(from, to);
        if ((fromLimit <= from) && (toLimit >= to)) {
            return r;
        }
        reader.setCursor(start);
        throw OUT_OF_RANGE.createWithContext(reader, r, limit);
    }

    public static IntRangeArgumentType noLimit() {
        return new IntRangeArgumentType();
    }

    public static IntRangeArgumentType withinRangeInclusive(IntRange range) {
        return new IntRangeArgumentType(IntRange::withinInclusive, range.from(), range.to());
    }

    public static IntRangeArgumentType brightnessRange() {
        return withinRangeInclusive(new IntRange(0, 15));
    }
}
