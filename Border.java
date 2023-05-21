package hungrygamespackage;

import org.bukkit.World;

public class Border {
    public static void shrinkBorder(World w, int t) {
        if (t > 20 && t % 40 == 0) {
            w.getWorldBorder().setSize(w.getWorldBorder().getSize() - 0.1, 1);
        } else if (t < 20) {
            w.getWorldBorder().setSize(250);
        }
    }
}
