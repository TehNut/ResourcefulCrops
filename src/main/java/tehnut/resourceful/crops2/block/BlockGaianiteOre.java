package tehnut.resourceful.crops2.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import tehnut.resourceful.crops2.ResourcefulCrops2;
import tehnut.resourceful.crops2.core.ModObjects;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockGaianiteOre extends Block {

    public static final PropertyBool NETHER = PropertyBool.create("nether");

    public BlockGaianiteOre() {
        super(Material.ROCK);

        setUnlocalizedName(ResourcefulCrops2.MODID + ".ore");
        setCreativeTab(ResourcefulCrops2.TAB_RCROP);
        setSoundType(SoundType.STONE);
        setHardness(4.0F);
        setHarvestLevel("pickaxe", 3);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer.Builder(this).add(NETHER).build();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(NETHER, meta == 1);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(NETHER) ? 1 : 0;
    }

    @Nullable
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return ModObjects.ESSENCE;
    }

    @Override
    public int damageDropped(IBlockState state) {
        return 0;
    }

    @Override
    public int quantityDroppedWithBonus(int fortune, Random random) {
        int drop = random.nextInt(2 * ((fortune + 1) / 2));
        return drop == 0 ? 1 : drop;
    }
}
