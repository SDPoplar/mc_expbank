package com.seadrip.expbank;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ExpPotion extends Item {
    public static final int POTION_VOLUME = 100;

    public ExpPotion() {
        super(new Item.Properties()
                .stacksTo(99)
                .rarity(Rarity.EPIC)
                .setNoRepair()
                // .food(POTION_PROP)
        );
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, @NotNull Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if(stack.isEmpty()) {
            return InteractionResultHolder.fail(player.getItemInHand(hand));
        }
        player.startUsingItem(hand);
        player.giveExperiencePoints(POTION_VOLUME);
        stack.setCount(stack.getCount() - 1);
        if(level.isClientSide()) {
            player.sendSystemMessage(Component.literal(POTION_VOLUME + " exp given, total: " + player.totalExperience));
        }
        return InteractionResultHolder.consume(stack);
    }
}
