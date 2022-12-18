package fr.mamiemru.blocrouter.gui.menu;

import fr.mamiemru.blocrouter.entities.BaseEntityPatternEncoder;
import fr.mamiemru.blocrouter.entities.BaseEntityWithMenuProvider;
import fr.mamiemru.blocrouter.entities.custom.patternEncoder.PatternEncoderEntity;
import fr.mamiemru.blocrouter.gui.menu.menus.patternEncoder.VacuumPatternEncoderMenu;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

public abstract class BaseContainerMenuPatternEncoder extends BaseContainerMenu {

    protected final CraftingContainer craftMatrix;

    protected BaseContainerMenuPatternEncoder(int id, Inventory inv, @NotNull MenuType<?> menu, ContainerData data, BaseEntityWithMenuProvider entity, int craftMatrixWidth, int craftMatrixHeight) {
        super(id, inv, menu, data, entity);
        this.craftMatrix = new CraftingContainer(this, craftMatrixWidth, craftMatrixHeight);
    }

    public BaseEntityPatternEncoder getEntity() {
        return (BaseEntityPatternEncoder) this.blockEntity;
    }

    protected void fillFakeSlots(CraftingContainer craftMatrix, IItemHandler handler) {
        for (int craftingMatrixSlotIndex = 0; craftingMatrixSlotIndex < getEntity().getNumberOfIngredientsSlots(); ++craftingMatrixSlotIndex) {
            craftMatrix.setItem(craftingMatrixSlotIndex, handler.getStackInSlot(PatternEncoderEntity.SLOT_INPUT_SLOT_0 + craftingMatrixSlotIndex));
        }
    }

}
