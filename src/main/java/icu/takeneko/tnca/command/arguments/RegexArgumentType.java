package icu.takeneko.tnca.command.arguments;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class RegexArgumentType implements ArgumentType<Pattern> {

    public static final DynamicCommandExceptionType REGEX_SYNTAX_EXCEPTION = new DynamicCommandExceptionType(o -> new LiteralMessage(o.toString()));

    @Override
    public Pattern parse(StringReader reader) throws CommandSyntaxException {
        try {
            String s = reader.getRemaining();
            reader.setCursor(reader.getTotalLength());
            return Pattern.compile(s);
        }catch (PatternSyntaxException e){
            int index = e.getIndex();
            String errorMessage = String.format("Invalid Regex pattern: %s%s: %s <--[HERE]",
                    e.getDescription(),
                    index >= 0 ? String.format(" near index %d ",e.getIndex()) : "",
                    e.getPattern());
            throw REGEX_SYNTAX_EXCEPTION.create(errorMessage);
        }
    }
}
