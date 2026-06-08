package net.bananabh.toastmod;

import net.minecraft.text.Text;
import net.fabricmc.api.ModInitializer;
import net.minecraft.item.FoodComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.RegistryKeys.creative_mode_tab;
import org.jetbrains.annotations.Nullable;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.util.Formatting;
import java.util.List;

public class ToastMod implements ModInitializer {
    public static final String MODID = "toastmod";

    public static final RegistryKey<ItemGroup> TOAST_GROUP_KEY = RegistryKey.of(
        RegistryKeys.ITEM_GROUP,
        new Identifier(MODID, "item_group")
    );

    public static class BurntToastItem extends Item {
        public BurntToastItem(Settings settings) {
            super(settings);
        }

        @Override
        public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
            tooltip.add(Text.translatable("item.toastmod.burnt_toast.description").formatted(Formatting.GRAY));
        }
    }

    // Bread is 0.6f, so 2.6f to match +2 saturation
    // note from future me: yeah no that's not right lmao bread is 2f but 4f would be way too powerful so i'm keeping it as 2.6f
    // note from future future me: still not right lmfao this is not how anything works nutrition actually controls the hunger bar so i'll change it to 0.6f and 6 nutrition
    public static final FoodComponent TOAST_FOOD = new FoodComponent.Builder()
            .hunger(6)
            .saturationModifier(0.6f)
            .build();

    // note to future self: add code for bread slice here
    public static final FoodComponent BREAD_SLICE_FOOD = new FoodComponent.Builder()
            .hunger(5)
            .saturationModifier(0.6f)
            .build();
    // yw past self

    public static final FoodComponent RAW_ENDERMAN_FOOD = new FoodComponent.Builder()
            .hunger(2)
            .saturationModifier(0.6f)
            .statusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 400, 0), 0.5f)
            .build();

    public static final FoodComponent COOKED_ENDERMAN_FOOD = new FoodComponent.Builder()
            .hunger(4)
            .saturationModifier(0.6f)
            .build();

    // Custom Item class to handle teleportation logic
    public static class EndermanMeatItem extends Item {
        private final float teleportChance;

        public EndermanMeatItem(Settings settings, float teleportChance) {
            super(settings);
            this.teleportChance = teleportChance;
        }

        @Override
        public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
            ItemStack result = super.finishUsing(stack, world, user);
            if (!world.isClient && world.random.nextFloat() < this.teleportChance) {
                // This mimics the Chorus Fruit teleport logic
                double d = user.getX();
                double e = user.getY();
                double f = user.getZ();

                for (int i = 0; i < 16; ++i) {
                    double g = user.getX() + (user.getRandom().nextDouble() - 0.5) * 16.0;
                    double h = user.getY() + (user.getRandom().nextInt(16) - 8);
                    double j = user.getZ() + (user.getRandom().nextDouble() - 0.5) * 16.0;

                    if (user.teleport(g, h, j, true)) {
                        break;
                    }
                }
            }
            return result;
        }
    }

    public static final Item BREAD_SLICE = new Item(new Item.Settings().food(BREAD_SLICE_FOOD));
    public static final Item TOAST = new Item(new Item.Settings().food(TOAST_FOOD));
    public static final Item BURNT_TOAST = new BurntToastItem(new Item.Settings());
    public static final Item RAW_ENDERMAN = new EndermanMeatItem(new Item.Settings().food(RAW_ENDERMAN_FOOD), 0.5f);
    public static final Item COOKED_ENDERMAN = new EndermanMeatItem(new Item.Settings().food(COOKED_ENDERMAN_FOOD), 0.3f);

    public static final ItemGroup TOAST_GROUP = FabricItemGroup.builder(new Identifier(MODID, "toast_group"))
        .displayName(Text.translatable("itemGroup.toastmod.toast_group"))
        .icon(() -> new ItemStack(TOAST))
        .entries((displayContext, entries) -> {
            entries.add(BREAD_SLICE);
            entries.add(TOAST);
            entries.add(BURNT_TOAST);
            entries.add(RAW_ENDERMAN);
            entries.add(COOKED_ENDERMAN);
        })
        .build();

    @Override
    public void onInitialize() {
        Registry.register(Registries.ITEM, new Identifier(MODID, "bread_slice"), BREAD_SLICE);
        Registry.register(Registries.ITEM, new Identifier(MODID, "toast"), TOAST);
        Registry.register(Registries.ITEM, new Identifier(MODID, "burnt_toast"), BURNT_TOAST);
        Registry.register(Registries.ITEM, new Identifier(MODID, "raw_enderman"), RAW_ENDERMAN);
        Registry.register(Registries.ITEM, new Identifier(MODID, "cooked_enderman"), COOKED_ENDERMAN);

        Registry.register(Registries.ITEM_GROUP, TOAST_GROUP_KEY, TOAST_GROUP);
    }
}
