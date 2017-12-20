package main;

import map.Tile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;


@SuppressWarnings("serial")
public class GuiManager extends JFrame implements ActionListener {
    private static final int MAPSIZE = GameController.MAPSIZE;
    private BoardButton[][] boardButtons = new BoardButton[MAPSIZE + 1][MAPSIZE + 1];
    private ArrayList<JButton> uiButtons = new ArrayList<>();
    private GameController gameController;
    private int currentX;
    private int currentY;
    private Point origin;
    private UiTextManager uiTextManager;
    private boolean unitSelected = false;

    GuiManager(GameController gameController) {
        this.gameController = gameController;
        BoardPanel boardPanel = new BoardPanel(MAPSIZE);
        UiPanel uiPanel = new UiPanel();
        uiTextManager = new UiTextManager(uiPanel);

        createUIButtons(uiPanel);

        createBoardButtons(boardPanel);

        FrameHandler.createAndSetupFrameAndScrollPane(uiPanel, boardPanel);

        uiTextManager.updateUI(gameController.getCurrentPlayer());
    }

    private void createUIButtons(JPanel uiPanel) {
        final int COLUMN_OFFSET = 120;
        final int FIRST_COLUMN = 300;
        final int FIRST_ROW = 10;
        final int LAST_COLUMN = 680;
        final int LAST_ROW = 70;

        int xPosition = FIRST_COLUMN;
        int yPosition = FIRST_ROW;

        JButton endTurn = new JButton("End Turn");
        endTurn.setBounds(1830, 0, 90, 125);
        endTurn.addActionListener(arg0 -> {
            gameController.nextPlayer();
            unitSelected = false;
            updateBoardButtonIconsAndBorders();
            hideUIButtons();
            uiTextManager.updateUI(gameController.getCurrentPlayer());
        });
        uiPanel.add(endTurn, BorderLayout.LINE_END);


        for (int i = 0; i < 8; i++) {
            uiButtons.add(new JButton());
            uiButtons.get(i).setBounds(xPosition, yPosition, 110, 50);
            uiButtons.get(i).setVisible(false);
            uiButtons.get(i).setBackground(Color.white);
            uiPanel.add(uiButtons.get(i), BorderLayout.CENTER);
            xPosition = xPosition + COLUMN_OFFSET;
            if (xPosition > LAST_COLUMN) {
                yPosition = LAST_ROW;
                xPosition = FIRST_COLUMN;
            }
        }

        for (int i = 0; i < uiButtons.size(); i++) {
            int buttonNum = i;
            uiButtons.get(i).addActionListener(arg0 -> uiButtonAction(buttonNum));
        }
    }

