package main;

import map.Tile;

import javax.swing.*;

class UiTextManager {
    private JLabel[] UIComponents = new JLabel[20];

    UiTextManager(JPanel panelUIContent) {
        panelUIContent.setLayout(null);

        for (int i = 0; i < UIComponents.length; i++) {
            UIComponents[i] = new JLabel();
            panelUIContent.add(UIComponents[i]);
            UIComponents[i].setVisible(true);
        }

        UIComponents[0].setBounds(100, 25, 100, 20);
        UIComponents[1].setBounds(100, 45, 250, 20);
        UIComponents[2].setBounds(100, 65, 250, 20);
        UIComponents[3].setBounds(100, 85, 250, 20);

        UIComponents[4].setBounds(1430, 25, 250, 20);
        UIComponents[5].setBounds(1370, 10, 50, 50);
        ImageIcon icon = new ImageIcon("textures\\terrain\\Tree.png");
        UIComponents[5].setIcon(icon);

        UIComponents[6].setBounds(1430, 80, 250, 20);
        UIComponents[7].setBounds(1370, 65, 50, 50);
        icon = new ImageIcon("textures\\terrain\\iron.png");
        UIComponents[7].setIcon(icon);

        UIComponents[8].setBounds(1590, 25, 250, 20);
        UIComponents[9].setBounds(1530, 10, 50, 50);
        icon = new ImageIcon("textures\\terrain\\gold.png");
        UIComponents[9].setIcon(icon);

        UIComponents[10].setBounds(1590, 80, 250, 20);
        UIComponents[11].setBounds(1530, 65, 50, 50);
        icon = new ImageIcon("textures\\terrain\\coal.png");
        UIComponents[11].setIcon(icon);

        UIComponents[12].setBounds(1750, 25, 250, 20);
        UIComponents[13].setBounds(1690, 10, 50, 50);
        icon = new ImageIcon("textures\\terrain\\copper.png");
        UIComponents[13].setIcon(icon);

        UIComponents[14].setBounds(1750, 80, 250, 20);
        UIComponents[15].setBounds(1690, 65, 50, 50);
        icon = new ImageIcon("textures\\terrain\\Mountain.png");
        UIComponents[15].setIcon(icon);

        UIComponents[16].setBounds(1270, 25, 250, 20);
        UIComponents[17].setBounds(1210, 10, 50, 50);
        icon = new ImageIcon("textures\\terrain\\farmFood.jpg");
        UIComponents[17].setIcon(icon);

        UIComponents[18].setBounds(1270, 80, 250, 20);
        UIComponents[19].setBounds(1210, 65, 50, 50);
        icon = new ImageIcon("textures\\terrain\\diamonds.png");
        UIComponents[19].setIcon(icon);
    }

    void updateUI(Player currentPlayer) {
        UIComponents[4].setText("Wood x " + currentPlayer.getResource(0));
        UIComponents[6].setText("Iron x " + currentPlayer.getResource(1));
        UIComponents[8].setText("Gold x " + currentPlayer.getResource(2));
        UIComponents[10].setText("Coal x " + currentPlayer.getResource(3));
        UIComponents[12].setText("Copper x " + currentPlayer.getResource(4));
        UIComponents[14].setText("Stone x " + currentPlayer.getResource(5));
        UIComponents[16].setText("Food x " + currentPlayer.getResource(6));
        UIComponents[18].setText("Diamonds x " + currentPlayer.getResource(8));
        UIComponents[0].setText(currentPlayer.getName() + "'s Turn");
    }

    void updateInformationText(Tile tileClicked) {
        String tileType;
        int currentHealth = 0;
        int maxHealth = 0;

        if (tileClicked.hasUnit()) {
            tileType = tileClicked.getUnit().getType();
            currentHealth = tileClicked.getUnit().getCurrentHealth();
            maxHealth = tileClicked.getUnit().getMaxHealth();
        } else if (tileClicked.hasBuilding()) {
            tileType = tileClicked.getBuilding().getType();
            currentHealth = tileClicked.getBuilding().getCurrentHealth();
            maxHealth = tileClicked.getBuilding().getMaxHealth();
        } else {
            tileType = tileClicked.getResource().getResourceType().toString();
        }

        updateTileType(tileType);
        updateUnitOrBuildingHealth(currentHealth, maxHealth);
        updateTerritoryOwnership(tileClicked.getOwner());
    }

    private void updateTerritoryOwnership(Player tileOwner) {
        if (tileOwner == null) {
            UIComponents[1].setText("This tile is in Natures Territory");
        } else {
            UIComponents[1].setText("This tile is in " + tileOwner.getName() + "'s Territory");
        }
    }

    private void updateTileType(String tileType) {
        final int TILE_TYPE_INDEX = 2;
        UIComponents[TILE_TYPE_INDEX].setText(tileType);
    }

    private void updateUnitOrBuildingHealth(int currentHealth, int maxHealth) {
        final int TILE_HEALTH_INDEX = 3;

        if (maxHealth == 0) {
            UIComponents[TILE_HEALTH_INDEX].setText("");
        } else {
            UIComponents[TILE_HEALTH_INDEX].setText("Health: " + String.valueOf(currentHealth) + '/' + String.valueOf(maxHealth));
        }
    }
}
