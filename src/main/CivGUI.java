package main;

import map.Tile;
import units.Unit;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;    // Using Swing components and containers

@SuppressWarnings("serial")
public class CivGUI extends JFrame implements ActionListener
{
    public static final int MAPSIZE = 40;

    private BoardButton[][] tiles = new BoardButton[MAPSIZE+1][MAPSIZE+1];

    private ArrayList<JButton> UIButtons = new ArrayList<>();

    private Game game;
    private int CurrentX;
    private int CurrentY;
    private Point origin;

    private UITextManager uiTextManager;
    private BoardPanel boardPanel;

    private boolean unitChosen = false;

    CivGUI(Game game) throws IOException{
        this.game = game;
        boardPanel = new BoardPanel(MAPSIZE);
        JPanel uiPanel = new JPanel();

        BufferedImage image = ImageIO.read(new File("textures\\backgrounds\\UI_texture.png"));
        JPanel panelUIContent = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(image, 0, 0, null);
            }
        };

        uiTextManager = new UITextManager(panelUIContent);

        CreateUIButtons(panelUIContent);

        uiPanel.setLayout(new BorderLayout());
        uiPanel.setPreferredSize(new Dimension(1950, 125));
        uiPanel.add(panelUIContent,BorderLayout.CENTER);

        JButton endTurn = new JButton("End Turn");
        endTurn.setBounds(1050, 25, 150, 80);
        endTurn.addActionListener(arg0 -> {
            game.swapPlayer();
            unitChosen = false;
            updateGUI();
            hideUIButtons();
            updateUI();
        });
        uiPanel.add(endTurn,BorderLayout.LINE_END);

        drawBoard();

        JScrollPane scrollPane = new JScrollPane(boardPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        JFrame frame = new JFrame("Civ like game");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(uiPanel, BorderLayout.SOUTH);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);

        uiTextManager.updateUI(game.getCurrentPlayer());
    }

    private void CreateUIButtons(JPanel panelUIContent){
        int x = 300;
        int y = 10;
        for(int i = 0; i < 8;i++){
            UIButtons.add(new JButton());
            UIButtons.get(i).setBounds(x, y, 110, 50);
            UIButtons.get(i).setVisible(false);
            UIButtons.get(i).setBackground(Color.white);
            panelUIContent.add(UIButtons.get(i));
            x=x+120;
            if(x>680){
                y=70;
                x=300;
            }
        }

        for(int i = 0; i < UIButtons.size(); i++){
            int ButtonNum = i;
            UIButtons.get(i).addActionListener(arg0 -> UIButtonAction(ButtonNum));
        }
    }

    private void UIButtonAction(int ButtonNum){
        // if the blue value is 0 then the button is red which means there is not enough resources
        String type;
        ArrayList<String> typesToCheck;
        if(game.getMap().getUnit(CurrentX, CurrentY) == null){
            //no unit on this tile
            type = game.gameMap.getTile(CurrentX, CurrentY).getType();
            typesToCheck = game.gameMap.getTile(CurrentX, CurrentY).getButtonList();
        }else{
            type = game.gameMap.getUnit(CurrentX, CurrentY).getType();
            typesToCheck = game.gameMap.getUnit(CurrentX, CurrentY).getButtonList();

            unitChosen = false;
            hideUIButtons();
        }
        String text = UIButtons.get(ButtonNum).getText();

        ButtonData data = new ButtonData(type, CurrentX, CurrentY, text);

        game.buttonClicked(data);
        updateGUI();
        updateUI();
        setButtonText(typesToCheck);
    }

    private void hideUIButtons(){
        for (JButton UIButton : UIButtons) {
            UIButton.setVisible(false);
        }
    }

    private void resetUIColours(){
        for (JButton UIButton : UIButtons) {
            UIButton.setBackground(Color.white);
            UIButton.setEnabled(true);
        }
    }

    private void ColourUIButtons(int index, String type){
        if (game.CheckAvailableResources(type)){
            UIButtons.get(index).setBackground(Color.white);
            UIButtons.get(index).setEnabled(true);
        }else{
            UIButtons.get(index).setBackground(Color.red);
            UIButtons.get(index).setEnabled(false);
        }
    }

    private void drawBoard(){
        //Initialize coordinates
        int Xcoords = 0;
        int Ycoords;

        //create board elements;
        for(int x = 0; x<=MAPSIZE; x++){
            Ycoords = 0;
            for(int y = 0; y<=MAPSIZE; y++){
                tiles[x][y] = new BoardButton(x,y);
                tiles[x][y].addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) { }

                    @Override
                    public void mousePressed(MouseEvent e) {
                        origin = new Point(e.getPoint());
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {

                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {}
                });
                tiles[x][y].addMouseMotionListener( new MouseMotionListener(){
                    @Override
                    public void mouseDragged(MouseEvent e) {
                        if (origin != null) {
                            JViewport viewPort = (JViewport) SwingUtilities.getAncestorOfClass(JViewport.class, boardPanel);
                            if (viewPort != null) {
                                int deltaX = origin.x - e.getX();
                                int deltaY = origin.y - e.getY();

                                Rectangle view = viewPort.getViewRect();
                                view.x += deltaX;
                                view.y += deltaY;

                                boardPanel.scrollRectToVisible(view);
                            }
                        }
                    }

                    @Override
                    public void mouseMoved(MouseEvent arg0) {

                    }

                });
                tiles[x][y].setBounds(Xcoords, Ycoords, 50, 50);
                tiles[x][y].setIcon(game.gameMap.getTile(x, y).getImage());
                tiles[x][y].addActionListener(this);
                //set any borders
                if(game.gameMap.getTile(x, y).getOwner() != null){
                    drawBorder(x,y);
                }
                Ycoords = Ycoords + 50; //increment y for next tile
                boardPanel.add(tiles[x][y],BorderLayout.CENTER);
            }
            Xcoords = Xcoords + 50; //increment x for next tile
        }
    }

    public void actionPerformed(ActionEvent arg0) {
        BoardButton button = (BoardButton) arg0.getSource();
        int buttonXcoord = button.getXcoord();
        int buttonYcoord = button.getYcoord();

        resetUIColours();

        if(unitChosen){
            if(game.isValidMove(CurrentX, CurrentY, buttonXcoord, buttonYcoord)){ // unit moved
                game.moveUnit(CurrentX, CurrentY, buttonXcoord, buttonYcoord);
                tiles[CurrentX][CurrentY].setIcon(game.gameMap.getTile(CurrentX, CurrentY).getImage());
            }
            unitChosen = false;
            updateGUI();
            actionPerformed(arg0);
        }

        CurrentX = buttonXcoord;
        CurrentY = buttonYcoord;
        String tileType = game.getMap().getTile(buttonXcoord, buttonYcoord).getType();
        Player tileOwner = game.getMap().getTile(buttonXcoord, buttonYcoord).getOwner();

        hideUIButtons();

        uiTextManager.updateOwnershipText(tileOwner);

        if(game.getMap().getUnit(CurrentX, CurrentY) == null){ // no unit on the tile
            uiTextManager.setComponentText(2,tileType);
            if(tileOwner == game.getCurrentPlayer() || tileOwner == null){
                setButtonText(game.getMap().getTile(CurrentX, CurrentY).getButtonList());
            }
        }else{	// unit on the tile
            String unitType = game.getMap().getUnit(CurrentX, CurrentY).getType();
            uiTextManager.setComponentText(2,unitType);
            if(game.getMap().getUnit(CurrentX, CurrentY).getOwner() != game.getCurrentPlayer()){
                return;
            }
            setButtonText(game.getMap().getUnit(CurrentX, CurrentY).getButtonList());
            highlightTiles(CurrentX,CurrentY);
        }
    }

    private void setButtonText(ArrayList<String> buttonsToBuild){
        if (buttonsToBuild == null)
                return;
        int index = 0;
        for (String button: buttonsToBuild) {
            UIButtons.get(index).setText(button);
            UIButtons.get(index).setVisible(true);
            ColourUIButtons(index, button);
            index++;
        }
    }

    private void highlightTiles(int x, int y){
        for(int xc = 0; xc<MAPSIZE; xc++){
            for(int yc = 0; yc<MAPSIZE; yc++){
                if(game.isValidMove(x, y, xc, yc)){
                    tiles[xc][yc].setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.white));
                    unitChosen = true;
                }
            }
        }
    }

    private void updateUI(){
        uiTextManager.updateUI(game.getCurrentPlayer());
    }

    private void drawRoad(int x, int y){
        boolean North = game.gameMap.RoadAdjacent(x, y-1); //north
        boolean South = game.gameMap.RoadAdjacent(x, y+1); //south
        boolean East = game.gameMap.RoadAdjacent(x+1, y); //east
        boolean West = game.gameMap.RoadAdjacent(x-1, y); //west

        ImageIcon icon = game.getMap().getTile(x,y).getImage(North,South,East,West);

        tiles[x][y].setIcon(icon);
    }

    private void updateGUI(){
        int xHigh = Math.min(CurrentX+10,MAPSIZE);
        int yHigh = Math.min(CurrentY+10,MAPSIZE);
        int xLow = Math.max(CurrentX-10,0);
        int yLow = Math.max(CurrentY-10,0);

        for(int x = xLow; x<xHigh; x++){
            for(int y = yLow; y<yHigh; y++){
                //reset borders
                tiles[x][y].setBorder(UIManager.getBorder("Button.border"));

                Unit currentUnit = game.gameMap.getUnit(x, y);
                Tile currentTile = game.gameMap.getTile(x, y);

                //set any borders
                if(currentUnit != null){
                    tiles[x][y].setIcon(currentUnit.getImage());
                    tiles[x][y].setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, currentUnit.getOwner().getColour()));
                }else if(currentTile.getOwner() != null){    //tile belongs to a player
                    if(currentTile.getType().equals("Road")){
                        drawRoad(x,y);
                    }else{
                        tiles[x][y].setIcon(game.gameMap.getTile(x, y).getImage());
                    }
                    drawBorder(x,y);

                    if(currentTile.isInUse() && !currentTile.getType().equals("Wheat")){
                        tiles[x][y].setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, currentTile.getOwner().getColour()));
                    }
                    if(currentTile.getHasCityConnection() ){ //TEMPORARY
                        tiles[x][y].setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.green));
                    }
                    tiles[x][y].setBorderPainted(true);
                }
            }
        }
        uiTextManager.setComponentText(0,game.getCurrentPlayer().getName() + "'s Turn");
    }

    private void drawBorder(int x, int y){
        tiles[x][y].setBorder(BorderFactory.createMatteBorder(game.gameMap.BorderRequired(x, y, x, y-1),
                game.gameMap.BorderRequired(x, y, x-1, y),
                game.gameMap.BorderRequired(x, y, x, y+1),
                game.gameMap.BorderRequired(x, y, x+1, y),
                game.getMap().getTile(x,y).getOwner().getColour()));
    }
}