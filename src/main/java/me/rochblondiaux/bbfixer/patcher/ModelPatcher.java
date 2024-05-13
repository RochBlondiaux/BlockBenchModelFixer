package me.rochblondiaux.bbfixer.patcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.Nullable;

import lombok.RequiredArgsConstructor;
import me.rochblondiaux.bbfixer.utils.Utils;
import me.rochblondiaux.blockbench.element.BlockBenchElement;
import me.rochblondiaux.blockbench.element.BlockBenchElementImpl;
import me.rochblondiaux.blockbench.model.BlockBenchModel;
import me.rochblondiaux.blockbench.model.BlockBenchModelImpl;
import me.rochblondiaux.blockbench.model.metadata.MetadataImpl;
import me.rochblondiaux.blockbench.texture.BlockBenchTextureImpl;
import me.rochblondiaux.blockbench.texture.BlockbenchTexture;
import team.unnamed.creative.model.ItemTransformImpl;

@RequiredArgsConstructor
public class ModelPatcher {

    private final BlockBenchModel model;

    public BlockBenchModel patch() {
        // Elements
        List<? extends BlockBenchElement> elements = model.elements();
        List<BlockBenchElementImpl> fixedElements = new ArrayList<>();
        for (BlockBenchElement element : elements) {
            fixedElements.add(new BlockBenchElementImpl(
                    element.uniqueId(),
                    element.type(),
                    fix(element.name()),
                    element.boxUv(),
                    element.rescale(),
                    element.locked(),
                    element.renderOrder(),
                    element.allowMirrorModeling(),
                    element.autoUv(),
                    element.color(),
                    element.from(),
                    element.to(),
                    element.origin(),
                    element.rotation(),
                    element.faces()
            ));
        }

        // Textures
        List<? extends BlockbenchTexture> textures = model.textures();
        List<BlockBenchTextureImpl> fixedTextures = new ArrayList<>();
        for (BlockbenchTexture texture : textures) {
            fixedTextures.add(new BlockBenchTextureImpl(
                    texture.uniqueId(),
                    texture.path(),
                    fix(texture.name()),
                    fix(texture.folder()),
                    fix(texture.namespace()),
                    texture.id(),
                    texture.width(),
                    texture.height(),
                    texture.uvWidth(),
                    texture.uvHeight(),
                    texture.particle(),
                    texture.layersEnabled(),
                    texture.renderMode(),
                    texture.renderSides(),
                    texture.frameTime(),
                    texture.frameOrderType(),
                    texture.frameInterpolate(),
                    texture.visible(),
                    texture.internal(),
                    texture.saved(),
                    texture.relativePath(),
                    texture.source()
            ));
        }

        return new BlockBenchModelImpl(
                fix(model.name()),
                fix(model.identifier()),
                fix(model.parent()),
                model.credit(),
                fix(model.modelIdentifier()),
                (MetadataImpl) model.metadata(),
                model.ambientOcclusion(),
                model.frontGuiLight(),
                model.visibleBox(),
                model.variablePlaceholders(),
                model.variablePlaceholderButtons(),
                model.resolution(),
                fixedElements,
                fixedTextures,
                (Map<String, ItemTransformImpl>) model.display()
        );
    }

    private @Nullable String fix(@Nullable String input) {
        return input == null ? null : Utils.fix(input);
    }

}
