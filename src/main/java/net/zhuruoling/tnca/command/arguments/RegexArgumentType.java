package net.zhuruoling.tnca.command.arguments;

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
            var s = reader.getRemaining();
            reader.setCursor(reader.getTotalLength());
            return Pattern.compile(s);
        }catch (PatternSyntaxException e){
            StringBuilder sb = new StringBuilder("Invalid Regex pattern: ");
            int index = e.getIndex();
            String pattern = e.getPattern();
            sb.append(e.getDescription());
            if (index >= 0) {
                sb.append(" near index ");
                sb.append(index);
            }
            sb.append(": ");
            sb.append(pattern);
            sb.append(" <--[HERE]");
            throw REGEX_SYNTAX_EXCEPTION.create(sb.toString());
        }
    }
}
