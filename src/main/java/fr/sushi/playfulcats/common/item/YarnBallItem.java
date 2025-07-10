package fr.sushi.playfulcats.common.item;

import fr.sushi.playfulcats.common.entity.ThrownYarnBall;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class YarnBallItem extends Item implements ProjectileItem
{
	public YarnBallItem(Properties properties)
	{
		super(properties);
	}

	private float getStrengthForTimeUsed(ItemStack stack, LivingEntity shooter, int timeLeft)
	{
		float timeUsed = this.getUseDuration(stack, shooter) - timeLeft;
		float strength = timeUsed / this.getUseDuration(stack, shooter);
		return Math.min(strength, 1.0f) * 2.5f;
	}

	@Override
	public boolean releaseUsing(ItemStack stack, Level level, LivingEntity shooter, int timeLeft)
	{
		if (this.getUseDuration(stack, shooter) - timeLeft < 5) {
			return false;
		}
		if (level instanceof ServerLevel serverlevel)
		{
			float strength = this.getStrengthForTimeUsed(stack, shooter, timeLeft);
			ThrownYarnBall projectile = Projectile.spawnProjectileFromRotation(
					ThrownYarnBall::new, serverlevel, stack, shooter, 0.0f, strength, 1.0f);
		} return true;
	}

	@Override
	public @NotNull InteractionResult use(Level level, Player player, InteractionHand hand)
	{
		player.startUsingItem(hand);
		return InteractionResult.CONSUME;
	}

	@Override
	public int getUseDuration(ItemStack stack, LivingEntity entity)
	{
		return 50;
	}

	@Override
	public ItemUseAnimation getUseAnimation(ItemStack stack)
	{
		return ItemUseAnimation.SPEAR;
	}

	@Override
	public @NotNull Projectile asProjectile(Level level, Position pos, ItemStack stack, Direction direction)
	{
		return new ThrownYarnBall(level, pos.x(), pos.y(), pos.z());
	}
}
