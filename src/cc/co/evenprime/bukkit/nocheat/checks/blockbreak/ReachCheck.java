package cc.co.evenprime.bukkit.nocheat.checks.blockbreak;

import java.util.Locale;
import cc.co.evenprime.bukkit.nocheat.NoCheat;
import cc.co.evenprime.bukkit.nocheat.NoCheatPlayer;
import cc.co.evenprime.bukkit.nocheat.actions.ParameterName;
import cc.co.evenprime.bukkit.nocheat.checks.CheckUtil;
import cc.co.evenprime.bukkit.nocheat.data.SimpleLocation;
import cc.co.evenprime.bukkit.nocheat.data.Statistics.Id;

/**
 * The reach check will find out if a player interacts with something that's too
 * far away
 * 
 */
public class ReachCheck extends BlockBreakCheck {

    public ReachCheck(NoCheat plugin) {
        super(plugin, "blockbreak.reach");
    }

    public boolean check(NoCheatPlayer player, BlockBreakData data, BlockBreakConfig cc) {

        boolean cancel = false;

        final SimpleLocation brokenBlock = data.brokenBlockLocation;

        final double distance = CheckUtil.reachCheck(player, brokenBlock.x + 0.5D, brokenBlock.y + 0.5D, brokenBlock.z + 0.5D, player.isCreative() ? cc.reachDistance + 2 : cc.reachDistance);

        if(distance > 0D) {
            // Player failed the check

            // Increment violation counter
            data.reachVL += distance;
            incrementStatistics(player, Id.BB_REACH, distance);
            data.reachDistance = distance;

            cancel = executeActions(player, cc.reachActions, data.reachVL);
        } else {
            data.reachVL *= 0.9D;
        }

        return cancel;
    }

    public String getParameter(ParameterName wildcard, NoCheatPlayer player) {

        if(wildcard == ParameterName.VIOLATIONS)
            return String.format(Locale.US, "%d", (int) getData(player.getDataStore()).reachVL);
        else if(wildcard == ParameterName.REACHDISTANCE)
            return String.format(Locale.US, "%.2f", getData(player.getDataStore()).reachDistance);
        else
            return super.getParameter(wildcard, player);
    }
}
