package tehnut.resourceful.crops.core.data;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.List;

public class Seed extends IForgeRegistryEntry.Impl<Seed> {

    private final String name;
    private final int tier;
    private final int craftAmount;
    @Nullable
    private Color color;
    private final transient boolean generateColor;
    private final List<ItemStack> inputItems;
    private final Output[] outputs;
    private final GrowthRequirement growthRequirement;

    @Nullable
    private String oreName;

    public Seed(String name, int tier, int craftAmount, @Nullable Color color, List<ItemStack> inputItems, Output[] outputs, @Nullable GrowthRequirement growthRequirement) {
        this.name = name;
        this.tier = tier;
        this.craftAmount = craftAmount;
        this.color = color;
        this.generateColor = color == null;
        this.inputItems = inputItems;
        this.outputs = outputs;
        this.growthRequirement = growthRequirement == null ? GrowthRequirement.DEFAULT : growthRequirement;
    }

    public Seed(String name, int tier, int craftAmount, @Nullable Color color, List<ItemStack> inputItems, Output output, @Nullable GrowthRequirement growthRequirement) {
        this(name, tier, craftAmount, color, inputItems, new Output[]{output}, growthRequirement);
    }

    public Seed(String name, int tier, int craftAmount, @Nullable Color color, ItemStack inputItem, Output output, @Nullable GrowthRequirement growthRequirement) {
        this(name, tier, craftAmount, color, Lists.newArrayList(inputItem), new Output[]{output}, growthRequirement);
    }

    public Seed(String name, int tier, int craftAmount, @Nullable Color color, String oreName, Output[] output, @Nullable GrowthRequirement growthRequirement) {
        this(name, tier, craftAmount, color, OreDictionary.getOres(oreName), output, growthRequirement);
    }

    public Seed(String name, int tier, int craftAmount, @Nullable Color color, String oreName, Output output, @Nullable GrowthRequirement growthRequirement) {
        this(name, tier, craftAmount, color, oreName, new Output[]{output}, growthRequirement);
    }

    public String getName() {
        return name;
    }

    public int getTier() {
        return tier;
    }

    public int getCraftAmount() {
        return craftAmount;
    }

    @Nullable
    public Color getColor() {
        return color;
    }

    public void setColor(@Nullable Color color) {
        this.color = color;
    }

    public boolean shouldGenerateColor() {
        return generateColor;
    }

    public List<ItemStack> getInputItems() {
        return ImmutableList.copyOf(inputItems);
    }

    public Output[] getOutputs() {
        return outputs;
    }

    public GrowthRequirement getGrowthRequirement() {
        return growthRequirement;
    }

    @Nullable
    public String getOreName() {
        return oreName;
    }

    public void setOreName(@Nullable String oreName) {
        this.oreName = oreName;
    }
}