    private void uiButtonAction(int ButtonNum) {
        String buttonText = uiButtons.get(ButtonNum).getText();

        ButtonData buttonData = new ButtonData(unitSelected, currentX, currentY, buttonText);

        gameController.buttonClicked(buttonData);
        updateBoardButtonIconsAndBorders();
        uiTextManager.updateUI(gameController.getCurrentPlayer());

        if (unitSelected) {
            unitSelected = false;
            hideUIButtons();
        } else {
            setButtonText();
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
                buttonBeingUpdated.setBorder(null);

                Tile currentTile = gameController.getMap().getTile(x, y);

                ImageIcon icon = gameController.getTileImage(x,y);
                buttonBeingUpdated.setIcon(icon);

                setButtonBorders(currentTile, buttonBeingUpdated);
            }
        }
    }

    private void hideUIButtons() {
        for (JButton UiButton : uiButtons) {
            UiButton.setVisible(false);
        }
    }

    private void setButtonText() {
        ArrayList<String> buttonsToBuild = gameController.getTileButtonList(unitSelected, currentX, currentY);
        if(buttonsToBuild == null)
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

    private void colourUIButtons(int index, String type) {
        if (gameController.checkAvailableResources(type, unitSelected)) {
            uiButtons.get(index).setBackground(Color.white);
            uiButtons.get(index).setEnabled(true);
        } else {
            uiButtons.get(index).setBackground(Color.red);
            uiButtons.get(index).setEnabled(false);
        }
    }

    private void setButtonBorders(Tile currentTile, BoardButton buttonBeingUpdated) {
        Color borderColour;

        if(currentTile.hasOwner()){
            borderColour = currentTile.getOwner().getColour();
            createLargerBorder(borderColour, buttonBeingUpdated);
            if (currentTile.getResource().isInUse()) {
                createSmallBorder(borderColour, buttonBeingUpdated);
            }else if(currentTile.hasBuildingWithCityConnection()){
                createSmallBorder(Color.green, buttonBeingUpdated);
            }
        }

        if(currentTile.hasUnit()){
            borderColour = currentTile.getUnit().getOwner().getColour();
            createSmallBorder(borderColour, buttonBeingUpdated);
        }
    }

    private void createSmallBorder(Color borderColour, BoardButton buttonBeingUpdated){
        final int BORDER_THICKNESS = 1;
        buttonBeingUpdated.setBorder(BorderFactory.createLineBorder(borderColour, BORDER_THICKNESS));
    }

    private void createLargerBorder(Color borderColour, BoardButton buttonBeingUpdated) {
        int x = buttonBeingUpdated.getXCoord();
        int y = buttonBeingUpdated.getYCoord();

        buttonBeingUpdated.setBorder(BorderFactory.createMatteBorder(
                gameController.getMap().borderRequired(x, y, x, y - 1),
                gameController.getMap().borderRequired(x, y, x - 1, y),
                gameController.getMap().borderRequired(x, y, x, y + 1),
                gameController.getMap().borderRequired(x, y, x + 1, y),
                borderColour));
    }

    private void createBoardButtons(BoardPanel boardPanel) {
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
                Tile currentTile = gameController.getMap().getTile(x, y);

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
                    createLargerBorder(currentTile.getOwner().getColour(), newBoardButton);
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
        Tile tileClicked = gameController.getMap().getTile(buttonXCoord, buttonYCoord);

        resetUIColours();
        uiTextManager.updateInformationText(tileClicked);

        if (unitSelected) {
            if (currentX == buttonXCoord && currentY == buttonYCoord) {
                deselectUnit();
            } else {
                performUnitMovement(arg0, buttonXCoord, buttonYCoord);
            }
        } else {
            performTileAction(buttonXCoord, buttonYCoord, tileClicked);
        }
    }

    private void resetUIColours() {
        for (JButton UiButton : uiButtons) {
            UiButton.setBackground(Color.white);
            UiButton.setEnabled(true);
        }
    }

    private void deselectUnit() {
        unitSelected = false;
        updateBoardButtonIconsAndBorders();
        hideUIButtons();
    }

    private void performUnitMovement(ActionEvent arg0, int buttonXCoord, int buttonYCoord) {
        if (gameController.moveUnit(currentX, currentY, buttonXCoord, buttonYCoord)) {
            boardButtons[currentX][currentY].setIcon(gameController.getTileImage(buttonXCoord, buttonYCoord));
            unitSelected = false;
            updateBoardButtonIconsAndBorders();
            actionPerformed(arg0);
        }
    }

    private void performTileAction(int buttonXCoord, int buttonYCoord, Tile tileClicked) {
        currentX = buttonXCoord;
        currentY = buttonYCoord;

        hideUIButtons();

        if (tileClicked.hasUnit() && tileClicked.getUnit().getOwner() == gameController.getCurrentPlayer()) {
            unitSelected = true;
            highlightTiles(currentX, currentY);
            setButtonText();
        } else if (tileClicked.hasBuilding() && tileClicked.getOwner() == gameController.getCurrentPlayer()) {
            setButtonText();
        }
    }

    private void highlightTiles(int startX, int startY) {
        final int BORDER_THICKNESS = 2;
        for (int endX = 0; endX < MAPSIZE; endX++) {
            for (int endY = 0; endY < MAPSIZE; endY++) {
                if (gameController.isValidMove(startX, startY, endX, endY)) {
                    boardButtons[endX][endY].setBorder(BorderFactory.createLineBorder(Color.white, BORDER_THICKNESS));
                }
            }
        }
    }
}

class FrameHandler{
    public static void createAndSetupFrameAndScrollPane(JPanel uiPanel, BoardPanel boardPanel) {
        JScrollPane scrollPane = new JScrollPane(boardPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        JFrame frame = new JFrame("Civ like gameController");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(uiPanel, BorderLayout.SOUTH);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }
}