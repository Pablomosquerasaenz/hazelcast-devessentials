package essentials.streamProcessing.support;

import java.util.Arrays;
import java.util.List;

public enum CoinType {
    BTC("Bitcoin", "#btc", "#bitcoin"),
    ETH("Ether", "ethereum", "#eth", "#ether", "#ethereum"),
    EOS("EOS", "#eos"),
    XRP("Ripple", "#xrp", "#ripple"),
    BCH("Bitcoin Cash", "#bitcoincash", "#bch"),
    LTC("Litecoin", "#ltc", "#litecoin"),
    XLM("Stellar", "#xlm", "#steller");

    private final String name;
    private final List<String> markers;

    CoinType(String... nameAndMarkers) {
        this.name = nameAndMarkers[0];
        this.markers = Arrays.asList(nameAndMarkers);
    }

    public String toString() {
        return name;
    }

    public List<String> markers() {
        return markers;
    }
}
