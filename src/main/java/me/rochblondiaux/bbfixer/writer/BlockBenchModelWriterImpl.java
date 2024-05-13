package me.rochblondiaux.bbfixer.writer;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.jetbrains.annotations.NotNull;

import com.google.gson.Gson;

import me.rochblondiaux.blockbench.model.BlockBenchModel;

public class BlockBenchModelWriterImpl implements BlockBenchModelWriter {

    private static final Gson GSON = new Gson();

    @Override
    public void writer(@NotNull OutputStream outputStream, @NotNull BlockBenchModel model) {
        try {
            outputStream.write(GSON.toJson(model).getBytes());
        } catch (Exception e) {
            throw new RuntimeException("Failed to write BlockBench model to output stream", e);
        }
    }

    @Override
    public void write(@NotNull Path path, @NotNull BlockBenchModel model) {
        // Create parent directories and file
        try {
            Files.createDirectories(path.getParent());
            Files.createFile(path);
        } catch (Exception ignored) {
        }

        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path)) {
            bufferedWriter.write(GSON.toJson(model));
        } catch (Exception e) {
            throw new RuntimeException("Failed to write BlockBench model to path: " + path, e);
        }
    }
}
