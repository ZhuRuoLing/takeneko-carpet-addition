package icu.takeneko.tnca.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import static icu.takeneko.tnca.command.arguments.RegexArgumentType.REGEX_SYNTAX_EXCEPTION;

public class RegexSuggestionProvider <S> implements SuggestionProvider<S> {
    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<S> context, SuggestionsBuilder builder) throws CommandSyntaxException {
        var remaining = builder.getRemaining();
        try {
            Pattern.compile(remaining);
        } catch (PatternSyntaxException e) {
            int index = e.getIndex();
            String errorMessage = String.format("Invalid Regex pattern: %s%s: %s <--[HERE]",
                    e.getDescription(),
                    index >= 0 ? String.format(" near index %d ",e.getIndex()) : "",
                    e.getPattern());
            throw REGEX_SYNTAX_EXCEPTION.create(errorMessage);
        }
        return builder.suggest(remaining).buildFuture();
    }
}
