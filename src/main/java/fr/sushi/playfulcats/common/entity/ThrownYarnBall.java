package fr.sushi.playfulcats.common.entity;

import fr.sushi.playfulcats.common.PlayfulCatRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.Objects;

public class ThrownYarnBall extends Projectile
{
	private int life;
	@Nullable
	private BlockState lastState;
	private static final EntityDataAccessor<Boolean> IN_GROUND =
			SynchedEntityData.defineId(ThrownYarnBall.class,
									   EntityDataSerializers.BOOLEAN);

	public ThrownYarnBall(EntityType<ThrownYarnBall> entityType, Level level)
	{
		super(entityType, level);
	}

	public ThrownYarnBall(Level level, double x, double y, double z)
	{
		this(PlayfulCatRegistries.EntityTypes.THROWN_YARN_BALL_TYPE.get(),
			 level);
		this.setPos(x, y, z);
		this.life = 0;
		this.entityData.set(IN_GROUND, false);
	}

	public ThrownYarnBall(Level level, LivingEntity shooter, ItemStack pickupItemStack)
	{
		this(level, shooter.getX(), shooter.getEyeY() - 0.1f, shooter.getZ());
	}

	@Override
	public boolean shouldRenderAtSqrDistance(double distance)
	{
		double d0 = this.getBoundingBox().getSize() * 10.0;
		if (Double.isNaN(d0))
		{
			d0 = 1.0;
		}
		d0 *= 64.0 * getViewScale();
		return distance < d0 * d0;
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder)
	{
		builder.define(IN_GROUND, false);
	}

	@Override
	public void shoot(double x, double y, double z, float velocity, float inaccuracy)
	{
		super.shoot(x, y, z, velocity, inaccuracy);
		this.life = 0;
	}

	@Override
	public void lerpMotion(double x, double y, double z)
	{
		super.lerpMotion(x, y, z);
		this.life = 0;
		if (this.isInGround() && Mth.lengthSquared(x, y, z) > 0.0)
		{
			this.setInGround(false);
		}
	}

	@Override
	public void tick()
	{
		Vec3 movement = this.getDeltaMovement();
		BlockPos blockpos = this.blockPosition();
		BlockState blockstate = this.level().getBlockState(blockpos);
		if (!blockstate.isAir())
		{
			VoxelShape voxelshape =
					blockstate.getCollisionShape(this.level(), blockpos);
			if (!voxelshape.isEmpty())
			{
				Vec3 vec31 = this.position();
				for (AABB aabb : voxelshape.toAabbs())
				{
					if (aabb.move(blockpos).contains(vec31))
					{
						this.setInGround(true);
						break;
					}
				}
			}
		}
		if (this.isInWaterOrRain() || blockstate.is(Blocks.POWDER_SNOW) ||
			this.isInFluidType(
					(fluidType, height) -> this.canFluidExtinguish(fluidType)))
		{
			this.clearFire();
		}
		if (this.isInGround())
		{
			if (!this.level().isClientSide())
			{
				if (this.lastState != blockstate && this.shouldFall())
				{
					this.startFalling();
				}
				else
				{
					this.tickDespawn();
				}
			}
			if (this.isAlive())
			{
				this.applyEffectsFromBlocks();
			}
		}
		else
		{
			Vec3 pos = this.position();
			if (this.isInWater())
			{
				this.applyInertia(this.getWaterInertia());
				this.addBubbleParticles(pos);
			}
			float f = (float) (Mth.atan2(movement.x, movement.z) * 180.0F /
							   (float) Math.PI);
			float f1 = (float) (
					Mth.atan2(movement.y, movement.horizontalDistance()) *
					180.0F / (float) Math.PI);
			this.setXRot(lerpRotation(this.getXRot(), f1));
			this.setYRot(lerpRotation(this.getYRot(), f));
			BlockHitResult blockhitresult = this.level().clipIncludingBorder(
					new ClipContext(movement, movement.add(pos),
									ClipContext.Block.COLLIDER,
									ClipContext.Fluid.NONE, this));
			this.stepMoveAndHit(blockhitresult);
			if (!this.isInWater())
			{
				this.applyInertia(0.99F);
			}
			if (!this.isInGround())
			{
				this.applyGravity();
			}
			super.tick();
		}
	}

