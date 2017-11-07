package nl.whitedove.verbruiksmanager;

import java.util.Comparator;

public class StatsComparator implements Comparator<VerbruikStat> {
    public static final StatsComparator instance=new StatsComparator();

    public int compare(VerbruikStat left, VerbruikStat right) {
        return Double.compare(right.getVerbruik(), left.getVerbruik());
    }
}
