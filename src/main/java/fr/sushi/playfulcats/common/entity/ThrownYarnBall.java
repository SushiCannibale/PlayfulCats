package fr.sushi.playfulcats.common.entity;

import fr.sushi.playfulcats.common.PlayfulCatRegistries;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class ThrownYarnBall extends AbstractArrow
{
	public ThrownYarnBall(EntityType<ThrownYarnBall> entityType, Level level)
	{
		super(entityType, level);
	}

	public ThrownYarnBall(Level level, double x, double y, double z, ItemStack pickupItemStack)
	{
		super(PlayfulCatRegistries.EntityTypes.THROWN_YARN_BALL_TYPE.get(), x,
			  y, z, level, pickupItemStack, pickupItemStack);
	}

	public ThrownYarnBall(Level level, LivingEntity shooter, ItemStack pickupItemStack)
	{
		super(PlayfulCatRegistries.EntityTypes.THROWN_YARN_BALL_TYPE.get(),
			  shooter, level, pickupItemStack, pickupItemStack);
	}

	@Override
	protected @NotNull ItemStack getDefaultPickupItem()
	{
		return ItemStack.EMPTY;
	}

	@Override
	protected void onHitBlock(BlockHitResult result)
	{
		BlockState blockstate = this.level().getBlockState(result.getBlockPos());
		blockstate.onProjectileHit(this.level(), blockstate, result, this);
		Vec3 movement = this.getDeltaMovement();
		Vec3 normal = result.getDirection().getUnitVec3();
		Vec3 reflected = movement.subtract(normal.scale(2 * movement.dot(normal)));
		this.setDeltaMovement(reflected);
	}
}
