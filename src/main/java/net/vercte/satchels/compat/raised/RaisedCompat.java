package net.vercte.satchels.compat.raised;

import dev.yurisuika.raised.api.RaisedApi;
import net.vercte.satchels.Satchels;
import net.vercte.satchels.client.satchel.SatchelHotbarOverlay;
import net.vercte.satchels.compat.CompatEntrypoint;

public class RaisedCompat implements CompatEntrypoint {
    @Override
    public void initialize() {}

    public static int getSatchelYOffset() {
        return RaisedApi.getY(Satchels.at(SatchelHotbarOverlay.ID));
    }
}
