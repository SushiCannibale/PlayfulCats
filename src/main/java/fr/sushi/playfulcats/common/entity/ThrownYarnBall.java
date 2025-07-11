package fr.sushi.playfulcats.common.entity;

import fr.sushi.playfulcats.common.PlayfulCatRegistries;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class ThrownYarnBall extends ThrowableProjectile
{
	public ThrownYarnBall(EntityType<ThrownYarnBall> entityType, Level level)
	{
		super(entityType, level);
	}

	public ThrownYarnBall(Level level, double x, double y, double z)
	{
		super(PlayfulCatRegistries.EntityTypes.THROWN_YARN_BALL_TYPE.get(), x,
			  y, z, level);
	}

	public ThrownYarnBall(Level level, LivingEntity shooter, ItemStack pickupItemStack)
	{
		this(level, shooter.getX(), shooter.getEyeY() - 0.1f, shooter.getZ());
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder)
	{
	}

	@Override
	public void tick()
	{
		if (!this.onGround())
		{
			super.tick();
		}
	}

	@Override
	protected void onHitBlock(BlockHitResult result)
	{
		super.onHitBlock(result);
		Vec3 motion = this.getDeltaMovement();
		if (motion.lengthSqr() < this.getGroundMotionThreshold())
		{
			this.setOnGroundWithMovement(true, motion);
		}
		else
		{
			Vec3 normal = result.getDirection().getUnitVec3();
			Vec3 reflected =
					motion.subtract(normal.scale(2 * motion.dot(normal)));
			this.setDeltaMovement(reflected);
		}
	}

	@Override
	protected boolean shouldBounceOnWorldBorder()
	{
		return true;
	}

	@Override
	protected double getDefaultGravity()
	{
		return 0.05;
	}

	private double getGroundMotionThreshold()
	{
		return 0.01D;
	}
}
