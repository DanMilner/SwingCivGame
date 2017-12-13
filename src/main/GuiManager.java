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
public class GuiManager extends JFrame implements ActionListener {
    private static final int MAPSIZE = Game.MAPSIZE;

    private BoardButton[][] boardButtons = new BoardButton[MAPSIZE + 1][MAPSIZE + 1];

    private ArrayList<JButton> uiButtons = new ArrayList<>();

    private Game game;
    private int currentX;
    private int currentY;
    private Point origin;

    private UiTextManager uiTextManager;
    private BoardPanel boardPanel;

    private boolean unitSelected = false;

    GuiManager(Game game) throws IOException {
        this.game = game;
        boardPanel = new BoardPanel(MAPSIZE);

        BufferedImage backgroundImage = ImageIO.read(new File("textures\\backgrounds\\UI_texture.png"));
        JPanel uiPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, null);
            }
        };

        uiTextManager = new UiTextManager(uiPanel);

        createUIButtons(uiPanel);

        uiPanel.setLayout(new BorderLayout());
        uiPanel.setPreferredSize(new Dimension(1950, 125));

        JButton endTurn = new JButton("End Turn");
        endTurn.setBounds(1050, 25, 150, 80);
        endTurn.addActionListener(arg0 -> {
            game.swapPlayers();
            unitSelected = false;
            updateBoardButtonIconsAndBorders();
            hideUIButtons();
            uiTextManager.updateUI(game.getCurrentPlayer());
        });
        uiPanel.add(endTurn, BorderLayout.LINE_END);

        createBoardButtons();

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

    private void createUIButtons(JPanel uiPanel) {
        final int COLUMN_OFFSET = 120;
        final int FIRST_COLUMN = 300;
        final int FIRST_ROW = 10;
        final int LAST_COLUMN = 680;
        final int LAST_ROW = 70;

        int xPosition = FIRST_COLUMN;
        int yPosition = FIRST_ROW;

        for (int i = 0; i < 8; i++) {
            uiButtons.add(new JButton());
            uiButtons.get(i).setBounds(xPosition, yPosition, 110, 50);
            uiButtons.get(i).setVisible(false);
            uiButtons.get(i).setBackground(Color.white);
            uiPanel.add(uiButtons.get(i));
            xPosition = xPosition + COLUMN_OFFSET;
            if (xPosition > LAST_COLUMN) {
                yPosition = LAST_ROW;
                xPosition = FIRST_COLUMN;
            }
        }

        for (int i = 0; i < uiButtons.size(); i++) {
            int buttonNum = i;
            uiButtons.get(i).addActionListener(arg0 -> UIButtonAction(buttonNum));
        }
    }

    private void UIButtonAction(int ButtonNum) {
        String type;
        ArrayList<String> typesToCheck;
        if (unitSelected) {
            type = game.getMap().getUnit(currentX, currentY).getType();
            typesToCheck = game.getMap().getUnit(currentX, currentY).getButtonList();
        } else {
            type = game.getMap().getTile(currentX, currentY).getBuilding().getType();
            typesToCheck = game.getMap().getTile(currentX, currentY).getBuilding().getButtonList();
        }
        String buttonText = uiButtons.get(ButtonNum).getText();

        ButtonData buttonData = new ButtonData(type, currentX, currentY, buttonText);

        game.buttonClicked(buttonData);
        updateBoardButtonIconsAndBorders();
        uiTextManager.updateUI(game.getCurrentPlayer());

        if(unitSelected){
            unitSelected = false;
            hideUIButtons();
        }else{
            setButtonText(typesToCheck);
        }
    }

    private void hideUIButtons() {
        for (JButton UiButton : uiButtons) {
            UiButton.setVisible(false);
        }
    }

    private void resetUIColours() {
        for (JButton UiButton : uiButtons) {
            UiButton.setBackground(Color.white);
            UiButton.setEnabled(true);
        }
    }

    private void colourUIButtons(int index, String type) {
        if (game.checkAvailableResources(type)) {
            uiButtons.get(index).setBackground(Color.white);
            uiButtons.get(index).setEnabled(true);
        } else {
            uiButtons.get(index).setBackground(Color.red);
            uiButtons.get(index).setEnabled(false);
        }
    }

    private void createBoardButtons() {
        final int BUTTON_SIZE = 50;
        final int START_POSITION = 0;

        int xPosition = START_POSITION;
        int yPosition;

        //create board elements;
        for (int x = 0; x <= MAPSIZE; x++) {
            yPosition = START_POSITION;
            for (int y = 0; y <= MAPSIZE; y++) {
                BoardButton newBoardButton = new BoardButton(x, y);
                boardButtons[x][y] = newBoardButton;
                Tile currentTile = game.getMap().getTile(x, y);

                newBoardButton.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                    }

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
                    public void mouseExited(MouseEvent e) {
                    }
                });
                newBoardButton.addMouseMotionListener(new MouseMotionListener() {
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
                    public void mouseMoved(MouseEvent e) {

                    }
                });
                newBoardButton.setBounds(xPosition, yPosition, BUTTON_SIZE, BUTTON_SIZE);
                newBoardButton.setIcon(currentTile.getImage());
                newBoardButton.addActionListener(this);
                newBoardButton.setBorder(null);

                //set any borders
                if (currentTile.hasOwner()) {
                    createBorder(currentTile.getOwner().getColour(), newBoardButton);
                }
                yPosition = yPosition + BUTTON_SIZE; //increment y for next tile
                boardPanel.add(newBoardButton, BorderLayout.CENTER);
            }
            xPosition = xPosition + BUTTON_SIZE; //increment x for next tile
        }
    }

    public void actionPerformed(ActionEvent arg0) {
        BoardButton button = (BoardButton) arg0.getSource();
        int buttonXCoord = button.getXCoord();
        int buttonYCoord = button.getYCoord();
        Tile tileClicked = game.getMap().getTile(buttonXCoord,buttonYCoord);

        resetUIColours();
        uiTextManager.updateInformationText(tileClicked);

        if (unitSelected) {
            if(currentX == buttonXCoord && currentY == buttonYCoord) {
                deselectUnit();
            }else {
                performUnitMovement(arg0, buttonXCoord, buttonYCoord);
            }
        }else{
            performTileAction(buttonXCoord, buttonYCoord, tileClicked);
        }
    }

    private void performTileAction(int buttonXCoord, int buttonYCoord, Tile tileClicked){
        currentX = buttonXCoord;
        currentY = buttonYCoord;

        hideUIButtons();

        if (tileClicked.hasUnit() && tileClicked.getUnit().getOwner() == game.getCurrentPlayer()) {
            Unit unitClicked = tileClicked.getUnit();
            setButtonText(unitClicked.getButtonList());
            highlightTiles(currentX, currentY);
            unitSelected = true;
        } else if (tileClicked.hasBuilding() && tileClicked.getOwner() == game.getCurrentPlayer()) {
            setButtonText(tileClicked.getBuilding().getButtonList());
        }
    }

    private void deselectUnit(){
        unitSelected = false;
        updateBoardButtonIconsAndBorders();
        hideUIButtons();
    }

    private void performUnitMovement(ActionEvent arg0, int buttonXCoord, int buttonYCoord){
        if (game.isValidMove(currentX, currentY, buttonXCoord, buttonYCoord)) {
            game.moveUnit(currentX, currentY, buttonXCoord, buttonYCoord);
            boardButtons[currentX][currentY].setIcon(game.getMap().getTile(currentX,currentY).getImage());
        }
        unitSelected = false;
        updateBoardButtonIconsAndBorders();
        actionPerformed(arg0);
    }

    private void setButtonText(ArrayList<String> buttonsToBuild) {
        if (buttonsToBuild == null)
            return;
        int index = 0;
        for (String button : buttonsToBuild) {
            uiButtons.get(index).setText(button);
            uiButtons.get(index).setVisible(true);
            if (!button.equals("Road"))
                colourUIButtons(index, button);
            index++;
        }
    }

    private void highlightTiles(int startX, int startY) {
        final int BORDER_THICKNESS = 2;
        for (int endX = 0; endX < MAPSIZE; endX++) {
            for (int endY = 0; endY < MAPSIZE; endY++) {
                if (game.isValidMove(startX, startY, endX, endY)) {
                    boardButtons[endX][endY].setBorder(BorderFactory.createLineBorder(Color.white, BORDER_THICKNESS));
                }
            }
        }
    }

    private void updateBoardButtonIconsAndBorders() {
        final int UPDATE_AREA = 10;
        int xHigh = Math.min(currentX + UPDATE_AREA, MAPSIZE);
        int yHigh = Math.min(currentY + UPDATE_AREA, MAPSIZE);
        int xLow = Math.max(currentX - UPDATE_AREA, 0);
        int yLow = Math.max(currentY - UPDATE_AREA, 0);

        for (int x = xLow; x < xHigh; x++) {
            for (int y = yLow; y < yHigh; y++) {
                BoardButton buttonBeingUpdated = boardButtons[x][y];

                Tile currentTile = game.getMap().getTile(x, y);

                if (currentTile.hasUnit()) {
                    Unit currentUnit = currentTile.getUnit();
                    setButtonIconAndBorderUnit(currentUnit, buttonBeingUpdated);
                } else if (currentTile.hasOwner()) {
                    setButtonIconAndBorderBuilding(currentTile, buttonBeingUpdated);
                } else{
                    buttonBeingUpdated.setBorder(null);
                }
            }
        }
    }

    private void setButtonBorders(Tile currentTile, BoardButton buttonBeingUpdated){
        final int BORDER_THICKNESS = 1;
        Color tileOwnersColour = currentTile.getOwner().getColour();

        createBorder(tileOwnersColour, buttonBeingUpdated);

        if (currentTile.getResource().isInUse())
            buttonBeingUpdated.setBorder(BorderFactory.createLineBorder(tileOwnersColour, BORDER_THICKNESS));

        if (currentTile.hasBuilding() && currentTile.getBuilding().getHasCityConnection())
            buttonBeingUpdated.setBorder(BorderFactory.createLineBorder(Color.green, BORDER_THICKNESS));
    }

    private void createBorder(Color tileOwnersColour, BoardButton buttonBeingUpdated) {
        int x = buttonBeingUpdated.getXCoord();
        int y = buttonBeingUpdated.getYCoord();
        buttonBeingUpdated.setBorder(BorderFactory.createMatteBorder(
                game.getMap().borderRequired(x, y, x, y - 1),
                game.getMap().borderRequired(x, y, x - 1, y),
                game.getMap().borderRequired(x, y, x, y + 1),
                game.getMap().borderRequired(x, y, x + 1, y),
                tileOwnersColour));
    }

    private void drawRoad(BoardButton buttonBeingUpdated) {
        int x = buttonBeingUpdated.getXCoord();
        int y = buttonBeingUpdated.getYCoord();

        boolean North = game.getMap().roadAdjacent(x, y - 1); //north
        boolean South = game.getMap().roadAdjacent(x, y + 1); //south
        boolean East = game.getMap().roadAdjacent(x + 1, y); //east
        boolean West = game.getMap().roadAdjacent(x - 1, y); //west

        ImageIcon icon = game.getMap().getTile(x, y).getBuilding().getImage(North, South, East, West);

        buttonBeingUpdated.setIcon(icon);
    }

    private void setButtonIconAndBorderBuilding(Tile currentTile, BoardButton buttonBeingUpdated){
        if (currentTile.hasBuilding() && currentTile.getBuilding().getType().equals("Road")) {
            drawRoad(buttonBeingUpdated);
        } else {
            buttonBeingUpdated.setIcon(currentTile.getImage());
        }

        setButtonBorders(currentTile, buttonBeingUpdated);
    }

    private void setButtonIconAndBorderUnit(Unit currentUnit, BoardButton button){
        final int BORDER_THICKNESS = 1;
        button.setIcon(currentUnit.getImage());
        button.setBorder(BorderFactory.createLineBorder(currentUnit.getOwner().getColour(), BORDER_THICKNESS));
    }
}