	private void stepMoveAndHit(BlockHitResult hitResult)
	{
		while (this.isAlive())
		{
			Vec3 vec3 = this.position();
			EntityHitResult entityhitresult =
					this.findHitEntity(vec3, hitResult.getLocation());
			Vec3 vec31 = Objects.requireNonNullElse(entityhitresult, hitResult)
								.getLocation();
			this.setPos(vec31);
			this.applyEffectsFromBlocks(vec3, vec31);
			if (this.portalProcess != null &&
				this.portalProcess.isInsidePortalThisTick())
			{
				this.handlePortal();
			}
			if (entityhitresult == null)
			{
				if (this.isAlive() &&
					hitResult.getType() != HitResult.Type.MISS)
				{
					if (net.neoforged.neoforge.event.EventHooks.onProjectileImpact(
							this, hitResult))
					{
						break;
					}
					this.hitTargetOrDeflectSelf(hitResult);
					this.hasImpulse = true;
				}
				break;
			}
			else if (this.isAlive() && !this.noPhysics &&
					 entityhitresult.getType() != HitResult.Type.MISS)
			{
				if (net.neoforged.neoforge.event.EventHooks.onProjectileImpact(
						this, entityhitresult))
				{
					break;
				}
				this.hasImpulse = true;
				break;
			}
		}
	}

	private void applyInertia(float inertia)
	{
		Vec3 vec3 = this.getDeltaMovement();
		this.setDeltaMovement(vec3.scale((double) inertia));
	}

	private void addBubbleParticles(Vec3 pos)
	{
		Vec3 vec3 = this.getDeltaMovement();
		for (int i = 0; i < 4; i++)
		{
			float f = 0.25F;
			this.level()
				.addParticle(ParticleTypes.BUBBLE, pos.x - vec3.x * 0.25,
							 pos.y - vec3.y * 0.25, pos.z - vec3.z * 0.25,
							 vec3.x, vec3.y, vec3.z);
		}
	}

	@Override
	protected double getDefaultGravity()
	{
		return 0.05;
	}

	private boolean shouldFall()
	{
		return this.isInGround() && this.level().noCollision(
				new AABB(this.position(), this.position()).inflate(0.06));
	}

	private void startFalling()
	{
		this.setInGround(false);
		Vec3 vec3 = this.getDeltaMovement();
		this.setDeltaMovement(vec3.multiply(this.random.nextFloat() * 0.2F,
											this.random.nextFloat() * 0.2F,
											this.random.nextFloat() * 0.2F));
	}

	protected boolean isInGround()
	{
		return this.entityData.get(IN_GROUND);
	}

	protected void setInGround(boolean inGround)
	{
		this.entityData.set(IN_GROUND, inGround);
	}

	@Override
	public void move(MoverType type, Vec3 pos)
	{
		super.move(type, pos);
		if (type != MoverType.SELF && this.shouldFall())
		{
			this.startFalling();
		}
	}

	protected void tickDespawn()
	{
		this.life++;
		if (this.life >= 1200)
		{
			this.discard();
		}
	}

	@Override
	public void onInsideBubbleColumn(boolean p_382819_)
	{
		if (!this.isInGround())
		{
			super.onInsideBubbleColumn(p_382819_);
		}
	}

	@Override
	public void push(double x, double y, double z)
	{
		if (!this.isInGround())
		{
			super.push(x, y, z);
		}
	}

	@Override
	protected void onHitBlock(BlockHitResult result)
	{
		this.lastState = this.level().getBlockState(result.getBlockPos());
		super.onHitBlock(result);
		Vec3 movement = this.getDeltaMovement();
		Vec3 normal = result.getDirection().getUnitVec3();
		Vec3 reflected =
				movement.subtract(normal.scale(2 * movement.dot(normal)));
		this.setDeltaMovement(reflected);
	}



	@Nullable
	protected EntityHitResult findHitEntity(Vec3 startVec, Vec3 endVec)
	{
		return ProjectileUtil.getEntityHitResult(this.level(), this, startVec,
												 endVec, this.getBoundingBox()
															 .expandTowards(
																	 this.getDeltaMovement())
															 .inflate(1.0),
												 this::canHitEntity);
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compound)
	{
		super.addAdditionalSaveData(compound);
		compound.putShort("life", (short) this.life);
		if (this.lastState != null)
		{
			compound.put("inBlockState",
						 NbtUtils.writeBlockState(this.lastState));
		}
		compound.putBoolean("inGround", this.isInGround());
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag compound)
	{
		super.readAdditionalSaveData(compound);
		this.life = compound.getShort("life");
		if (compound.contains("inBlockState", 10))
		{
			this.lastState = NbtUtils.readBlockState(
					this.level().holderLookup(Registries.BLOCK),
					compound.getCompound("inBlockState"));
		}
		this.setInGround(compound.getBoolean("inGround"));
	}

	protected float getWaterInertia()
	{
		return 0.6F;
	}

	@Override
	protected boolean shouldBounceOnWorldBorder()
	{
		return true;
	}
}
