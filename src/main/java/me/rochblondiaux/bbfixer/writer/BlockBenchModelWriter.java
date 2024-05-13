package me.rochblondiaux.bbfixer.writer;

import java.io.OutputStream;
import java.nio.file.Path;

import org.jetbrains.annotations.NotNull;

import me.rochblondiaux.blockbench.model.BlockBenchModel;

public interface BlockBenchModelWriter {

    static @NotNull BlockBenchModelWriter writer() {
        return new BlockBenchModelWriterImpl();
    }

    void writer(@NotNull OutputStream outputStream, @NotNull BlockBenchModel model);

    void write(@NotNull Path path, @NotNull BlockBenchModel model);

}
