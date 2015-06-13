package tehnut.resourceful.crops.registry;

import com.google.gson.GsonBuilder;
import net.minecraft.item.ItemStack;
import tehnut.resourceful.crops.ResourcefulCrops;
import tehnut.resourceful.crops.base.Seed;

import java.util.ArrayList;
import java.util.List;

public class SeedRegistry {

    public static GsonBuilder seedBuilder;

    public static void registerSeed(Seed seed) {
        ResourcefulCrops.getSeedCache().addObject(seed, seed.getName());
    }

    public static Seed getSeed(int index) {
        return ResourcefulCrops.getSeedCache().getObject(index);
    }

    public static Seed getSeed(String name) {
        return ResourcefulCrops.getSeedCache().getObject(name);
    }

    public static int getIndexOf(Seed seed) {
        return ResourcefulCrops.getSeedCache().getID(seed);
    }

    public static String getNameOf(Seed seed) {
        return ResourcefulCrops.getSeedCache().getName(seed);
    }

    public static int getSize() {
        return ResourcefulCrops.getSeedCache().getEnumeratedObjects().size();
    }

    public static boolean isEmpty() {
        return ResourcefulCrops.getSeedCache().getEnumeratedObjects().isEmpty();
    }

    public static List<Seed> getSeedList() {
        return new ArrayList<Seed>(ResourcefulCrops.getSeedCache().getEnumeratedObjects().valueCollection());
    }

    public static ItemStack getItemStackForSeed(Seed seed) {
        return new ItemStack(ItemRegistry.seed, 1, getIndexOf(seed));
    }
}
