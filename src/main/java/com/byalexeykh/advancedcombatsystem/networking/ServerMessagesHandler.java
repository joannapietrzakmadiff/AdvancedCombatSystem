package com.byalexeykh.advancedcombatsystem.networking;

import com.byalexeykh.advancedcombatsystem.networking.messages.MessageDestroyBlock;
import com.byalexeykh.advancedcombatsystem.networking.messages.MessageSendDefaultsConfig;
import com.byalexeykh.advancedcombatsystem.networking.messages.MessageSwing;
import com.byalexeykh.advancedcombatsystem.networking.messages.MessageSwingEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

import static com.byalexeykh.advancedcombatsystem.AdvancedCombatSystem.calculateDamage;

public class ServerMessagesHandler {

    private static Logger LOGGER = LogManager.getLogger();

    public static void onMessageSwingReceived(MessageSwing msg, Supplier<NetworkEvent.Context> ctxSupplier){
        NetworkEvent.Context ctx = ctxSupplier.get();
        LogicalSide sideReceived = ctx.getDirection().getReceptionSide();
        World world = ctx.getSender().getEntityWorld();
        ctx.setPacketHandled(true);

        if(sideReceived != LogicalSide.SERVER){
            LOGGER.error("MessageSwing received on wrong side!: " + sideReceived);
            return;
        }
        if(!msg.isMessageValid()){
            LOGGER.error("Invalid message! " + msg.toString());
            return;
        }

        final ServerPlayerEntity player = ctx.getSender();
        if(player == null){
            LOGGER.error("ServerMessagesHandler | onMessageSwingReceived | Player was null when message was received!");
        }

        ctx.enqueueWork(() ->{
            Entity entityToHit = world.getEntityByID(msg.getEntityToHitID());
            try{
                entityToHit.getEntity().attackEntityFrom(
                        DamageSource.causePlayerDamage(player),
                        calculateDamage(msg.getTicksLMBIsPressed(), player, entityToHit)
                );
            }catch (Exception e){
                LOGGER.error("[ACS-server] ERROR while executing attackEntityFrom: " + e + " | player: " + player + " | message: " + msg + " | entityToHit: " + entityToHit);
            }
            if(player.isSprinting()){
                Vec3d fromPlayerToEntity = new Vec3d(entityToHit.getPosX() - player.getPosX(), entityToHit.getPosY() - player.getPosYEye(), entityToHit.getPosZ() - player.getPosZ()).normalize();
                entityToHit.addVelocity(fromPlayerToEntity.x / 6, fromPlayerToEntity.y / 6, fromPlayerToEntity.z / 6);
            }

            player.getHeldItemMainhand().damageItem(1, player, PlayerEntity -> {PlayerEntity.sendBreakAnimation(EquipmentSlotType.MAINHAND);});
        });
    }

    public static void OnMessageDestroyBlockReceived(MessageDestroyBlock msg, Supplier<NetworkEvent.Context> ctxSupplier){
        NetworkEvent.Context ctx = ctxSupplier.get();
        LogicalSide sideReceived = ctx.getDirection().getReceptionSide();
        World world = ctx.getSender().getEntityWorld();
        ctx.setPacketHandled(true);

        if(sideReceived != LogicalSide.SERVER){
            LOGGER.error("MessageDestroyBlock received on wrong side!: " + sideReceived);
            return;
        }
        if(!msg.isMessageValid()){
            LOGGER.error("Invalid message! " + msg.toString());
            return;
        }

        final ServerPlayerEntity player = ctx.getSender();
        if(player == null){
            LOGGER.error("ServerMessagesHandler | OnMessageDestroyBlockReceived | Player was null when message was received!");
        }

        ctx.enqueueWork(() ->{
            //LOGGER.warn("[SERVER] destroying block " + world.getBlockState(msg.getBlockPosToDestroy()) + "at: " + msg.getBlockPosToDestroy().toString());
            world.destroyBlock(msg.getBlockPosToDestroy(), true);
            player.getHeldItemMainhand().damageItem(1, player, PlayerEntity -> {PlayerEntity.sendBreakAnimation(EquipmentSlotType.MAINHAND);});
        });
    }

    public static void OnMessageSwingEffectsReceived(MessageSwingEffects msg, Supplier<NetworkEvent.Context> ctxSupplier){
        NetworkEvent.Context ctx = ctxSupplier.get();
        LogicalSide sideReceived = ctx.getDirection().getReceptionSide();
        World world = ctx.getSender().getEntityWorld();
        ctx.setPacketHandled(true);

        if(sideReceived != LogicalSide.SERVER){
            LOGGER.error("MessageSwingEffects received on wrong side!: " + sideReceived);
            return;
        }

        final ServerPlayerEntity player = ctx.getSender();
        if(player == null){
            LOGGER.error("ServerMessagesHandler | OnMessageSwingEffectsReceived | Player was null when message was received!");
        }

        ctx.enqueueWork(() ->{
            world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, player.getSoundCategory(), 1.0F, 1.0F);
            player.spawnSweepParticles();
        });
    }
}
