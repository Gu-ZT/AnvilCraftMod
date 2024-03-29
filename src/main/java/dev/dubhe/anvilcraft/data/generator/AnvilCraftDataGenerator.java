package dev.dubhe.anvilcraft.data.generator;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import org.jetbrains.annotations.NotNull;

public class AnvilCraftDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(@NotNull FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(MyBlockTagGenerator::new);
        pack.addProvider(MyItemTagGenerator::new);
        pack.addProvider(MyRecipesGenerator::new);
        pack.addProvider(MyBlockLootGenerator::new);
    }
}
