package de.pasuki.colorful_redstone.fabric.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.pasuki.colorful_redstone.ColorfulRedstone;
import de.pasuki.colorful_redstone.registry.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ColorfulRedstoneModelProvider implements DataProvider {
    private final PackOutput.PathProvider blockstates;
    private final PackOutput.PathProvider blockModels;
    private final PackOutput.PathProvider itemModels;

    public ColorfulRedstoneModelProvider(FabricDataOutput output) {
        this.blockstates = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "blockstates");
        this.blockModels = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "models/block");
        this.itemModels = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "models/item");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput writer) {
        List<CompletableFuture<?>> futures = new ArrayList<>();

        for (DyeColor color : ModBlocks.supportedColors()) {
            String dustId = ModBlocks.stoneDustId(color);
            ResourceLocation dustLocation = ColorfulRedstone.id(dustId);

            futures.add(DataProvider.saveStable(writer, createWireBlockstate(dustId), blockstates.json(dustLocation)));
            futures.add(DataProvider.saveStable(writer, createDotModel(dustId), blockModels.json(ColorfulRedstone.id(dustId + "_dot"))));
            futures.add(DataProvider.saveStable(writer, createSide0Model(dustId), blockModels.json(ColorfulRedstone.id(dustId + "_side0"))));
            futures.add(DataProvider.saveStable(writer, createSideAlt0Model(dustId), blockModels.json(ColorfulRedstone.id(dustId + "_side_alt0"))));
            futures.add(DataProvider.saveStable(writer, createSide1Model(dustId), blockModels.json(ColorfulRedstone.id(dustId + "_side1"))));
            futures.add(DataProvider.saveStable(writer, createSideAlt1Model(dustId), blockModels.json(ColorfulRedstone.id(dustId + "_side_alt1"))));
            futures.add(DataProvider.saveStable(writer, createUpModel(dustId), blockModels.json(ColorfulRedstone.id(dustId + "_up"))));
            futures.add(DataProvider.saveStable(writer, createFlatItemModel(dustId), itemModels.json(dustLocation)));

            String blockId = ModBlocks.stoneBlockId(color);
            ResourceLocation blockLocation = ColorfulRedstone.id(blockId);
            futures.add(DataProvider.saveStable(writer, createSimpleBlockstate(blockId), blockstates.json(blockLocation)));
            futures.add(DataProvider.saveStable(writer, createCubeAllModel(blockId), blockModels.json(blockLocation)));
            futures.add(DataProvider.saveStable(writer, createBlockItemModel(blockId), itemModels.json(blockLocation)));

            String torchId = ModBlocks.stoneTorchId(color);
            String wallTorchId = ModBlocks.stoneWallTorchId(color);
            ResourceLocation torchLoc = ColorfulRedstone.id(torchId);
            ResourceLocation wallTorchLoc = ColorfulRedstone.id(wallTorchId);

            futures.add(DataProvider.saveStable(writer, createTorchBlockstate(torchId), blockstates.json(torchLoc)));
            futures.add(DataProvider.saveStable(writer, createWallTorchBlockstate(wallTorchId), blockstates.json(wallTorchLoc)));

            futures.add(DataProvider.saveStable(writer, createTorchModel(torchId), blockModels.json(torchLoc)));
            futures.add(DataProvider.saveStable(writer, createTorchOffModel(torchId), blockModels.json(ColorfulRedstone.id(torchId + "_off"))));
            futures.add(DataProvider.saveStable(writer, createWallTorchModel(wallTorchId, torchId), blockModels.json(wallTorchLoc)));
            futures.add(DataProvider.saveStable(writer, createWallTorchOffModel(wallTorchId, torchId), blockModels.json(ColorfulRedstone.id(wallTorchId + "_off"))));
            futures.add(DataProvider.saveStable(writer, createTorchItemModel(torchId), itemModels.json(torchLoc)));

            String repeaterId = ModBlocks.stoneRepeaterId(color);
            ResourceLocation repeaterLoc = ColorfulRedstone.id(repeaterId);
            futures.add(DataProvider.saveStable(writer, createRepeaterBlockstate(repeaterId), blockstates.json(repeaterLoc)));
            futures.add(DataProvider.saveStable(writer, createRepeaterModel(color, 1, false, false), blockModels.json(repeaterLoc)));
            for (int delay = 1; delay <= 4; delay++) {
                for (boolean locked : new boolean[]{false, true}) {
                    for (boolean powered : new boolean[]{false, true}) {
                        String modelId = repeaterModelId(repeaterId, delay, powered, locked);
                        futures.add(DataProvider.saveStable(
                                writer,
                                createRepeaterModel(color, delay, powered, locked),
                                blockModels.json(ColorfulRedstone.id(modelId))
                        ));
                    }
                }
            }
            futures.add(DataProvider.saveStable(writer, createRepeaterItemModel(color), itemModels.json(repeaterLoc)));

            String comparatorId = ModBlocks.stoneComparatorId(color);
            ResourceLocation comparatorLoc = ColorfulRedstone.id(comparatorId);
            futures.add(DataProvider.saveStable(writer, createComparatorBlockstate(comparatorId), blockstates.json(comparatorLoc)));
            futures.add(DataProvider.saveStable(writer, createComparatorModel(color, comparatorId, false, false), blockModels.json(comparatorLoc)));
            futures.add(DataProvider.saveStable(writer, createComparatorModel(color, comparatorId, true, false), blockModels.json(ColorfulRedstone.id(comparatorId + "_on"))));
            futures.add(DataProvider.saveStable(writer, createComparatorModel(color, comparatorId, false, true), blockModels.json(ColorfulRedstone.id(comparatorId + "_subtract"))));
            futures.add(DataProvider.saveStable(writer, createComparatorModel(color, comparatorId, true, true), blockModels.json(ColorfulRedstone.id(comparatorId + "_on_subtract"))));
            futures.add(DataProvider.saveStable(writer, createComparatorItemModel(color), itemModels.json(comparatorLoc)));
        }

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }

    @Override
    public String getName() {
        return "Colorful Redstone Models";
    }

    private static JsonObject createSimpleBlockstate(String id) {
        JsonObject root = new JsonObject();
        JsonObject variants = new JsonObject();
        JsonObject defaultVariant = new JsonObject();
        defaultVariant.addProperty("model", ColorfulRedstone.MOD_ID + ":block/" + id);
        variants.add("", defaultVariant);
        root.add("variants", variants);
        return root;
    }

    private static JsonObject createCubeAllModel(String id) {
        JsonObject root = new JsonObject();
        root.addProperty("parent", "minecraft:block/cube_all");
        JsonObject textures = new JsonObject();
        textures.addProperty("all", ColorfulRedstone.MOD_ID + ":block/" + id);
        root.add("textures", textures);
        return root;
    }

    private static JsonObject createBlockItemModel(String id) {
        JsonObject root = new JsonObject();
        root.addProperty("parent", ColorfulRedstone.MOD_ID + ":block/" + id);
        return root;
    }

    private static JsonObject createTorchBlockstate(String torchId) {
        JsonObject root = new JsonObject();
        JsonObject variants = new JsonObject();

        JsonObject off = new JsonObject();
        off.addProperty("model", ColorfulRedstone.MOD_ID + ":block/" + torchId + "_off");
        variants.add("lit=false", off);

        JsonObject on = new JsonObject();
        on.addProperty("model", ColorfulRedstone.MOD_ID + ":block/" + torchId);
        variants.add("lit=true", on);

        root.add("variants", variants);
        return root;
    }

    private static JsonObject createWallTorchBlockstate(String wallTorchId) {
        JsonObject root = new JsonObject();
        JsonObject variants = new JsonObject();

        variants.add("facing=east,lit=false", variant(ColorfulRedstone.MOD_ID + ":block/" + wallTorchId + "_off", null));
        variants.add("facing=east,lit=true", variant(ColorfulRedstone.MOD_ID + ":block/" + wallTorchId, null));
        variants.add("facing=north,lit=false", variant(ColorfulRedstone.MOD_ID + ":block/" + wallTorchId + "_off", 270));
        variants.add("facing=north,lit=true", variant(ColorfulRedstone.MOD_ID + ":block/" + wallTorchId, 270));
        variants.add("facing=south,lit=false", variant(ColorfulRedstone.MOD_ID + ":block/" + wallTorchId + "_off", 90));
        variants.add("facing=south,lit=true", variant(ColorfulRedstone.MOD_ID + ":block/" + wallTorchId, 90));
        variants.add("facing=west,lit=false", variant(ColorfulRedstone.MOD_ID + ":block/" + wallTorchId + "_off", 180));
        variants.add("facing=west,lit=true", variant(ColorfulRedstone.MOD_ID + ":block/" + wallTorchId, 180));

        root.add("variants", variants);
        return root;
    }

    private static JsonObject createRepeaterBlockstate(String repeaterId) {
        JsonObject root = new JsonObject();
        JsonObject variants = new JsonObject();

        String[] facings = {"north", "east", "south", "west"};
        for (String facing : facings) {
            Integer y = rotationForFacing(facing);
            for (int delay = 1; delay <= 4; delay++) {
                for (boolean locked : new boolean[]{false, true}) {
                    for (boolean powered : new boolean[]{false, true}) {
                        String key = "delay=" + delay + ",facing=" + facing + ",locked=" + locked + ",powered=" + powered;
                        String model = ColorfulRedstone.MOD_ID + ":block/" + repeaterModelId(repeaterId, delay, powered, locked);
                        variants.add(key, variant(model, y));
                    }
                }
            }
        }

        root.add("variants", variants);
        return root;
    }

    private static JsonObject createComparatorBlockstate(String comparatorId) {
        JsonObject root = new JsonObject();
        JsonObject variants = new JsonObject();

        String[] facings = {"north", "east", "south", "west"};
        String[] modes = {"compare", "subtract"};
        for (String facing : facings) {
            Integer y = rotationForFacing(facing);
            for (String mode : modes) {
                for (boolean powered : new boolean[]{false, true}) {
                    String key = "facing=" + facing + ",mode=" + mode + ",powered=" + powered;
                    String model = ColorfulRedstone.MOD_ID + ":block/" + comparatorId;
                    if (powered) {
                        model += "_on";
                    }
                    if ("subtract".equals(mode)) {
                        model += "_subtract";
                    }
                    variants.add(key, variant(model, y));
                }
            }
        }

        root.add("variants", variants);
        return root;
    }

    private static JsonObject createRepeaterModel(DyeColor color, int delay, boolean on, boolean locked) {
        JsonObject root = new JsonObject();
        String parent = "minecraft:block/repeater_" + delay + "tick" + (on ? "_on" : "") + (locked ? "_locked" : "");
        root.addProperty("parent", parent);

        if (color == DyeColor.RED) {
            return root;
        }

        String stoneName = stoneName(color);
        JsonObject textures = new JsonObject();
        textures.addProperty("particle", ColorfulRedstone.MOD_ID + ":block/" + stoneName + "_repeater");
        textures.addProperty("top", ColorfulRedstone.MOD_ID + ":block/" + stoneName + "_repeater_top_" + (on ? "on" : "off"));
        textures.addProperty("lit", ColorfulRedstone.MOD_ID + ":block/" + stoneName + "_torch");
        textures.addProperty("unlit", ColorfulRedstone.MOD_ID + ":block/" + stoneName + "_torch_off");
        root.add("textures", textures);
        return root;
    }

    private static JsonObject createComparatorModel(DyeColor color, String comparatorId, boolean on, boolean subtract) {
        JsonObject root = new JsonObject();
        String parent = "minecraft:block/comparator" + (on ? "_on" : "") + (subtract ? "_subtract" : "");
        root.addProperty("parent", parent);

        if (color == DyeColor.RED) {
            return root;
        }

        String stoneName = stoneName(color);
        JsonObject textures = new JsonObject();
        textures.addProperty("particle", ColorfulRedstone.MOD_ID + ":block/" + stoneName + "_comperator");
        textures.addProperty("top", ColorfulRedstone.MOD_ID + ":block/" + stoneName + "_comperator_top_" + (on ? "on" : "off"));
        textures.addProperty("lit", ColorfulRedstone.MOD_ID + ":block/" + stoneName + "_torch");
        textures.addProperty("unlit", ColorfulRedstone.MOD_ID + ":block/" + stoneName + "_torch_off");
        root.add("textures", textures);
        return root;
    }

    private static String stoneName(DyeColor color) {
        String dustId = ModBlocks.stoneDustId(color);
        return dustId.substring(0, dustId.length() - "_dust".length());
    }

    private static String repeaterModelId(String repeaterId, int delay, boolean powered, boolean locked) {
        return repeaterId + "_" + delay + "tick" + (powered ? "_on" : "") + (locked ? "_locked" : "");
    }

    private static Integer rotationForFacing(String facing) {
        return switch (facing) {
            case "east" -> 270;
            case "north" -> 180;
            case "west" -> 90;
            default -> null;
        };
    }
    private static JsonObject createTorchModel(String torchId) {
        JsonObject root = new JsonObject();
        root.addProperty("parent", "minecraft:block/template_torch");
        JsonObject textures = new JsonObject();
        textures.addProperty("torch", ColorfulRedstone.MOD_ID + ":block/" + torchId);
        root.add("textures", textures);
        return root;
    }

    private static JsonObject createTorchOffModel(String torchId) {
        JsonObject root = new JsonObject();
        root.addProperty("parent", "minecraft:block/template_torch");
        JsonObject textures = new JsonObject();
        textures.addProperty("torch", ColorfulRedstone.MOD_ID + ":block/" + torchId + "_off");
        root.add("textures", textures);
        return root;
    }

    private static JsonObject createWallTorchModel(String wallTorchId, String torchId) {
        JsonObject root = new JsonObject();
        root.addProperty("parent", "minecraft:block/template_torch_wall");
        JsonObject textures = new JsonObject();
        textures.addProperty("torch", ColorfulRedstone.MOD_ID + ":block/" + torchId);
        root.add("textures", textures);
        return root;
    }

    private static JsonObject createWallTorchOffModel(String wallTorchId, String torchId) {
        JsonObject root = new JsonObject();
        root.addProperty("parent", "minecraft:block/template_torch_wall");
        JsonObject textures = new JsonObject();
        textures.addProperty("torch", ColorfulRedstone.MOD_ID + ":block/" + torchId + "_off");
        root.add("textures", textures);
        return root;
    }

    private static JsonObject createTorchItemModel(String torchId) {
        JsonObject root = new JsonObject();
        root.addProperty("parent", "minecraft:item/generated");
        JsonObject textures = new JsonObject();
        textures.addProperty("layer0", ColorfulRedstone.MOD_ID + ":block/" + torchId);
        root.add("textures", textures);
        return root;
    }

    private static JsonObject createRepeaterItemModel(DyeColor color) {
        if (color == DyeColor.RED) {
            JsonObject root = new JsonObject();
            root.addProperty("parent", "minecraft:item/repeater");
            return root;
        }

        String stoneName = stoneName(color);
        JsonObject root = new JsonObject();
        root.addProperty("parent", "minecraft:item/generated");
        JsonObject textures = new JsonObject();
        textures.addProperty("layer0", ColorfulRedstone.MOD_ID + ":block/" + stoneName + "_repeater");
        root.add("textures", textures);
        return root;
    }

    private static JsonObject createComparatorItemModel(DyeColor color) {
        if (color == DyeColor.RED) {
            JsonObject root = new JsonObject();
            root.addProperty("parent", "minecraft:item/comparator");
            return root;
        }

        String stoneName = stoneName(color);
        JsonObject root = new JsonObject();
        root.addProperty("parent", "minecraft:item/generated");
        JsonObject textures = new JsonObject();
        textures.addProperty("layer0", ColorfulRedstone.MOD_ID + ":block/" + stoneName + "_comperator");
        root.add("textures", textures);
        return root;
    }

    private static JsonObject variant(String model, Integer y) {
        JsonObject obj = new JsonObject();
        obj.addProperty("model", model);
        if (y != null) {
            obj.addProperty("y", y);
        }
        return obj;
    }

    private static JsonObject createWireBlockstate(String id) {
        JsonObject root = new JsonObject();
        JsonArray multipart = new JsonArray();

        JsonObject dotPart = new JsonObject();
        JsonObject dotApply = new JsonObject();
        dotApply.addProperty("model", ColorfulRedstone.MOD_ID + ":block/" + id + "_dot");
        dotPart.add("apply", dotApply);

        JsonObject dotWhen = new JsonObject();
        JsonArray orArray = new JsonArray();
        orArray.add(connectionObject("none", "none", "none", "none"));
        orArray.add(connectionObject("side|up", "side|up", null, null));
        orArray.add(connectionObject("side|up", null, "side|up", null));
        orArray.add(connectionObject(null, null, "side|up", "side|up"));
        orArray.add(connectionObject(null, "side|up", null, "side|up"));
        dotWhen.add("OR", orArray);
        dotPart.add("when", dotWhen);
        multipart.add(dotPart);

        multipart.add(partWithWhen(ColorfulRedstone.MOD_ID + ":block/" + id + "_side0", "north", "side|up", 0));
        multipart.add(partWithWhen(ColorfulRedstone.MOD_ID + ":block/" + id + "_side_alt0", "south", "side|up", 0));
        multipart.add(partWithWhen(ColorfulRedstone.MOD_ID + ":block/" + id + "_side_alt1", "east", "side|up", 270));
        multipart.add(partWithWhen(ColorfulRedstone.MOD_ID + ":block/" + id + "_side1", "west", "side|up", 270));
        multipart.add(partWithWhen(ColorfulRedstone.MOD_ID + ":block/" + id + "_up", "north", "up", 0));
        multipart.add(partWithWhen(ColorfulRedstone.MOD_ID + ":block/" + id + "_up", "east", "up", 90));
        multipart.add(partWithWhen(ColorfulRedstone.MOD_ID + ":block/" + id + "_up", "south", "up", 180));
        multipart.add(partWithWhen(ColorfulRedstone.MOD_ID + ":block/" + id + "_up", "west", "up", 270));

        root.add("multipart", multipart);
        return root;
    }

    private static JsonObject createDotModel(String id) {
        JsonObject root = new JsonObject();
        root.addProperty("parent", "minecraft:block/redstone_dust_dot");
        JsonObject textures = new JsonObject();
        textures.addProperty("particle", ColorfulRedstone.MOD_ID + ":block/" + id + "_dot");
        textures.addProperty("line", ColorfulRedstone.MOD_ID + ":block/" + id + "_dot");
        textures.addProperty("overlay", ColorfulRedstone.MOD_ID + ":block/" + id + "_overlay");
        root.add("textures", textures);
        return root;
    }

    private static JsonObject createSide0Model(String id) {
        JsonObject root = new JsonObject();
        root.addProperty("parent", "minecraft:block/redstone_dust_side0");
        JsonObject textures = new JsonObject();
        textures.addProperty("line", ColorfulRedstone.MOD_ID + ":block/" + id + "_line0");
        root.add("textures", textures);
        return root;
    }

    private static JsonObject createSideAlt0Model(String id) {
        JsonObject root = new JsonObject();
        root.addProperty("parent", "minecraft:block/redstone_dust_side_alt0");
        JsonObject textures = new JsonObject();
        textures.addProperty("line", ColorfulRedstone.MOD_ID + ":block/" + id + "_line0");
        root.add("textures", textures);
        return root;
    }

    private static JsonObject createSide1Model(String id) {
        JsonObject root = new JsonObject();
        root.addProperty("parent", "minecraft:block/redstone_dust_side1");
        JsonObject textures = new JsonObject();
        textures.addProperty("line", ColorfulRedstone.MOD_ID + ":block/" + id + "_line1");
        root.add("textures", textures);
        return root;
    }

    private static JsonObject createSideAlt1Model(String id) {
        JsonObject root = new JsonObject();
        root.addProperty("parent", "minecraft:block/redstone_dust_side_alt1");
        JsonObject textures = new JsonObject();
        textures.addProperty("line", ColorfulRedstone.MOD_ID + ":block/" + id + "_line1");
        root.add("textures", textures);
        return root;
    }

    private static JsonObject createUpModel(String id) {
        JsonObject root = new JsonObject();
        root.addProperty("parent", "minecraft:block/redstone_dust_up");
        JsonObject textures = new JsonObject();
        textures.addProperty("particle", ColorfulRedstone.MOD_ID + ":block/" + id + "_dot");
        textures.addProperty("line", ColorfulRedstone.MOD_ID + ":block/" + id + "_line0");
        textures.addProperty("overlay", ColorfulRedstone.MOD_ID + ":block/" + id + "_overlay");
        root.add("textures", textures);
        return root;
    }

    private static JsonObject createFlatItemModel(String id) {
        JsonObject itemModel = new JsonObject();
        itemModel.addProperty("parent", "minecraft:item/generated");
        JsonObject textures = new JsonObject();
        textures.addProperty("layer0", ColorfulRedstone.MOD_ID + ":item/" + id);
        itemModel.add("textures", textures);
        return itemModel;
    }

    private static JsonObject connectionObject(String east, String north, String south, String west) {
        JsonObject obj = new JsonObject();
        if (east != null) obj.addProperty("east", east);
        if (north != null) obj.addProperty("north", north);
        if (south != null) obj.addProperty("south", south);
        if (west != null) obj.addProperty("west", west);
        return obj;
    }

    private static JsonObject partWithWhen(String model, String sideKey, String sideValue, int yRotation) {
        JsonObject part = new JsonObject();
        JsonObject apply = new JsonObject();
        apply.addProperty("model", model);
        if (yRotation != 0) {
            apply.addProperty("y", yRotation);
        }
        part.add("apply", apply);

        JsonObject when = new JsonObject();
        when.addProperty(sideKey, sideValue);
        part.add("when", when);
        return part;
    }
}














