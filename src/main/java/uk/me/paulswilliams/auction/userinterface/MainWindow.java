package uk.me.paulswilliams.auction.userinterface;

import uk.me.paulswilliams.auction.Announcer;
import uk.me.paulswilliams.auction.Item;
import uk.me.paulswilliams.auction.SniperPortfolio;
import uk.me.paulswilliams.auction.UserRequestListener;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

import static uk.me.paulswilliams.auction.Main.MAIN_WINDOW_NAME;

public class MainWindow extends JFrame{
    public static final String SNIPER_STATUS_NAME = "sniper status";

    private static final String SNIPERS_TABLE_NAME = "snipersTable";
    public static final String APPLICATION_TITLE = "Auction Sniper";
    public static final String JOIN_BUTTON_NAME = "Join auction";
    public static final String NEW_ITEM_ID_NAME = "new item id";
    public static final String NEW_ITEM_STOP_PRICE_NAME = "stop price";
    private Announcer<UserRequestListener> userRequests = Announcer.to(UserRequestListener.class);
    private SnipersTableModel snipers;

    public MainWindow(SniperPortfolio portfolio) {
        super("Auction Sniper");
        snipers = new SnipersTableModel();
        portfolio.addPortfolioListener(snipers);

        setName(MAIN_WINDOW_NAME);
        fillContentPane(makeSnipersTable(), makeControls());
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private JPanel makeControls() {
        JPanel controls = new JPanel(new FlowLayout());

        final JTextField itemIdField = new JTextField();
        itemIdField.setColumns(25);
        itemIdField.setName(NEW_ITEM_ID_NAME);
        controls.add(itemIdField);

        final JFormattedTextField stopPriceField = new JFormattedTextField(NumberFormat.getNumberInstance());
        stopPriceField.setColumns(7);
        stopPriceField.setName(NEW_ITEM_STOP_PRICE_NAME);
        controls.add(stopPriceField);

        JButton joinAuctionButton = new JButton("Join Auction");
        joinAuctionButton.setName(JOIN_BUTTON_NAME);
        joinAuctionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userRequests.announce().joinAuction(new Item(itemId(), stopPrice()));
            }

            private int stopPrice() {
                return ((Number)stopPriceField.getValue()).intValue();
            }

            private String itemId() {
                return itemIdField.getText();
            }
        });
        controls.add(joinAuctionButton);

        return controls;
    }

    private void fillContentPane(JTable snipersTable, JPanel controls) {
        final Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(controls, BorderLayout.NORTH);
        contentPane.add(new JScrollPane(snipersTable), BorderLayout.CENTER);
    }

    private JTable makeSnipersTable() {
        final JTable snipersTable = new JTable(snipers);
        snipersTable.setName(SNIPERS_TABLE_NAME);
        return snipersTable;
    }

    public void addUserRequestListener(UserRequestListener userRequestListener) {
        userRequests.addListener(userRequestListener);
    }
}
