package uk.me.paulswilliams.auction;

public interface SniperListener {
    void sniperLost();

    void sniperBidding();

    void sniperWinning();

    void sniperWon();
}