package com.seadrip.expbank;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BottleItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

public class ExpPacker extends Block {
    private static final Properties BLOCK_PROP = Properties.of().strength(5);

    public ExpPacker() {
        super(BLOCK_PROP);
    }

    @Override
    public @NotNull InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        ItemStack stack = player.getItemInHand(hand);
        if(stack.isEmpty() || !stack.is(Items.GLASS_BOTTLE)) {
            return InteractionResult.PASS;
        }
        if(player.totalExperience < 100) {
            if(level.isClientSide) {
                player.sendSystemMessage(Component.literal("no enough experience, at least 100 is needed"));
            }
            return InteractionResult.FAIL;
        }
        int consumeBottle = Math.min(stack.getCount(), player.totalExperience / ExpPotion.POTION_VOLUME);
        stack.setCount(stack.getCount() - consumeBottle);
        ItemStack pop = ExpBank.EXP_POTION.get().getDefaultInstance();
        pop.setCount(consumeBottle);
        player.giveExperiencePoints(-1 * ExpPotion.POTION_VOLUME * consumeBottle);
        popResource(level, pos, pop);
        return InteractionResult.CONSUME;
    }
}
