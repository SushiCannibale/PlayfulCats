package fr.sushi.playfulcats.common.entity;

import fr.sushi.playfulcats.common.PlayfulCatRegistries;
import fr.sushi.playfulcats.common.item.YarnBallItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.debug.DebugRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
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
	public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity entity)
	{
		return new ClientboundAddEntityPacket(this, entity);
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
	protected void onHitEntity(EntityHitResult result)
	{
		super.onHitEntity(result);
		Entity entity = result.getEntity();
		float deceleration = this.getHitDeceleration(entity);
		float knockback = this.getHitKnockback(entity);
		entity.push(this.getDeltaMovement().scale(knockback));
		this.setDeltaMovement(
				this.getDeltaMovement().scale(deceleration).reverse());
	}

	@Override
	public boolean isPickable()
	{
		return true;
	}

	@Override
	public float getPickRadius()
	{
		return 0.0f;
	}

	@Override
	public InteractionResult interact(Player player, InteractionHand hand)
	{
		InteractionResult leashResult = super.interact(player, hand);
		if (leashResult == InteractionResult.PASS &&
			player.getItemInHand(hand) == ItemStack.EMPTY)
		{
			if (!this.level().isClientSide())
			{
				YarnBallItem item =
						PlayfulCatRegistries.Items.YARN_BALL_ITEM.get();
				if (!player.hasInfiniteMaterials()) {
					player.addItem(new ItemStack(item));
				}
				this.gameEvent(GameEvent.ENTITY_INTERACT, player);
				this.discard();
			}
			return InteractionResult.SUCCESS;
		}
		return leashResult;
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

	private float getHitDeceleration(Entity entity)
	{
		return 0.5f;
	}

	private float getHitKnockback(Entity entity)
	{
		return 0.9f;
	}

	private float getGroundMotionThreshold()
	{
		return 0.01f;
	}
}
