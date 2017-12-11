package main;

import javax.swing.*;

class UITextManager {
    private JLabel[] UIComponents = new JLabel[20];

    UITextManager(JPanel panelUIContent){
        panelUIContent.setLayout(null);

        for(int i = 0; i < 17; i++){
            UIComponents[i] = new JLabel();
            panelUIContent.add(UIComponents[i]);
            UIComponents[i].setVisible(true);
        }

        UIComponents[0].setBounds(100, 25, 100, 20);

        UIComponents[1].setBounds(100, 45, 250, 20);

        UIComponents[2].setBounds(100, 65, 250, 20);

        UIComponents[3].setBounds(1430, 25, 250, 20);
        UIComponents[4].setBounds(1370, 10, 50, 50);
        ImageIcon icon = new ImageIcon("C:\\Users\\Daniel\\workspace\\Civ\\textures\\terrain\\Tree.png");
        UIComponents[4].setIcon(icon);

        UIComponents[5].setBounds(1430, 80, 250, 20);
        UIComponents[6].setBounds(1370, 65, 50, 50);
        icon = new ImageIcon("C:\\Users\\Daniel\\workspace\\Civ\\textures\\terrain\\iron.png");
        UIComponents[6].setIcon(icon);

        UIComponents[7].setBounds(1590, 25, 250, 20);
        UIComponents[8].setBounds(1530, 10, 50, 50);
        icon = new ImageIcon("C:\\Users\\Daniel\\workspace\\Civ\\textures\\terrain\\gold.png");
        UIComponents[8].setIcon(icon);

        UIComponents[9].setBounds(1590, 80, 250, 20);
        UIComponents[10].setBounds(1530, 65, 50, 50);
        icon = new ImageIcon("C:\\Users\\Daniel\\workspace\\Civ\\textures\\terrain\\coal.png");
        UIComponents[10].setIcon(icon);

        UIComponents[11].setBounds(1750, 25, 250, 20);
        UIComponents[12].setBounds(1690, 10, 50, 50);
        icon = new ImageIcon("C:\\Users\\Daniel\\workspace\\Civ\\textures\\terrain\\copper.png");
        UIComponents[12].setIcon(icon);

        UIComponents[13].setBounds(1750, 80, 250, 20);
        UIComponents[14].setBounds(1690, 65, 50, 50);
        icon = new ImageIcon("C:\\Users\\Daniel\\workspace\\Civ\\textures\\terrain\\Mountain.png");
        UIComponents[14].setIcon(icon);

        UIComponents[15].setBounds(1270, 25, 250, 20);
        UIComponents[16].setBounds(1210, 10, 50, 50);
        icon = new ImageIcon("C:\\Users\\Daniel\\workspace\\Civ\\textures\\terrain\\farmFood.jpg");
        UIComponents[16].setIcon(icon);
    }

    void updateUI(Player currentPlayer){
        UIComponents[3].setText("Wood x " + currentPlayer.getResource(0));
        UIComponents[5].setText("Iron x " + currentPlayer.getResource(1));
        UIComponents[7].setText("Gold x " + currentPlayer.getResource(2));
        UIComponents[9].setText("Coal x " + currentPlayer.getResource(3));
        UIComponents[11].setText("Copper x " + currentPlayer.getResource(4));
        UIComponents[13].setText("Stone x " + currentPlayer.getResource(5));
        UIComponents[15].setText("Food x " + currentPlayer.getResource(6));
        UIComponents[0].setText(currentPlayer.getName() + "'s Turn");
    }

    void setComponentText(int index, String text){
        UIComponents[index].setText(text);
    }

    void updateOwnershipText(Player owner){
        if(owner == null){
            UIComponents[1].setText("This tile is in Natures Territory");
        }else{
            UIComponents[1].setText("This tile is in " + owner.getName() + "'s Territory");
        }
    }

}
