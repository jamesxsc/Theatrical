/*
 * Copyright 2018 Theatrical Team (James Conway (615283) & Stuart (Rushmead)) and it's contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.georlegacy.general.theatrical.blocks.fixtures;

import com.georlegacy.general.theatrical.TheatricalMain;
import com.georlegacy.general.theatrical.api.capabilities.power.ITheatricalPowerStorage;
import com.georlegacy.general.theatrical.api.capabilities.power.TheatricalPower;
import com.georlegacy.general.theatrical.blocks.fixtures.base.BlockHangable;
import com.georlegacy.general.theatrical.blocks.fixtures.base.IHasTileEntity;
import com.georlegacy.general.theatrical.guis.handlers.enumeration.GUIID;
import com.georlegacy.general.theatrical.integration.top.ITOPProvider;
import com.georlegacy.general.theatrical.tabs.base.CreativeTabs;
import com.georlegacy.general.theatrical.tiles.fixtures.TileFresnel;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockFresnel extends BlockHangable implements ITileEntityProvider, IHasTileEntity, ITOPProvider {


    public BlockFresnel() {
        super("fresnel", new EnumFacing[]{EnumFacing.DOWN});
        this.setCreativeTab(CreativeTabs.FIXTURES_TAB);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(@Nonnull World worldIn, int meta) {
        return new TileFresnel();
    }

    @Override
    public Class<? extends TileEntity> getTileEntity() {
        return TileFresnel.class;
    }

    private TileFresnel getTE(World world, BlockPos pos) {
        return (TileFresnel) world.getTileEntity(pos);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state,
        EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY,
        float hitZ) {
        if (!worldIn.isRemote) {
            if (!playerIn.isSneaking()) {
                playerIn.openGui(TheatricalMain.instance, GUIID.FIXTURE_FRESNEL.getNid(), worldIn,
                    pos.getX(), pos.getY(), pos.getZ());
            }
        }
        return super
            .onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public boolean isBlockNormalCube(IBlockState blockState) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState blockState) {
        return false;
    }


    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess worldIn, BlockPos pos,
        EnumFacing side) {
        return false;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileFresnel tileFresnel = (TileFresnel) worldIn.getTileEntity(pos);
        if (tileFresnel != null && tileFresnel.getLightBlock() != null) {
            worldIn.setBlockToAir(tileFresnel.getLightBlock());
        }
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        if (world.getTileEntity(pos) instanceof TileFresnel) {
            TileFresnel tileFresnel = (TileFresnel) world.getTileEntity(pos);
            if (tileFresnel != null) {
                return (int) (tileFresnel.getIntensity() * 4 / 255);
            }
        }
        return super.getLightValue(state, world, pos);
    }


    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        TileEntity tileEntity = world.getTileEntity(data.getPos());

        if (tileEntity instanceof TileFresnel) {
            TileFresnel pipe = (TileFresnel) tileEntity;
            ITheatricalPowerStorage theatricalPower = pipe.getCapability(TheatricalPower.CAP, null);
            probeInfo.text("Power: " + theatricalPower.getEnergyStored());
        }
    }
}
