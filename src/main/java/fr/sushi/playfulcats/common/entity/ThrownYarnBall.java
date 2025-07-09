package fr.sushi.playfulcats.common.entity;

import fr.sushi.playfulcats.common.PlayfulCatRegistries;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ThrownYarnBall extends ThrowableProjectile
{
	public ThrownYarnBall(EntityType<ThrownYarnBall> entityType, Level level)
	{
		super(entityType, level);
	}

	public ThrownYarnBall(Level level, LivingEntity shooter, ItemStack pickupItemStack) {
		this(level, shooter.getX(), shooter.getEyeY(), shooter.getZ());
	}

	public ThrownYarnBall(Level level, double x, double y, double z)
	{
		super(PlayfulCatRegistries.EntityTypes.THROWN_YARN_BALL_TYPE.get(), x,
			  y, z, level);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder)
	{
	}

	@Override
	public void tick()
	{
		super.tick();
	}
}
