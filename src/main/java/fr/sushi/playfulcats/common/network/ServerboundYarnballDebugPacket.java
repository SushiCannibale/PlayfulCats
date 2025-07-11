package fr.sushi.playfulcats.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ServerboundYarnballDebugPacket(
		CompoundTag tag) implements CustomPacketPayload
{
	public static final CustomPacketPayload.Type<ServerboundYarnballDebugPacket>
			TYPE = new CustomPacketPayload.Type<>(
			ResourceLocation.fromNamespaceAndPath("mymod", "my_data"));

	public static final StreamCodec<ByteBuf, ServerboundYarnballDebugPacket>
			STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.COMPOUND_TAG,
												 ServerboundYarnballDebugPacket::tag,
												 ServerboundYarnballDebugPacket::new);

	@Override
	public Type<? extends CustomPacketPayload> type()
	{
		return TYPE;
	}

	public void handler(IPayloadContext iPayloadContext)
	{

	}
}
