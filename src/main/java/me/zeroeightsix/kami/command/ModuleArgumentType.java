package me.zeroeightsix.kami.command;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import me.zeroeightsix.kami.module.ModulePlay;
import me.zeroeightsix.kami.module.ModuleManager;
import net.minecraft.server.command.CommandSource;
import net.minecraft.text.LiteralText;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class ModuleArgumentType implements ArgumentType<ModulePlay> {

    private static final Collection<String> EXAMPLES = Arrays.asList("Aura", "CameraClip", "Flight");
    public static final DynamicCommandExceptionType INVALID_MODULE_EXCEPTION = new DynamicCommandExceptionType((object) -> {
        return new LiteralText("Unknown module '" + object + "'");
    });

    public static ModuleArgumentType module() {
        return new ModuleArgumentType();
    }

    @Override
    public ModulePlay parse(StringReader reader) throws CommandSyntaxException {
        String string = reader.readUnquotedString();
        ModulePlay module = ModuleManager.getModuleByName(string);
        if (module == null) {
            throw INVALID_MODULE_EXCEPTION.create(string);
        }
        return module;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return CommandSource.suggestMatching(ModuleManager.getModules().stream().map(m -> m.getName().getValue()), builder);
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }

}
