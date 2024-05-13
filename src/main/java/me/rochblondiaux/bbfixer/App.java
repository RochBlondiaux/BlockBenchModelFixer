package me.rochblondiaux.bbfixer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import org.tinylog.Logger;

import me.rochblondiaux.bbfixer.patcher.ModelPatcher;
import me.rochblondiaux.bbfixer.utils.Utils;
import me.rochblondiaux.bbfixer.writer.BlockBenchModelWriter;
import me.rochblondiaux.blockbench.BlockBenchModelReader;
import me.rochblondiaux.blockbench.model.BlockBenchModel;
import net.kyori.adventure.key.Key;

public class App {

    private final Path dataFolder;
    private final Path inputFolder;
    private final Path outputFolder;
    private final Map<Path, BlockBenchModel> models = new HashMap<>();

    public App() {
        this.dataFolder = Paths.get("").toAbsolutePath();
        this.inputFolder = dataFolder.resolve("blueprints");
        this.outputFolder = dataFolder.resolve("fixed_blueprints");
    }

    public void start() {
        Logger.info("Booting up...");

        // Data folder
        if (!Files.isDirectory(dataFolder)) {
            Logger.error("Data folder not found: {}", dataFolder);
            return;
        }

        // Shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));

        // Load blueprints
        BlockBenchModelReader reader = BlockBenchModelReader.reader();
        try (Stream<Path> pathStream = Files.walk(this.inputFolder)) {
            pathStream.filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".bbmodel"))
                    .forEach(path -> {
                        BlockBenchModel model = reader.read(path);
                        if (model == null) {
                            Logger.warn("Failed to load blueprint: {}", path);
                            return;
                        }

                        models.put(path, model);
                    });
        } catch (IOException e) {
            Logger.error(e, "Failed to load blueprints");
            return;
        }
        Logger.info("Loaded {} blueprints", models.size());

        // Fix & export blueprints
        BlockBenchModelWriter writer = BlockBenchModelWriter.writer();
        AtomicInteger counter = new AtomicInteger();
        models.forEach((path, blockBenchModel) -> {
            // Fix
            BlockBenchModel patched = new ModelPatcher(blockBenchModel).patch();
            if (patched == null) {
                Logger.warn("Failed to fix blueprint: {}", path);
                return;
            }

            // Fix path
            Path relative = this.inputFolder.relativize(path);
            AtomicReference<Path> newPath = new AtomicReference<>(this.outputFolder);
            relative.iterator().forEachRemaining(path1 -> {
                boolean valid = Key.parseableValue(path1.toString());
                newPath.set(newPath.get().resolve(valid ? path1.toString() : Utils.fix(path1.toString())));
            });

            // Export
            writer.write(newPath.get(), patched);
            counter.incrementAndGet();
        });
        Logger.info("Exported {}/{} blueprints", counter.get(), models.size());
    }

    public void stop() {
        Logger.info("Shutting down...");
    }
}
