package tehnut.resourceful.crops.block;

import net.minecraft.block.BlockCrops;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import tehnut.resourceful.crops.block.prop.PropertySeedType;
import tehnut.resourceful.crops.block.tile.TileSeedContainer;
import tehnut.resourceful.crops.core.RegistrarResourcefulCrops;
import tehnut.resourceful.crops.core.data.Seed;
import tehnut.resourceful.crops.item.ItemResourceful;
import tehnut.resourceful.crops.util.Util;

import javax.annotation.Nullable;
import java.util.*;

public class BlockResourcefulCrop extends BlockCrops {

    public static final IProperty<String> SEED_TYPE = new PropertySeedType();

    private BlockStateContainer stateContainer;

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer.Builder(this).add(AGE).build(); // Dummy state to avoid issues when instantiating
    }

    private BlockStateContainer createRealState() {
        return new BlockStateContainer.Builder(this).add(SEED_TYPE, AGE).build();
    }

    @Override
    public BlockStateContainer getBlockState() {
        return stateContainer;
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        Seed seed = RegistrarResourcefulCrops.SEEDS.getValue(Util.getTile(TileSeedContainer.class, world, pos).getSeedKey());
        return state.withProperty(SEED_TYPE, seed.getRegistryName().toString().replace(":", "_"));
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        if (canGrow(world, pos, state))
            super.updateTick(world, pos, state, rand);
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        drops.add(getResourcefulStack(getSeed(), world, pos));
        if (getAge(state) >= 7)
            drops.add(getResourcefulStack(getCrop(), world, pos));
    }

    @Override
    public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity tile, ItemStack stack) {
        super.harvestBlock(world, player, pos, state, tile, stack);
        world.setBlockToAir(pos);
    }

    @Override
    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
        return willHarvest || super.removedByPlayer(state, world, pos, player, false);
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return getItem(world, pos, state);
    }

    @Override
    public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
        return getResourcefulStack(getSeed(), world, pos);
    }

    @Override
    protected Item getSeed() {
        return RegistrarResourcefulCrops.SEED;
    }

    @Override
    protected Item getCrop() {
        return RegistrarResourcefulCrops.SHARD;
    }

    @Override
    public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
        return EnumPlantType.Crop;
    }

    @Override
    public boolean canUseBonemeal(World world, Random rand, BlockPos pos, IBlockState state) {
        TileSeedContainer seedContainer = Util.getTile(TileSeedContainer.class, world, pos);
        if (seedContainer != null) {
            Seed seed = RegistrarResourcefulCrops.SEEDS.getValue(seedContainer.getSeedKey());
            if (!seed.isNull())
                return seed.canFertilize();
        }
        return false;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileSeedContainer();
    }

    public boolean canGrow(World world, BlockPos pos, IBlockState state) {
        if (isMaxAge(state))
            return false;

        TileSeedContainer seedContainer = Util.getTile(TileSeedContainer.class, world, pos);
        if (seedContainer == null)
            return false;

        Seed seed = RegistrarResourcefulCrops.SEEDS.getValue(seedContainer.getSeedKey());
        if (seed == null)
            return false;

        if (world.getLightFromNeighbors(pos.up()) < seed.getGrowthRequirement().getMinLight() && world.getLightFromNeighbors(pos.up()) > seed.getGrowthRequirement().getMaxLight())
            return false;

        if (seed.getGrowthRequirement().getRequiredState() != null && !world.getBlockState(pos.down(2)).equals(seed.getGrowthRequirement().getRequiredState()))
            return false;

        return true;
    }

    public BlockResourcefulCrop init() {
        this.stateContainer = createRealState();
        this.setDefaultState(stateContainer.getBaseState());
        return this;
    }

    private ItemStack getResourcefulStack(Item toDrop, IBlockAccess world, BlockPos pos) {
        TileSeedContainer cropTile = Util.getTile(TileSeedContainer.class, world, pos);
        if (cropTile != null)
            return ItemResourceful.getResourcefulStack(toDrop, cropTile.getSeedKey());

        return new ItemStack(toDrop, 1, Short.MAX_VALUE - 1);
    }
}
